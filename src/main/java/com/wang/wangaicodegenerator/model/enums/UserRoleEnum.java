package com.wang.wangaicodegenerator.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

/**
 * @author: Shajia Wang
 * @createTime: 2025/8/15---00:22
 * @description:
 */
@Getter
public enum UserRoleEnum {

    /**
     * 用户
     */
    USER("用户", "user"),
    /**
     * 管理员
     */
    ADMIN("管理员", "admin");

    private final String text;

    private final String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的value
     * @return 枚举值
     */
    public static UserRoleEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (UserRoleEnum anEnum : UserRoleEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}

