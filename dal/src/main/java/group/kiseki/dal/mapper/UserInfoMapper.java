package group.kiseki.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import group.kiseki.dal.entity.UserInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息Mapper接口
 *
 * @author Yan
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfoDO> {

}
