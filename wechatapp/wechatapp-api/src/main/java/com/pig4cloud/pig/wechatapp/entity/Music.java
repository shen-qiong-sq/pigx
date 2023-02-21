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
 * 音乐信息表
 *
 * @author shenqiong
 * @date 2023-02-21 14:22:38
 */
@Data
@TableName("t_music")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "音乐信息表")
public class Music extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description ="主键id")
    private Long id;

    /**
     * 音乐名称
     */
    @Schema(description ="音乐名称")
    private String name;

    /**
     * 歌手名称
     */
    @Schema(description ="歌手名称")
    private String singer;

    /**
     * 原唱
     */
    @Schema(description ="原唱")
    private String originalSong;

    /**
     * 作词
     */
    @Schema(description ="作词")
    private String lyricism;

    /**
     * 作曲
     */
    @Schema(description ="作曲")
    private String composeMusic;

    /**
     * 歌词
     */
    @Schema(description ="歌词")
    private String lyrics;

    /**
     * 歌曲类型：1-流行 2-电音 3-摇滚 4-纯音乐 5-国风 6-嘻哈 7-R&B 8-民谣 9-DJ 10-古典 11-世界音乐 12-乡村 13-爵士 14-二次元
     */
    @Schema(description ="歌曲类型：1-流行 2-电音 3-摇滚 4-纯音乐 5-国风 6-嘻哈 7-R&B 8-民谣 9-DJ 10-古典 11-世界音乐 12-乡村 13-爵士 14-二次元")
    private String type;

    /**
     * 时长，精确到秒，格式（分:秒）
     */
    @Schema(description ="时长，精确到秒，格式（分:秒）")
    private BigDecimal duration;

    /**
     * 发布日期
     */
    @Schema(description ="发布日期")
    private LocalDateTime releaseDate;

    /**
     * 上传日期
     */
    @Schema(description ="上传日期")
    private LocalDateTime uploadDate;

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
