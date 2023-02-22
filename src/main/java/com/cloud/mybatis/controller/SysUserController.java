package com.cloud.mybatis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.mybatis.entity.SysUser;
import com.cloud.mybatis.service.ISysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author zhouTao
 * @since 2023-02-08
 */
@RestController
@RequestMapping("/sys-user")
public class SysUserController {

    @Resource
    private ISysUserService service;

    @PostMapping("/insert")
    public boolean insertUser(@RequestBody SysUser sysUser) {
        return service.save(sysUser);
    }

    @GetMapping("/select")
    public List<SysUser> selectUser() {
        return service.list();
    }

    @GetMapping("/selectPage")
    public Page<SysUser> selectUserPage(@RequestParam(name="pageNo", required = false, defaultValue="1") int pageNo,
                                        @RequestParam(name="pageSize", required = false, defaultValue="10") int pageSize) {
        Page<SysUser> page = new Page<>(pageNo, pageSize);
        return service.page(page);
    }


    @GetMapping("/selectOne/{id}")
    public List<SysUser> selectUserById(@PathVariable("id") Long id) {
        return service.list(new QueryWrapper<>(new SysUser()).eq("id", id));
    }

    @GetMapping("/updateOne")
    public boolean updateOne(@RequestParam("id") Long id, @RequestParam("name") String name) {
        SysUser user = new SysUser();
        user.setDisName(name);
        user.setVersion(null);
        return service.update(user, new QueryWrapper<SysUser>().eq("id", id));
    }

    @GetMapping("/updateAll")
    public boolean updateAll(@RequestParam("name") String name) {
        SysUser user = new SysUser();
        user.setDisName(name);
        return service.update(user, null);
    }

    @GetMapping("/delete/{id}")
    public boolean deleteUserById(@PathVariable("id") Long id) {
        return service.removeById(id);
    }
}
