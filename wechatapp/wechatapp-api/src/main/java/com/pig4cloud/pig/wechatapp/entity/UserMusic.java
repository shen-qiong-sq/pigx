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

/**
 * 用户音乐播放列表
 *
 * @author shenqiong
 * @date 2023-02-21 14:23:10
 */
@Data
@TableName("t_user_music")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户音乐播放列表")
public class UserMusic extends BaseEntity {

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
     * 音乐id
     */
    @Schema(description ="音乐id")
    private Long musicId;

    /**
     * 排序
     */
    @Schema(description ="排序")
    private Long sort;

    /**
     * 播放次数
     */
    @Schema(description ="播放次数")
    private Long playNumber;

    /**
     * 是否加入喜欢：0-否 1-是
     */
    @Schema(description ="是否加入喜欢：0-否 1-是")
    private String isLike;

    /**
     * 是否下载本地：0-否 1-是
     */
    @Schema(description ="是否下载本地：0-否 1-是")
    private String isDown;

    /**
     * 是否播放：0-未播放 1-播放中
     */
    @Schema(description ="是否播放：0-未播放 1-播放中")
    private String isPlay;

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
