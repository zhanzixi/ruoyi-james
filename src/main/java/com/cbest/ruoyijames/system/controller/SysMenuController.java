package com.cbest.ruoyijames.system.controller;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.cbest.ruoyijames.common.model.AjaxResult;
import com.cbest.ruoyijames.common.util.ValidatorUtils;
import com.cbest.ruoyijames.system.entity.SysMenu;
import com.cbest.ruoyijames.system.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单权限表 前端控制器
 * </p>
 *
 * @author James
 * @since 2021-04-25
 */
@AllArgsConstructor
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/system/sys-menu")
public class SysMenuController {

    private final ValidatorUtils validatorUtils;
    private final ISysMenuService iSysMenuService;

    @ApiOperation("列表")
    @GetMapping
    public AjaxResult<List<SysMenu>> list(SysMenu menu) {
        LambdaQueryChainWrapper<SysMenu> query = iSysMenuService.lambdaQuery()
                .like(StringUtils.isNotEmpty(menu.getName()), SysMenu::getName, menu.getName())
                .eq(menu.getStatus() != null, SysMenu::getStatus, menu.getStatus())
                .orderByAsc(SysMenu::getParentId, SysMenu::getSort);
        return AjaxResult.success(query.list());
    }

    @ApiOperation("查询单个")
    @GetMapping("/{id}")
    public AjaxResult<SysMenu> getOne(@PathVariable Integer id) {
        return AjaxResult.success(iSysMenuService.getById(id));
    }

    @ApiOperation("新增单个")
    @PostMapping
    public AjaxResult addOne(@RequestBody SysMenu menu) {
        validatorUtils.validate(menu);
        iSysMenuService.save(menu);
        return AjaxResult.success();
    }

    @ApiOperation("修改单个")
    @PutMapping
    AjaxResult updateOne(@RequestBody SysMenu menu) {
        validatorUtils.validate(menu);
        iSysMenuService.updateById(menu);
        return AjaxResult.success();
    }

    @ApiOperation("删除单个")
    @DeleteMapping("/{id}")
    AjaxResult deleteOne(@PathVariable Integer id) {
        iSysMenuService.removeById(id);
        return AjaxResult.success();
    }

}
