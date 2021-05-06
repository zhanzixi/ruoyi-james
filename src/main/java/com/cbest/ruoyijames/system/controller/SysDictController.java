package com.cbest.ruoyijames.system.controller;

import com.cbest.ruoyijames.common.constant.StatusEnum;
import com.cbest.ruoyijames.common.model.AjaxResult;
import com.cbest.ruoyijames.common.model.LabelValueBean;
import com.cbest.ruoyijames.system.entity.SysMenu;
import com.cbest.ruoyijames.system.entity.SysRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author James
 * @date 2021/4/21 16:07
 */
@AllArgsConstructor
@Api(tags = "字典管理")
@RestController
@RequestMapping("/system/dict")
public class SysDictController {

    @ApiImplicitParam(name = "type", value = "字典类型")
    @ApiOperation("得到字典数据")
    @GetMapping("/data/{type}")
    public AjaxResult<List<LabelValueBean>> list(@PathVariable String type) {
        switch (type) {
            case "common_status":
                return AjaxResult.success(
                        Arrays.stream(StatusEnum.values())
                                .map(e -> new LabelValueBean<>(e.getName(), e.name()))
                                .collect(Collectors.toList())
                );
            case "sys_menu_type":
                return AjaxResult.success(
                        Arrays.stream(SysMenu.Type.values())
                                .map(e -> new LabelValueBean<>(e.getName(), e.name()))
                                .collect(Collectors.toList())
                );
            case "sys_role_data_scope":
                return AjaxResult.success(
                        Arrays.stream(SysRole.DataScopeEnum.values())
                                .map(e -> new LabelValueBean<>(e.getName(), e.name()))
                                .collect(Collectors.toList())
                );
        }
        throw new IllegalArgumentException("type error, please input (common_status,sys_menu_type,sys_role_data_scope)");
    }
}
