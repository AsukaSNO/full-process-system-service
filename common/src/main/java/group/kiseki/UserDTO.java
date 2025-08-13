package group.kiseki;

import lombok.Builder;
import lombok.Data;

/**
 * @author Yan
 */
@Data
@Builder
public class UserDTO {
    String loginName;
    String password;
}
