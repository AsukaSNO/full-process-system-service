package group.kiseki.auth.dto;

import lombok.Data;

/**
 * 登录请求DTO
 *
 * @author Yan
 */
@Data
public class LoginRequest {
    
    /**
     * 登录名
     */
    private String loginName;
    
    /**
     * 密码
     */
    private String password;
}
