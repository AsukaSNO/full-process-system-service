package group.kiseki.auth.dto;

import lombok.Data;

/**
 * 刷新Token请求DTO
 *
 * @author Yan
 */
@Data
public class RefreshTokenRequest {
    
    /**
     * 刷新令牌
     */
    private String refreshToken;
}
