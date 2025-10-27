package com.wang.wangaicodegenerator.langgraph4j.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/22---17:20
 * @description: 图片分类枚举
 */

@Getter
public enum ImageCategoryEnum {
    /**
     * 内容图片
     */
    CONTENT("内容图片", "CONTENT"),
    /**
     * LOGO图片
     */
    LOGO("LOGO图片", "LOGO"),
    /**
     * 插画图片
     */
    ILLUSTRATION("插画图片", "ILLUSTRATION"),
    /**
     * 架构图片
     */
    ARCHITECTURE("架构图片", "ARCHITECTURE");


    private final String text;

    private final String value;

    ImageCategoryEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的value
     * @return 枚举值
     */
    public static ImageCategoryEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (ImageCategoryEnum anEnum : ImageCategoryEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}

