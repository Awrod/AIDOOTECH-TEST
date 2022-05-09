package com.aidootech.aidootechtest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author cxl
 * @since 2022-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AidootechUser对象", description="")
public class AidootechUser extends Model<AidootechUser> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户ID",hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "出生日期")
    private Date dob;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "简介")
    private String description;

    @ApiModelProperty(value = "创建日期",hidden = true)
    @TableField("createdAt")
    private Date createdAt;

    @ApiModelProperty(value = "用户类型",hidden = true)
    private String type;

    @ApiModelProperty(value = "密码")
    private String password;



}
