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

package com.pig4cloud.pig.wechatapp.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.wechatapp.entity.UserMusic;
import com.pig4cloud.pig.wechatapp.service.UserMusicService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


/**
 * 用户音乐播放列表
 *
 * @author shenqiong
 * @date 2023-02-21 14:23:10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/usermusic" )
@Tag(name = "用户音乐播放列表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class UserMusicController {

    private final  UserMusicService userMusicService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param userMusic 用户音乐播放列表
     * @return
     */
    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('wechatapp_usermusic_get')" )
    public R getUserMusicPage(Page page, UserMusic userMusic) {
        return R.ok(userMusicService.page(page, Wrappers.query(userMusic)));
    }


    /**
     * 通过id查询用户音乐播放列表
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询", description = "通过id查询")
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('wechatapp_usermusic_get')" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(userMusicService.getById(id));
    }

    /**
     * 新增用户音乐播放列表
     * @param userMusic 用户音乐播放列表
     * @return R
     */
    @Operation(summary = "新增用户音乐播放列表", description = "新增用户音乐播放列表")
    @SysLog("新增用户音乐播放列表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('wechatapp_usermusic_add')" )
    public R save(@RequestBody UserMusic userMusic) {
        return R.ok(userMusicService.save(userMusic));
    }

    /**
     * 修改用户音乐播放列表
     * @param userMusic 用户音乐播放列表
     * @return R
     */
    @Operation(summary = "修改用户音乐播放列表", description = "修改用户音乐播放列表")
    @SysLog("修改用户音乐播放列表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('wechatapp_usermusic_edit')" )
    public R updateById(@RequestBody UserMusic userMusic) {
        return R.ok(userMusicService.updateById(userMusic));
    }

    /**
     * 通过id删除用户音乐播放列表
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id删除用户音乐播放列表", description = "通过id删除用户音乐播放列表")
    @SysLog("通过id删除用户音乐播放列表" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('wechatapp_usermusic_del')" )
    public R removeById(@PathVariable Long id) {
        return R.ok(userMusicService.removeById(id));
    }

}
