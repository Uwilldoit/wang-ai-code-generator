package com.wang.wangaicodegenerator.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/15---01:56
 * @description:
 */

@Getter
public enum CodeGenTypeEnum {

    /**
     * 原生 HTML 模式
     */
    HTML("原生 HTML 模式", "html"),
    /**
     * 原生多文件模式
     */
    MULTI_FILE("原生多文件模式", "multi_file"),
    /**
     * Vue 工程模式
     */
    VUE_PROJECT("Vue 工程模式", "vue_project");

    private final String text;
    private final String value;

    CodeGenTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的value
     * @return 枚举值
     */
    public static CodeGenTypeEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (CodeGenTypeEnum anEnum : CodeGenTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}

