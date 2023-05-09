package com.cloud.mybatis.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.mybatis.entity.SysUser;
import com.cloud.mybatis.mapper.SysUserMapper;
import com.cloud.mybatis.service.ISysUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author zhouTao
 * @since 2023-02-08
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

}
