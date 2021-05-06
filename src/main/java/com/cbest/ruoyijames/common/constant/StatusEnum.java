package com.cbest.ruoyijames.common.constant;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum StatusEnum implements IEnum<Integer> {
    NORMAL(0, "正常"),
    DISABLED(1, "停用"),
    ;

    private final Integer value;
    private final String name;

    StatusEnum(Integer value, String name) {
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
