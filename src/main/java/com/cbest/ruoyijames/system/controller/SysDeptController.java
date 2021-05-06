package com.cbest.ruoyijames.system.controller;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.cbest.ruoyijames.common.model.AjaxResult;
import com.cbest.ruoyijames.common.util.ValidatorUtils;
import com.cbest.ruoyijames.system.entity.SysDept;
import com.cbest.ruoyijames.system.service.ISysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author James
 * @since 2021-04-21
 */
@AllArgsConstructor
@Api(tags = "部门管理")
@RestController
@RequestMapping("/system/sys-dept")
public class SysDeptController {

    private final ValidatorUtils validatorUtils;
    private final ISysDeptService iSysDeptService;

    @ApiOperation("列表")
    @GetMapping
    public AjaxResult<List<SysDept>> list(SysDept dept) {
        LambdaQueryChainWrapper<SysDept> query = iSysDeptService.lambdaQuery()
                .like(StringUtils.isNotEmpty(dept.getName()), SysDept::getName, dept.getName())
                .eq(dept.getStatus() != null, SysDept::getStatus, dept.getStatus())
                .orderByAsc(SysDept::getParentId, SysDept::getSort);
        return AjaxResult.success(query.list());
    }

    @ApiOperation("查询单个")
    @GetMapping("/{id}")
    public AjaxResult<SysDept> getOne(@PathVariable Integer id) {
        return AjaxResult.success(iSysDeptService.getById(id));
    }

    @ApiOperation("删除单个部门")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> deleteOne(@PathVariable Integer id) {
        LambdaQueryChainWrapper<SysDept> query = iSysDeptService.lambdaQuery().eq(SysDept::getParentId, id);
        if (query.count() > 0) {
            throw new IllegalArgumentException("该部门下还有子部门！");
        }
        return AjaxResult.success(iSysDeptService.removeById(id));
    }

    @ApiOperation("新增单个")
    @PostMapping
    public AjaxResult add(@RequestBody SysDept sysDept) {
        validatorUtils.validate(sysDept);
        iSysDeptService.save(sysDept);
        return AjaxResult.success();
    }

    @ApiOperation("修改单个")
    @PutMapping
    public AjaxResult updateOne(@RequestBody SysDept sysDept) {
        validatorUtils.validate(sysDept);
        iSysDeptService.updateById(sysDept);
        return AjaxResult.success();
    }
}
