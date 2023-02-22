package com.cloud.mybatis.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.mybatis.entity.DimDateInfo;

import java.util.List;

/**
 * <p>
 * 时间维度表 服务类
 * </p>
 *
 * @author zhout
 * @since 2022-12-16
 */
public interface IDimDateInfoService extends IService<DimDateInfo> {

    /**
     * 根据日期查询 返回DimDateInfo对象
     * @param date 日期字符串
     * @return DimDateInfo
     */
    DimDateInfo getByDate(String date);

    /**
     *
     * @param tableName 表名
     * @return 表创建语句
     */
    List<DimDateInfo> showTable(String tableName);
}
