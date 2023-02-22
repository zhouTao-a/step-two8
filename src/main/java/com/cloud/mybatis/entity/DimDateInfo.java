package com.cloud.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 时间维度表
 * </p>
 *
 * @author zhout
 * @since 2022-12-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true) // 链式访问，该注解设置为chain=true，生成setter方法返回this
@TableName("dim_date_info")
public class DimDateInfo implements Serializable {
    private Integer id;

    /**
     * 日(yyyy-mm-dd)
     */
    @TableField("date")
    private String date;

    /**
     * 年
     */
    @TableField("year")
    private Integer year;

    /**
     * 第几季度
     */
    @TableField("quarter")
    private Integer quarter;

    /**
     * 所属季度(中文)
     */
    @TableField("quarter_cn")
    private String quarterCn;

    /**
     * 第几月
     */
    @TableField("month")
    private Integer month;

    /**
     * 所属月份(中文)
     */
    @TableField("month_cn")
    private String monthCn;

    /**
     * 所属月份(英文)
     */
    @TableField("month_en")
    private String monthEn;

    /**
     * 周几
     */
    @TableField("week")
    private Integer week;

    /**
     * 星期几(中文)
     */
    @TableField("week_cn")
    private String weekCn;

    /**
     * 星期几(英文)
     */
    @TableField("week_en")
    private String weekEn;

    /**
     * 本年第几周
     */
    @TableField("week_of_year")
    private Integer weekOfYear;

    /**
     * 每月的第几天
     */
    @TableField("day_of_month")
    private Integer dayOfMonth;

    /**
     * 本年第几天
     */
    @TableField("day_of_year")
    private Integer dayOfYear;


}
