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

import java.time.LocalDateTime;

/**
 * 用户会员等级表
 *
 * @author shenqiong
 * @date 2023-02-21 14:23:01
 */
@Data
@TableName("t_user_level")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户会员等级表")
public class UserLevel extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description ="主键id")
    private Long id;

    /**
     * 用户id
     */
    @Schema(description ="用户id")
    private Long userId;

    /**
     * 等级id
     */
    @Schema(description ="等级id")
    private Long levelId;

    /**
     * 等级名称
     */
    @Schema(description ="等级名称")
    private String levelName;

    /**
     * 生效时间
     */
    @Schema(description ="生效时间")
    private LocalDateTime effectiveTime;

    /**
     * 到期时间
     */
    @Schema(description ="到期时间")
    private LocalDateTime maturityTime;

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
