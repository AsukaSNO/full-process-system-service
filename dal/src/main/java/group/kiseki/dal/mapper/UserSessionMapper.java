package group.kiseki.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import group.kiseki.dal.entity.UserSessionDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户会话Mapper接口
 *
 * @author Yan
 */
@Mapper
public interface UserSessionMapper extends BaseMapper<UserSessionDO> {

}
