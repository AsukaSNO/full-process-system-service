package group.kiseki.auth.service;

import group.kiseki.auth.dto.LoginRequest;
import group.kiseki.auth.dto.LoginResult;
import group.kiseki.auth.dto.RefreshTokenRequest;
import group.kiseki.auth.dto.UserDTO;

/**
 * 认证服务接口
 *
 * @author Yan
 */
public interface AuthService {

    /**
     * 用户注册
     *
     * @param userDTO 用户注册信息
     * @return 注册结果
     */
    boolean register(UserDTO userDTO);

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @param clientIp 客户端IP
     * @param userAgent 用户代理
     * @return 登录结果
     */
    LoginResult login(LoginRequest loginRequest, String clientIp, String userAgent);

    /**
     * 刷新token
     *
     * @param refreshTokenRequest 刷新token请求
     * @return 新的token
     */
    String refreshToken(RefreshTokenRequest refreshTokenRequest);

    /**
     * 用户登出
     *
     * @param token 会话令牌
     * @return 登出结果
     */
    boolean logout(String token);

    /**
     * 验证token
     *
     * @param token 会话令牌
     * @return 用户ID，无效返回null
     */
    Integer validateToken(String token);

    /**
     * 检查用户名是否存在
     *
     * @param loginName 登录名
     * @return 是否存在
     */
    boolean isLoginNameExists(String loginName);
}
