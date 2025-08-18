package group.kiseki.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import group.kiseki.auth.entity.*;
import group.kiseki.auth.service.AuthService;
import group.kiseki.dal.entity.UserAuthDO;
import group.kiseki.dal.entity.UserInfoDO;
import group.kiseki.dal.entity.UserSessionDO;
import group.kiseki.dal.mapper.UserAuthMapper;
import group.kiseki.dal.mapper.UserInfoMapper;
import group.kiseki.dal.mapper.UserSessionMapper;
import group.kiseki.dal.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
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

    // Token过期时间：1天
    private static final long TOKEN_EXPIRE_DAYS = 1;
    // 刷新Token过期时间：30天
    private static final long REFRESH_TOKEN_EXPIRE_DAYS = 30;

    @Override
    @Transactional
    public RegisterResult register(UserDTO userDTO) {
        if (Objects.isNull(userDTO) || StringUtils.isAnyBlank(userDTO.getLoginName(), userDTO.getPassword())) {
            return RegisterResult.WRONG_PARAM;
        }

        // 检查用户名是否已存在
        if (isLoginNameExists(userDTO.getLoginName())) {
            return RegisterResult.ALREADY_EXIST_LOGIN_NAME;
        }

        // 加盐
        String salt = generateRandom();
        String encryptedPassword = encryptPassword(userDTO.getPassword(), salt);

        // 保存用户基本信息
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setNickname(userDTO.getLoginName());
        userInfoService.save(userInfoDO);

        // 保存用户认证信息
        UserAuthDO userAuthDO = new UserAuthDO();
        userAuthDO.setUid(userInfoDO.getUid());
        userAuthDO.setLoginName(userDTO.getLoginName());
        userAuthDO.setPassword(encryptedPassword);
        userAuthDO.setSalt(salt);
        userAuthDO.setStatus(1);
        userAuthDO.setCreateTime(LocalDateTime.now());
        userAuthDO.setUpdateTime(LocalDateTime.now());

        return userAuthMapper.insert(userAuthDO) > 0 ? RegisterResult.SUCCESS : RegisterResult.FALI;
    }

    @Override
    @Transactional
    public LoginResult login(LoginRequest loginRequest, String clientIp, String userAgent) {
        // 查询用户认证信息
        LambdaQueryWrapper<UserAuthDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserAuthDO::getLoginName, loginRequest.getLoginName()).eq(UserAuthDO::getStatus, 1);
        UserAuthDO userAuthDO = userAuthMapper.selectOne(lambdaQueryWrapper);

        if (userAuthDO == null) {
            return null;
        }

        // 验证密码
        String encryptedPassword = encryptPassword(loginRequest.getPassword(), userAuthDO.getSalt());
        if (!encryptedPassword.equals(userAuthDO.getPassword())) {
            return null;
        }

        // 查询用户基本信息
        UserInfoDO userInfoDO = userInfoService.getById(userAuthDO.getUid());
        if (userInfoDO == null) {
            return null;
        }

        // 生成token和刷新token
        String token = generateToken();
        String refreshToken = generateToken();

        // 计算过期时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusDays(TOKEN_EXPIRE_DAYS);

        // 保存会话信息
        UserSessionDO userSessionDO = new UserSessionDO();
        userSessionDO.setUid(userInfoDO.getUid());
        userSessionDO.setToken(token);
        userSessionDO.setRefreshToken(refreshToken);
        userSessionDO.setExpireTime(expireTime);
        userSessionDO.setCreateTime(now);
        userSessionDO.setLastActiveTime(now);
        userSessionDO.setClientIp(clientIp);
        userSessionDO.setUserAgent(userAgent);
        userSessionDO.setStatus(1);

        userSessionMapper.insert(userSessionDO);

        // 更新最后登录时间
        userAuthDO.setLastLoginTime(now);
        userAuthDO.setLastLoginIp(clientIp);
        userAuthDO.setUpdateTime(now);
        userAuthMapper.updateById(userAuthDO);

        return LoginResult.builder()
                .token(token)
                .refreshToken(refreshToken)
                .userInfoDO(userInfoDO)
                .expireTime(expireTime.toEpochSecond(ZoneOffset.of("+8")))
                .build();
    }

    @Override
    public String refreshToken(RefreshTokenRequest refreshTokenRequest) {
        // 查询有效的刷新token
        LambdaQueryWrapper<UserSessionDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserSessionDO::getRefreshToken, refreshTokenRequest.getRefreshToken())
                   .eq(UserSessionDO::getStatus, 1)
                   .gt(UserSessionDO::getExpireTime, LocalDateTime.now());

        UserSessionDO userSessionDO = userSessionMapper.selectOne(lambdaQueryWrapper);
        if (userSessionDO == null) {
            return null;
        }

        // 生成新的token
        String newToken = generateToken();
        LocalDateTime newExpireTime = LocalDateTime.now().plusDays(TOKEN_EXPIRE_DAYS);

        // 更新会话信息
        userSessionDO.setToken(newToken);
        userSessionDO.setExpireTime(newExpireTime);
        userSessionDO.setLastActiveTime(LocalDateTime.now());
        userSessionMapper.updateById(userSessionDO);

        return newToken;
    }

    @Override
    public boolean logout(String token) {
        LambdaQueryWrapper<UserSessionDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserSessionDO::getToken, token);

        UserSessionDO userSessionDO = userSessionMapper.selectOne(lambdaQueryWrapper);
        if (userSessionDO != null) {
            userSessionDO.setStatus(0);
            userSessionDO.setLastActiveTime(LocalDateTime.now());
            return userSessionMapper.updateById(userSessionDO) > 0;
        }
        return false;
    }

    @Override
    public Integer validateToken(String token) {
        LambdaQueryWrapper<UserSessionDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserSessionDO::getToken, token)
                   .eq(UserSessionDO::getStatus, 1)
                   .gt(UserSessionDO::getExpireTime, LocalDateTime.now());

        UserSessionDO userSessionDO = userSessionMapper.selectOne(lambdaQueryWrapper);
        if (userSessionDO != null) {
            // 更新最后活跃时间
            userSessionDO.setLastActiveTime(LocalDateTime.now());
            userSessionMapper.updateById(userSessionDO);
            return userSessionDO.getUid();
        }
        return null;
    }

    @Override
    public boolean isLoginNameExists(String loginName) {
        LambdaQueryWrapper<UserAuthDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserAuthDO::getLoginName, loginName);
        return userAuthMapper.selectCount(lambdaQueryWrapper) > 0;
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
