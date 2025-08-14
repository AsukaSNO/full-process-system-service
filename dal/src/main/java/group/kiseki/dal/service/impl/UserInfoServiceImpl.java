package group.kiseki.dal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import group.kiseki.dal.entity.UserInfo;
import group.kiseki.dal.mapper.UserInfoMapper;
import group.kiseki.dal.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
 * 用户信息服务实现类
 *
 * @author Yan
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
