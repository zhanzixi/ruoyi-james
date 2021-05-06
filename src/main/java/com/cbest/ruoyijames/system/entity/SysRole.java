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
import java.util.List;

/**
 * <p>
 * 角色信息表
 * </p>
 *
 * @author James
 * @since 2021-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysRole对象", description = "角色信息表")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotEmpty
    @ApiModelProperty(value = "角色名称")
    private String name;

    @NotEmpty
    @ApiModelProperty(value = "角色权限字符串")
    private String code;

    @ApiModelProperty(value = "显示顺序")
    private Integer sort;

    @ApiModelProperty(value = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）")
    private DataScopeEnum dataScope;

    @ApiModelProperty(value = "角色状态（0正常 1停用）")
    private StatusEnum status;

    @TableLogic
    @TableField("is_deleted")
    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    private Integer deleted;

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

    @TableField(exist = false)
    private List<Integer> menuIdList;

    @TableField(exist = false)
    private List<Integer> deptIdList;

    public enum DataScopeEnum implements IEnum<Integer> {
        ALL(1, "全部数据权限"),
        CUSTOM(2, "自定数据权限"),
        DEPT(3, "本部门数据权限"),
        DEPT_AND_BELOW(4, "本部门及以下数据权限"),
        SELF(5, "仅本人数据权限"),
        ;

        private final Integer value;
        private final String name;

        DataScopeEnum(Integer value, String name) {
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
