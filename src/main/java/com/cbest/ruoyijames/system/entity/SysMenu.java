package com.cbest.ruoyijames.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.cbest.ruoyijames.common.constant.StatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author James
 * @since 2021-04-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysMenu对象", description = "菜单权限表")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "父菜单ID")
    private Integer parentId;

    @NotEmpty
    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "显示顺序")
    private Integer sort;

    @NotEmpty
    @ApiModelProperty(value = "路由地址")
    private String path;

    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "是否为外链（0否 1是）")
    private Integer isFrame;

    @ApiModelProperty(value = "是否缓存（0不缓存 1缓存）")
    private Integer isCache;

    @ApiModelProperty(value = "菜单类型（1目录 2菜单 3按钮）")
    private Type type;

    @ApiModelProperty(value = "菜单状态（0隐藏 1显示）")
    private Integer visible;

    @ApiModelProperty(value = "菜单状态（0正常 1停用）")
    private StatusEnum status;

    @ApiModelProperty(value = "权限标识")
    private String perms;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "更新者")
    private String modifiedBy;

    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModified;

    public enum Type implements IEnum<Integer> {
        CATALOG(1, "目录"),
        MENU(2, "菜单"),
        BUTTON(3, "按钮"),
        ;

        private final Integer value;
        private final String name;

        Type(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        @Override
        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

}
