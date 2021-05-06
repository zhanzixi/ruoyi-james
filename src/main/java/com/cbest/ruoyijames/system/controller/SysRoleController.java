package com.cbest.ruoyijames.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cbest.ruoyijames.common.model.AjaxResult;
import com.cbest.ruoyijames.common.model.MyPage;
import com.cbest.ruoyijames.common.util.ValidatorUtils;
import com.cbest.ruoyijames.system.entity.SysRole;
import com.cbest.ruoyijames.system.entity.SysRoleDept;
import com.cbest.ruoyijames.system.entity.SysRoleMenu;
import com.cbest.ruoyijames.system.service.ISysRoleDeptService;
import com.cbest.ruoyijames.system.service.ISysRoleMenuService;
import com.cbest.ruoyijames.system.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色信息表 前端控制器
 * </p>
 *
 * @author James
 * @since 2021-04-27
 */
@AllArgsConstructor
@Api(tags = "权限管理")
@RestController
@RequestMapping("/system/sys-role")
public class SysRoleController {
    private final ValidatorUtils validatorUtils;
    private final ISysRoleService iSysRoleService;
    private final ISysRoleMenuService iSysRoleMenuService;
    private final ISysRoleDeptService iSysRoleDeptService;

    @ApiOperation("列表-带分页")
    @GetMapping
    public AjaxResult<IPage<SysRole>> list(SysRole role, MyPage page) {
        LambdaQueryWrapper<SysRole> query = Wrappers.lambdaQuery(SysRole.class)
                .like(StringUtils.isNotEmpty(role.getCode()), SysRole::getCode, role.getCode())
                .like(StringUtils.isNotEmpty(role.getName()), SysRole::getName, role.getName())
                .eq(role.getStatus() != null, SysRole::getStatus, role.getStatus())
                .orderByAsc(SysRole::getSort);
        return AjaxResult.success(iSysRoleService.page(new Page<>(page.getPageNum(), page.getPageSize()), query));
    }

    @ApiOperation("查询单个")
    @GetMapping("/{id}")
    public AjaxResult<SysRole> getOne(@PathVariable Integer id) {
        SysRole role = iSysRoleService.getById(id);
        if (role != null) {
            List<Integer> menuIdList = iSysRoleMenuService.lambdaQuery()
                    .eq(SysRoleMenu::getRoleId, id).select(SysRoleMenu::getMenuId).list()
                    .stream().map(SysRoleMenu::getMenuId)
                    .collect(Collectors.toList());
            role.setMenuIdList(menuIdList);

            List<Integer> deptIdList = iSysRoleDeptService.lambdaQuery()
                    .eq(SysRoleDept::getRoleId, id).select(SysRoleDept::getDeptId).list()
                    .stream().map(SysRoleDept::getDeptId)
                    .collect(Collectors.toList());
            role.setDeptIdList(deptIdList);
        }
        return AjaxResult.success(role);
    }

    @ApiOperation("新增单个")
    @PostMapping
    public AjaxResult saveOne(@RequestBody SysRole role) {
        iSysRoleService.saveWithMenus(role);
        return AjaxResult.success();
    }

    @ApiOperation("修改单个")
    @PutMapping
    AjaxResult updateOne(@RequestBody SysRole role) {
        validatorUtils.validate(role);
        iSysRoleService.updateByIdWithMenus(role);
        return AjaxResult.success();
    }

    @ApiOperation("修改单个 数据权限")
    @PutMapping("/{id}/data-scope")
    AjaxResult updateOneDataScope(@PathVariable Integer id, @RequestBody SysRole role) {
        role.setId(id);
        iSysRoleService.updateByIdWithDept(role);
        return AjaxResult.success();
    }

    @ApiOperation("删除单个")
    @DeleteMapping("/{id}")
    AjaxResult removeOne(@PathVariable Integer id) {
        iSysRoleService.removeById(id);
        return AjaxResult.success();
    }

}
