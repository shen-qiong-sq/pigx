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
 * 用户信息表
 *
 * @author shenqiong
 * @date 2023-02-21 14:22:52
 */
@Data
@TableName("t_user")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户信息表")
public class User extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description ="主键id")
    private Long id;

    /**
     * 用户名
     */
    @Schema(description ="用户名")
    private String name;

    /**
     * 密码
     */
    @Schema(description ="密码")
    private String password;

    /**
     * 真实姓名
     */
    @Schema(description ="真实姓名")
    private String realName;

    /**
     * 用户类型：0-系统管理员 1-普通用户 2-会员
     */
    @Schema(description ="用户类型：0-系统管理员 1-普通用户 2-会员")
    private String type;

    /**
     * 在线状态：0-在线 1-隐身 2-离线
     */
    @Schema(description ="在线状态：0-在线 1-隐身 2-离线")
    private String status;

    /**
     * 最后一次登录时间
     */
    @Schema(description ="最后一次登录时间")
    private LocalDateTime lastLoginTime;

    /**
     * 是否首次登录：0-是 1-否
     */
    @Schema(description ="是否首次登录：0-是 1-否")
    private String firstLoginFlag;

    /**
     * 注册时间
     */
    @Schema(description ="注册时间")
    private LocalDateTime registerTime;

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
