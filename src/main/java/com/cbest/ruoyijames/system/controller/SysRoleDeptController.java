package com.cbest.ruoyijames.system.controller;


import com.cbest.ruoyijames.common.model.AjaxResult;
import com.cbest.ruoyijames.system.entity.SysRoleDept;
import com.cbest.ruoyijames.system.service.ISysRoleDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色和部门关联表 前端控制器
 * </p>
 *
 * @author James
 * @since 2021-04-28
 */
@Api(tags = "角色-部门(数据权限)管理")
@AllArgsConstructor
@RestController
@RequestMapping("/system/sys-role-dept")
public class SysRoleDeptController {

    private final ISysRoleDeptService iSysRoleDeptService;

    @ApiOperation("根据角色ID查询部门ID列表")
    @GetMapping
    AjaxResult<List<Integer>> listDeptIds(@RequestParam Integer roleId) {
        List<Integer> deptIdList = iSysRoleDeptService.lambdaQuery()
                .eq(SysRoleDept::getRoleId, roleId)
                .select(SysRoleDept::getDeptId)
                .list()
                .stream()
                .map(SysRoleDept::getDeptId)
                .collect(Collectors.toList());
        return AjaxResult.success(deptIdList);
    }
}
