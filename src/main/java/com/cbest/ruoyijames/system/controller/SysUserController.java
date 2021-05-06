package com.cbest.ruoyijames.system.controller;


import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cbest.ruoyijames.common.constant.StatusEnum;
import com.cbest.ruoyijames.common.model.AjaxResult;
import com.cbest.ruoyijames.common.model.MyPage;
import com.cbest.ruoyijames.system.entity.SysUser;
import com.cbest.ruoyijames.system.entity.SysUserPost;
import com.cbest.ruoyijames.system.entity.SysUserRole;
import com.cbest.ruoyijames.system.service.ISysUserPostService;
import com.cbest.ruoyijames.system.service.ISysUserRoleService;
import com.cbest.ruoyijames.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author James
 * @since 2021-04-23
 */
@AllArgsConstructor
@Api(tags = "用户管理")
@RestController
@RequestMapping("/system/sys-user")
public class SysUserController {

    private final ISysUserService iSysUserService;
    private final ISysUserPostService iSysUserPostService;
    private final ISysUserRoleService iSysUserRoleService;

    @ApiOperation("列表-带分页")
    @GetMapping
    public AjaxResult<IPage<SysUser>> list(SysUser user, MyPage page) {
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery(SysUser.class)
                .eq(user.getDeptId() != null, SysUser::getDeptId, user.getDeptId())
                .like(StringUtils.isNotEmpty(user.getName()), SysUser::getName, user.getName())
                .like(StringUtils.isNotEmpty(user.getPhoneNumber()), SysUser::getPhoneNumber, user.getPhoneNumber())
                .eq(user.getStatus() != null, SysUser::getStatus, user.getStatus())
                .orderByAsc(SysUser::getId);
        return AjaxResult.success(iSysUserService.page(new Page<>(page.getPageNum(), page.getPageSize()), queryWrapper));
    }

    @ApiOperation("查询单个")
    @GetMapping("/{id}")
    public AjaxResult<SysUser> getOne(@PathVariable Integer id) {
        SysUser user = iSysUserService.getById(id);
        if (user != null) {
            List<Integer> postIdList = iSysUserPostService.lambdaQuery()
                    .eq(SysUserPost::getUserId, id).select(SysUserPost::getPostId).list()
                    .stream().map(SysUserPost::getPostId).collect(Collectors.toList());
            user.setPostIdList(postIdList);
            List<Integer> roleIdList = iSysUserRoleService.lambdaQuery()
                    .eq(SysUserRole::getUserId, id).select(SysUserRole::getRoleId).list()
                    .stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            user.setRoleIdList(roleIdList);
        }
        return AjaxResult.success(user);
    }

    @ApiOperation("新增单个")
    @PostMapping
    public AjaxResult addOne(@RequestBody SysUser user) {
        iSysUserService.saveWithPostRole(user);
        return AjaxResult.success();
    }

    @ApiOperation("修改单个")
    @PutMapping
    AjaxResult updateOne(@RequestBody SysUser user) {
        iSysUserService.updateByIdWithPostRole(user);
        return AjaxResult.success();
    }

    @ApiOperation("修改单个-状态")
    @PutMapping("/{id}/status")
    AjaxResult updateOneStatus(@PathVariable Integer id, @RequestParam StatusEnum status) {
        iSysUserService.lambdaUpdate()
                .eq(SysUser::getId, id)
                .set(SysUser::getStatus, status)
                .update();
        return AjaxResult.success();
    }

    @ApiOperation("修改单个-密码")
    @PutMapping("/{id}/password")
    AjaxResult updateOnePassword(@PathVariable Integer id, @RequestParam String password) {
        iSysUserService.lambdaUpdate()
                .eq(SysUser::getId, id)
                .set(SysUser::getPassword, MD5.create().digestHex(password))
                .update();
        return AjaxResult.success();
    }

    @ApiOperation("删除单个")
    @DeleteMapping("/{id}")
    AjaxResult deleteOne(@PathVariable Integer id) {
        iSysUserService.removeById(id);
        return AjaxResult.success();
    }

}
