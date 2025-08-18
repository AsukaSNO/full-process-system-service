package group.kiseki.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import group.kiseki.dal.entity.UserAuthDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户认证Mapper接口
 *
 * @author Yan
 */
@Mapper
public interface UserAuthMapper extends BaseMapper<UserAuthDO> {

}
