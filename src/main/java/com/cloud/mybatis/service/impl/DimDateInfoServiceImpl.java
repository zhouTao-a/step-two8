package com.cloud.mybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.mybatis.entity.DimDateInfo;
import com.cloud.mybatis.mapper.DimDateInfoMapper;
import com.cloud.mybatis.service.IDimDateInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 时间维度表 服务实现类
 * </p>
 *
 * @author zhout
 * @since 2022-12-16
 */
@Service
public class DimDateInfoServiceImpl extends ServiceImpl<DimDateInfoMapper, DimDateInfo> implements IDimDateInfoService {

    @Resource
    private DimDateInfoMapper mapper;

    @Override
    public DimDateInfo getByDate(String date) {
        if (ObjectUtils.isEmpty(date)) {
            throw new RuntimeException("日期不能为空");
        }
        return mapper.getByDate(date);
    }

    @Override
    public List<DimDateInfo> showTable(String tableName) {
        return mapper.showTable(tableName);
    }
}
