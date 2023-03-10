/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.entity.*;
import com.pig4cloud.pig.admin.api.util.ParamResolver;
import com.pig4cloud.pig.admin.api.vo.UserExcelVO;
import com.pig4cloud.pig.admin.api.vo.UserVO;
import com.pig4cloud.pig.admin.mapper.*;
import com.pig4cloud.pig.admin.service.AppService;
import com.pig4cloud.pig.admin.service.SysMenuService;
import com.pig4cloud.pig.admin.service.SysUserService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.enums.MenuTypeEnum;
import com.pig4cloud.pig.common.core.exception.ErrorCodes;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.plugin.excel.vo.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2019/2/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

	private final AppService appService;

	private final SysRoleMapper sysRoleMapper;

	private final SysDeptMapper sysDeptMapper;

	private final SysMenuService sysMenuService;

	private final SysPostMapper sysPostMapper;

	private final SysUserRoleMapper sysUserRoleMapper;

	private final SysUserPostMapper sysUserPostMapper;

	/**
	 * ??????????????????
	 * @param userDto DTO ??????
	 * @return success/fail
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveUser(UserDTO userDto) {
		SysUser sysUser = new SysUser();
		BeanUtils.copyProperties(userDto, sysUser);
		sysUser.setDelFlag(CommonConstants.STATUS_NORMAL);
		sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
		baseMapper.insert(sysUser);
		userDto.getRole().stream().map(roleId -> {
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(sysUser.getUserId());
			userRole.setRoleId(roleId);
			return userRole;
		}).forEach(sysUserRoleMapper::insert);
		// ????????????????????????
		Optional.ofNullable(userDto.getPost()).ifPresent(posts -> {
			posts.stream().map(postId -> {
				SysUserPost userPost = new SysUserPost();
				userPost.setUserId(sysUser.getUserId());
				userPost.setPostId(postId);
				return userPost;
			}).forEach(sysUserPostMapper::insert);
		});
		return Boolean.TRUE;
	}

	/**
	 * ??????????????????????????????
	 * @param sysUser ??????
	 * @return
	 */
	@Override
	public UserInfo getUserInfo(SysUser sysUser) {
		UserInfo userInfo = new UserInfo();
		userInfo.setSysUser(sysUser);
		// ??????????????????
		List<SysRole> roleList = sysRoleMapper.listRolesByUserId(sysUser.getUserId());
		userInfo.setRoleList(roleList);
		// ?????????????????? ???ID???
		List<Long> roleIds = roleList.stream().map(SysRole::getRoleId).collect(Collectors.toList());
		userInfo.setRoles(ArrayUtil.toArray(roleIds, Long.class));
		// ??????????????????
		List<SysPost> postList = sysPostMapper.listPostsByUserId(sysUser.getUserId());
		userInfo.setPostList(postList);
		// ?????????????????????menu.permission???
		Set<String> permissions = roleIds.stream().map(sysMenuService::findMenuByRoleId).flatMap(Collection::stream)
				.filter(m -> MenuTypeEnum.BUTTON.getType().equals(m.getType())).map(SysMenu::getPermission)
				.filter(StrUtil::isNotBlank).collect(Collectors.toSet());
		userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));

		return userInfo;
	}

	/**
	 * ????????????????????????????????????????????????
	 * @param page ????????????
	 * @param userDTO ????????????
	 * @return
	 */
	@Override
	public IPage<UserVO> getUserWithRolePage(Page page, UserDTO userDTO) {
		return baseMapper.getUserVosPage(page, userDTO);
	}

	/**
	 * ??????ID??????????????????
	 * @param id ??????ID
	 * @return ????????????
	 */
	@Override
	public UserVO getUserVoById(Long id) {
		return baseMapper.getUserVoById(id);
	}

	/**
	 * ????????????
	 * @param sysUser ??????
	 * @return Boolean
	 */
	@Override
	@CacheEvict(value = CacheConstants.USER_DETAILS, key = "#sysUser.username")
	public Boolean removeUserById(SysUser sysUser) {
		sysUserRoleMapper.deleteByUserId(sysUser.getUserId());
		// ????????????????????????
		sysUserPostMapper.delete(Wrappers.<SysUserPost>lambdaQuery().eq(SysUserPost::getUserId, sysUser.getUserId()));
		this.removeById(sysUser.getUserId());
		return Boolean.TRUE;
	}

	@Override
	@CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
	public R<Boolean> updateUserInfo(UserDTO userDto) {
		UserVO userVO = baseMapper.getUserVoByUsername(userDto.getUsername());

		// ???????????????????????????,??????????????????????????????
		if (!StrUtil.equals(userVO.getPhone(), userDto.getPhone())) {
			if (!appService.check(userDto.getPhone(), userDto.getCode())) {
				return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_APP_SMS_ERROR));
			}
		}

		// ??????????????????
		SysUser sysUser = new SysUser();
		if (StrUtil.isNotBlank(userDto.getNewpassword1())) {
			Assert.isTrue(ENCODER.matches(userDto.getPassword(), userVO.getPassword()),
					MsgUtils.getMessage(ErrorCodes.SYS_USER_UPDATE_PASSWORDERROR));
			sysUser.setPassword(ENCODER.encode(userDto.getNewpassword1()));
		}
		sysUser.setPhone(userDto.getPhone());
		sysUser.setUserId(userVO.getUserId());
		sysUser.setAvatar(userDto.getAvatar());
		return R.ok(this.updateById(sysUser));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
	public R<Boolean> updateUser(UserDTO userDto) {
		SysUser sysUser = new SysUser();
		BeanUtils.copyProperties(userDto, sysUser);
		sysUser.setUpdateTime(LocalDateTime.now());

		if (StrUtil.isNotBlank(userDto.getPassword())) {
			sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
		}
		this.updateById(sysUser);

		sysUserRoleMapper
				.delete(Wrappers.<SysUserRole>update().lambda().eq(SysUserRole::getUserId, userDto.getUserId()));
		userDto.getRole().forEach(roleId -> {
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(sysUser.getUserId());
			userRole.setRoleId(roleId);
			userRole.insert();
		});
		sysUserPostMapper.delete(Wrappers.<SysUserPost>lambdaQuery().eq(SysUserPost::getUserId, userDto.getUserId()));
		userDto.getPost().forEach(postId -> {
			SysUserPost userPost = new SysUserPost();
			userPost.setUserId(sysUser.getUserId());
			userPost.setPostId(postId);
			userPost.insert();
		});
		return R.ok();
	}

	/**
	 * ?????????????????????????????????
	 * @param username ?????????
	 * @return R
	 */
	@Override
	public List<SysUser> listAncestorUsersByUsername(String username) {
		SysUser sysUser = this.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));

		SysDept sysDept = sysDeptMapper.selectById(sysUser.getDeptId());
		if (sysDept == null) {
			return null;
		}

		Long parentId = sysDept.getParentId();
		return this.list(Wrappers.<SysUser>query().lambda().eq(SysUser::getDeptId, parentId));
	}

	/**
	 * ?????????????????????
	 * @param userDTO ????????????
	 * @return list
	 */
	@Override
	public List<UserExcelVO> listUser(UserDTO userDTO) {
		List<UserVO> voList = baseMapper.selectVoList(userDTO);
		// ?????????execl ????????????
		List<UserExcelVO> userExcelVOList = voList.stream().map(userVO -> {
			UserExcelVO excelVO = new UserExcelVO();
			BeanUtils.copyProperties(userVO, excelVO);
			String roleNameList = userVO.getRoleList().stream().map(SysRole::getRoleName)
					.collect(Collectors.joining(StrUtil.COMMA));
			excelVO.setRoleNameList(roleNameList);
			String postNameList = userVO.getPostList().stream().map(SysPost::getPostName)
					.collect(Collectors.joining(StrUtil.COMMA));
			excelVO.setPostNameList(postNameList);
			return excelVO;
		}).collect(Collectors.toList());
		return userExcelVOList;
	}

	/**
	 * excel ????????????, ??????????????? ?????????????????????
	 * @param excelVOList excel ????????????
	 * @param bindingResult ????????????
	 * @return ok fail
	 */
	@Override
	public R importUser(List<UserExcelVO> excelVOList, BindingResult bindingResult) {
		// ?????????????????????????????????
		List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

		// ?????????????????????
		List<SysUser> userList = this.list();
		List<SysDept> deptList = sysDeptMapper.selectList(Wrappers.emptyWrapper());
		List<SysRole> roleList = sysRoleMapper.selectList(Wrappers.emptyWrapper());
		List<SysPost> postList = sysPostMapper.selectList(Wrappers.emptyWrapper());

		// ???????????????????????? ?????? UserDto
		for (UserExcelVO excel : excelVOList) {
			Set<String> errorMsg = new HashSet<>();
			// ???????????????????????????
			boolean exsitUserName = userList.stream()
					.anyMatch(sysUser -> excel.getUsername().equals(sysUser.getUsername()));

			if (exsitUserName) {
				errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERNAME_EXISTING, excel.getUsername()));
			}

			// ?????????????????????????????????????????????
			Optional<SysDept> deptOptional = deptList.stream()
					.filter(dept -> excel.getDeptName().equals(dept.getName())).findFirst();
			if (!deptOptional.isPresent()) {
				errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_DEPT_DEPTNAME_INEXISTENCE, excel.getDeptName()));
			}

			// ?????????????????????????????????????????????
			List<String> roleNameList = StrUtil.split(excel.getRoleNameList(), StrUtil.COMMA);
			List<SysRole> roleCollList = roleList.stream()
					.filter(role -> roleNameList.stream().anyMatch(name -> role.getRoleName().equals(name)))
					.collect(Collectors.toList());

			if (roleCollList.size() != roleNameList.size()) {
				errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_ROLE_ROLENAME_INEXISTENCE, excel.getRoleNameList()));
			}

			// ?????????????????????????????????????????????
			List<String> postNameList = StrUtil.split(excel.getPostNameList(), StrUtil.COMMA);
			List<SysPost> postCollList = postList.stream()
					.filter(post -> postNameList.stream().anyMatch(name -> post.getPostName().equals(name)))
					.collect(Collectors.toList());

			if (postCollList.size() != postNameList.size()) {
				errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_POST_POSTNAME_INEXISTENCE, excel.getPostNameList()));
			}

			// ??????????????????
			if (CollUtil.isEmpty(errorMsg)) {
				insertExcelUser(excel, deptOptional, roleCollList, postCollList);
			}
			else {
				// ?????????????????????
				errorMessageList.add(new ErrorMessage(excel.getLineNum(), errorMsg));
			}

		}

		if (CollUtil.isNotEmpty(errorMessageList)) {
			return R.failed(errorMessageList);
		}
		return R.ok();
	}

	@Override
	public List<Long> listUserIdByDeptIds(Set<Long> deptIds) {
		return this.listObjs(
				Wrappers.lambdaQuery(SysUser.class).select(SysUser::getUserId).in(SysUser::getDeptId, deptIds),
				Long.class::cast);
	}

	/**
	 * ??????excel User
	 */
	private void insertExcelUser(UserExcelVO excel, Optional<SysDept> deptOptional, List<SysRole> roleCollList,
			List<SysPost> postCollList) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(excel.getUsername());
		userDTO.setPhone(excel.getPhone());
		// ????????????????????????????????????
		userDTO.setPassword(userDTO.getPhone());
		// ??????????????????????????????ID
		userDTO.setDeptId(deptOptional.get().getDeptId());
		// ??????????????????????????????ID
		List<Long> roleIdList = roleCollList.stream().map(SysRole::getRoleId).collect(Collectors.toList());
		userDTO.setRole(roleIdList);
		List<Long> postIdList = postCollList.stream().map(SysPost::getPostId).collect(Collectors.toList());
		userDTO.setPost(postIdList);
		// ????????????
		this.saveUser(userDTO);
	}

	/**
	 * ???????????? ????????????????????????
	 * @param userDto ????????????
	 * @return success/false
	 */
	@Override
	public R<Boolean> registerUser(UserDTO userDto) {
		// ???????????????
		if (!appService.check(userDto.getPhone(), userDto.getCode())) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_APP_SMS_ERROR));
		}

		// ???????????????????????????
		SysUser sysUser = this.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, userDto.getUsername()));
		if (sysUser != null) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERNAME_EXISTING, userDto.getUsername()));
		}

		// ????????????????????????
		String defaultRole = ParamResolver.getStr("USER_DEFAULT_ROLE");
		// ????????????
		SysRole sysRole = sysRoleMapper
				.selectOne(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getRoleCode, defaultRole));

		if (sysRole == null) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_PARAM_CONFIG_ERROR, "USER_DEFAULT_ROLE"));
		}

		userDto.setRole(Collections.singletonList(sysRole.getRoleId()));
		return R.ok(saveUser(userDto));
	}

}
