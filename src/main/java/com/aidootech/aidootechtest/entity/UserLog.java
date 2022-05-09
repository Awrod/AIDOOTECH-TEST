package com.aidootech.aidootechtest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel(value="UserLog对象", description="")
public class UserLog extends Model<UserLog> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "日志编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userid;

    @ApiModelProperty(value = "操作类型")
    private String operation;

    @ApiModelProperty(value = "操作时间")
    private Date operationtime;



}
