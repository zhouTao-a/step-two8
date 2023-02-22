package com.cloud.mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author zhouTao
 * @since 2023-02-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("dis_name")
    private String disName;

    @TableField("login_name")
    private String loginName;

    @TableField("password")
    private String password;

    @TableField(value = "deleted")
    private Integer deleted;

    @TableField("version")
    @Version
    private Integer version;

    @TableField("tenant_id")
    private Integer tenantId;
}
