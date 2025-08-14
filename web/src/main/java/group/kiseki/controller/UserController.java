package group.kiseki.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import group.kiseki.dal.entity.UserInfo;
import group.kiseki.dal.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 *
 * @author Yan
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 创建用户
     */
    @PostMapping
    public UserInfo createUser(@RequestBody UserInfo userInfo) {
        userInfoService.save(userInfo);
        return userInfo;
    }

    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public UserInfo getUserById(@PathVariable Integer id) {
        return userInfoService.getById(id);
    }

    /**
     * 获取所有用户
     */
    @GetMapping
    public List<UserInfo> getAllUsers() {
        return userInfoService.list();
    }

    /**
     * 分页查询用户
     */
    @GetMapping("/page")
    public Page<UserInfo> getUsersByPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<UserInfo> page = new Page<>(current, size);
        return userInfoService.page(page);
    }

    /**
     * 根据昵称查询用户
     */
    @GetMapping("/search")
    public List<UserInfo> searchUsersByNickname(@RequestParam String nickname) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("nickname", nickname);
        return userInfoService.list(queryWrapper);
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public boolean updateUser(@PathVariable Integer id, @RequestBody UserInfo userInfo) {
        userInfo.setUid(id);
        return userInfoService.updateById(userInfo);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Integer id) {
        return userInfoService.removeById(id);
    }
}
