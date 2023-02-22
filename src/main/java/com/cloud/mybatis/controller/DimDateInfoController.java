package com.cloud.mybatis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.mybatis.entity.DimDateInfo;
import com.cloud.mybatis.mapper.DimDateInfoMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 时间维度表 前端控制器
 * </p>
 *
 * @author zhout
 * @since 2022-12-16
 */
@RestController
@RequestMapping("/date-info")
public class DimDateInfoController {

    @Resource
    private DimDateInfoMapper mapper;

    @GetMapping("/list/{date}")
    public List<DimDateInfo> list(@PathVariable String date){
        QueryWrapper<DimDateInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("date", date);
        return mapper.selectList(wrapper);
    }

}
