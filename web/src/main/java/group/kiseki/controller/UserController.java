package group.kiseki.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import group.kiseki.dal.entity.UserInfoDO;
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
    public UserInfoDO createUser(@RequestBody UserInfoDO userInfoDO) {
        userInfoService.save(userInfoDO);
        return userInfoDO;
    }

    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public UserInfoDO getUserById(@PathVariable Integer id) {
        return userInfoService.getById(id);
    }

    /**
     * 获取所有用户
     */
    @GetMapping
    public List<UserInfoDO> getAllUsers() {
        return userInfoService.list();
    }

    /**
     * 分页查询用户
     */
    @GetMapping("/page")
    public Page<UserInfoDO> getUsersByPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<UserInfoDO> page = new Page<>(current, size);
        return userInfoService.page(page);
    }

    /**
     * 根据昵称查询用户
     */
    @GetMapping("/search")
    public List<UserInfoDO> searchUsersByNickname(@RequestParam String nickname) {
        LambdaQueryWrapper<UserInfoDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(UserInfoDO::getNickname, nickname);
        return userInfoService.list(lambdaQueryWrapper);
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public boolean updateUser(@PathVariable Integer id, @RequestBody UserInfoDO userInfoDO) {
        userInfoDO.setUid(id);
        return userInfoService.updateById(userInfoDO);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Integer id) {
        return userInfoService.removeById(id);
    }
}
