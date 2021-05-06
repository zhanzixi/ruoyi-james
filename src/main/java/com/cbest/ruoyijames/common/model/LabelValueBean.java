package com.cbest.ruoyijames.common.model;

import lombok.Data;

/**
 * @author James
 * @date 2021/4/21 16:11
 */
@Data
public class LabelValueBean<K, V> {
    private K label;
    private V value;

    public LabelValueBean(K label, V value) {
        this.label = label;
        this.value = value;
    }
}
