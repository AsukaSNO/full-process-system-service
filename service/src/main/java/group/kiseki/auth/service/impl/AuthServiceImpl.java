package group.kiseki.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import group.kiseki.auth.dto.LoginRequest;
import group.kiseki.auth.dto.LoginResult;
import group.kiseki.auth.dto.RefreshTokenRequest;
import group.kiseki.auth.dto.UserDTO;
import group.kiseki.auth.service.AuthService;
import group.kiseki.dal.entity.UserAuth;
import group.kiseki.dal.entity.UserInfo;
import group.kiseki.dal.entity.UserSession;
import group.kiseki.dal.mapper.UserAuthMapper;
import group.kiseki.dal.mapper.UserInfoMapper;
import group.kiseki.dal.mapper.UserSessionMapper;
import group.kiseki.dal.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

/**
 * 认证服务实现类
 *
 * @author Yan
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private UserSessionMapper userSessionMapper;

    // Token过期时间：30天
    private static final long TOKEN_EXPIRE_DAYS = 30;
    // 刷新Token过期时间：60天
    private static final long REFRESH_TOKEN_EXPIRE_DAYS = 60;

    @Override
    @Transactional
    public boolean register(UserDTO userDTO) {
        // 检查参数
        if (Objects.isNull(userDTO) || Objects.isNull(userDTO.getLoginName()) || Objects.isNull(userDTO.getPassword())) {
            return false;
        }

        // 检查用户名是否已存在
        if (isLoginNameExists(userDTO.getLoginName())) {
            return false;
        }

        // 加盐
        String salt = generateRandom();
        String encryptedPassword = encryptPassword(userDTO.getPassword(), salt);

        // 保存用户基本信息
        UserInfo userInfo = new UserInfo();
        userInfo.setNickname(userDTO.getLoginName());

        userInfoService.save(userInfo);

        // 保存用户认证信息
        UserAuth userAuth = new UserAuth();
        userAuth.setUid(userInfo.getUid());
        userAuth.setLoginName(userDTO.getLoginName());
        userAuth.setPassword(encryptedPassword);
        userAuth.setSalt(salt);
        userAuth.setStatus(1);
        userAuth.setCreateTime(LocalDateTime.now());
        userAuth.setUpdateTime(LocalDateTime.now());

        return userAuthMapper.insert(userAuth) > 0;
    }

    @Override
    @Transactional
    public LoginResult login(LoginRequest loginRequest, String clientIp, String userAgent) {
        // 查询用户认证信息
        QueryWrapper<UserAuth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("loginName", loginRequest.getLoginName()).eq("status", 1);
        UserAuth userAuth = userAuthMapper.selectOne(queryWrapper);

        if (userAuth == null) {
            return null;
        }

        // 验证密码
        String encryptedPassword = encryptPassword(loginRequest.getPassword(), userAuth.getSalt());
        if (!encryptedPassword.equals(userAuth.getPassword())) {
            return null;
        }

        // 查询用户基本信息
        UserInfo userInfo = userInfoService.getById(userAuth.getUid());
        if (userInfo == null) {
            return null;
        }

        // 生成token和刷新token
        String token = generateToken();
        String refreshToken = generateToken();

        // 计算过期时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusDays(TOKEN_EXPIRE_DAYS);

        // 保存会话信息
        UserSession userSession = new UserSession();
        userSession.setUid(userInfo.getUid());
        userSession.setToken(token);
        userSession.setRefreshToken(refreshToken);
        userSession.setExpireTime(expireTime);
        userSession.setCreateTime(now);
        userSession.setLastActiveTime(now);
        userSession.setClientIp(clientIp);
        userSession.setUserAgent(userAgent);
        userSession.setStatus(1);

        userSessionMapper.insert(userSession);

        // 更新最后登录时间
        userAuth.setLastLoginTime(now);
        userAuth.setLastLoginIp(clientIp);
        userAuth.setUpdateTime(now);
        userAuthMapper.updateById(userAuth);

        return LoginResult.builder()
                .token(token)
                .refreshToken(refreshToken)
                .userInfo(userInfo)
                .expireTime(expireTime.toEpochSecond(ZoneOffset.of("+8")))
                .build();
    }

    @Override
    public String refreshToken(RefreshTokenRequest refreshTokenRequest) {
        // 查询有效的刷新token
        QueryWrapper<UserSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("refresh_token", refreshTokenRequest.getRefreshToken())
                   .eq("status", 1)
                   .gt("expire_time", LocalDateTime.now());

        UserSession userSession = userSessionMapper.selectOne(queryWrapper);
        if (userSession == null) {
            return null;
        }

        // 生成新的token
        String newToken = generateToken();
        LocalDateTime newExpireTime = LocalDateTime.now().plusDays(TOKEN_EXPIRE_DAYS);

        // 更新会话信息
        userSession.setToken(newToken);
        userSession.setExpireTime(newExpireTime);
        userSession.setLastActiveTime(LocalDateTime.now());
        userSessionMapper.updateById(userSession);

        return newToken;
    }

    @Override
    public boolean logout(String token) {
        QueryWrapper<UserSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token", token);

        UserSession userSession = userSessionMapper.selectOne(queryWrapper);
        if (userSession != null) {
            userSession.setStatus(0);
            userSession.setLastActiveTime(LocalDateTime.now());
            return userSessionMapper.updateById(userSession) > 0;
        }
        return false;
    }

    @Override
    public Integer validateToken(String token) {
        QueryWrapper<UserSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token", token)
                   .eq("status", 1)
                   .gt("expire_time", LocalDateTime.now());

        UserSession userSession = userSessionMapper.selectOne(queryWrapper);
        if (userSession != null) {
            // 更新最后活跃时间
            userSession.setLastActiveTime(LocalDateTime.now());
            userSessionMapper.updateById(userSession);
            return userSession.getUid();
        }
        return null;
    }

    @Override
    public boolean isLoginNameExists(String loginName) {
        QueryWrapper<UserAuth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("loginName", loginName);
        return userAuthMapper.selectCount(queryWrapper) > 0;
    }

    /**
     * 生成16位随机字符串
     */
    private String generateRandom() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    /**
     * 加密密码
     */
    private String encryptPassword(String password, String salt) {
        String saltedPassword = password + salt;
        return DigestUtils.md5DigestAsHex(saltedPassword.getBytes());
    }

    /**
     * 生成token
     */
    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
