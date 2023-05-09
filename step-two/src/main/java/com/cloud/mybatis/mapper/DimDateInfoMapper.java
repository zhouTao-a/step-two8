package com.cloud.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.mybatis.entity.DimDateInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 时间维度表 Mapper 接口
 * </p>
 *
 * @author zhout
 * @since 2022-12-16
 */
@Mapper
public interface DimDateInfoMapper extends BaseMapper<DimDateInfo> {

    /**
     * 根据日期查询 返回DimDateInfo对象
     * @param date 日期字符串
     * @return DimDateInfo
     */
    DimDateInfo getByDate(@Param("date") String date);

    /**
     *
     * @param tableName 表名
     * @return 表创建语句
     */
    List<DimDateInfo> showTable(@Param("tableName") String tableName);
}
