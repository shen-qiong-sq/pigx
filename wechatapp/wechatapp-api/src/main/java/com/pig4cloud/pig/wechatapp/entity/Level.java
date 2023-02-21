/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.pig4cloud.pig.wechatapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pig4cloud.pig.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员等级信息表
 *
 * @author shenqiong
 * @date 2023-02-20 17:52:51
 */
@Data
@TableName("t_level")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "会员等级信息表")
public class Level extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description ="主键id")
    private Long id;

    /**
     * 等级名称
     */
    @Schema(description ="等级名称")
    private String levelName;

    /**
     * 价格
     */
    @Schema(description ="价格")
    private BigDecimal price;

    /**
     * 总金额
     */
    @Schema(description ="总金额")
    private BigDecimal totalAmount;

    /**
     * 有效期
     */
    @Schema(description ="有效期")
    private LocalDateTime validityPeriod;

    /**
     * 备注
     */
    @Schema(description ="备注")
    private String remark;

    /**
     * 删除标识：0-正常 1-已删除
     */
    @Schema(description ="删除标识：0-正常 1-已删除")
    private String delFlag;


}
