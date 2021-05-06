package com.cbest.ruoyijames.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cbest.ruoyijames.common.model.AjaxResult;
import com.cbest.ruoyijames.common.model.MyPage;
import com.cbest.ruoyijames.common.util.ValidatorUtils;
import com.cbest.ruoyijames.system.entity.SysPost;
import com.cbest.ruoyijames.system.service.ISysPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 岗位信息表 前端控制器
 * </p>
 *
 * @author James
 * @since 2021-04-23
 */
@AllArgsConstructor
@Api(tags = "岗位管理")
@RestController
@RequestMapping("/system/sys-post")
public class SysPostController {

    private final ValidatorUtils validatorUtils;
    private final ISysPostService iSysPostService;


    @ApiOperation("列表-带分页")
    @GetMapping
    public AjaxResult<IPage<SysPost>> list(SysPost post, MyPage page) {
        LambdaQueryWrapper<SysPost> query = Wrappers.lambdaQuery(SysPost.class)
                .like(StringUtils.isNotEmpty(post.getCode()), SysPost::getCode, post.getCode())
                .like(StringUtils.isNotEmpty(post.getName()), SysPost::getName, post.getName())
                .eq(post.getStatus() != null, SysPost::getStatus, post.getStatus())
                .orderByAsc(SysPost::getSort);
        return AjaxResult.success(iSysPostService.page(new Page<>(page.getPageNum(), page.getPageSize()), query));
    }

    @ApiOperation("查询单个")
    @GetMapping("/{id}")
    public AjaxResult<SysPost> getOne(@PathVariable Integer id) {
        return AjaxResult.success(iSysPostService.getById(id));
    }

    @ApiOperation("新增单个")
    @PostMapping
    public AjaxResult addOne(@RequestBody SysPost post) {
        validatorUtils.validate(post);
        iSysPostService.save(post);
        return AjaxResult.success();
    }

    @ApiOperation("修改单个")
    @PutMapping
    AjaxResult updateOne(@RequestBody SysPost post) {
        validatorUtils.validate(post);
        iSysPostService.updateById(post);
        return AjaxResult.success();
    }

    @ApiOperation("删除单个")
    @DeleteMapping("/{id}")
    AjaxResult deleteOne(@PathVariable Integer id) {
        iSysPostService.removeById(id);
        return AjaxResult.success();
    }
}
