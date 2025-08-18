package group.kiseki.auth.entity;

import group.kiseki.dal.entity.UserInfoDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录结果DTO
 *
 * @author Yan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResult {

    /**
     * 访问令牌
     */
    private String token;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 用户信息
     */
    private UserInfoDO userInfoDO;

    /**
     * 过期时间（时间戳）
     */
    private long expireTime;
}
