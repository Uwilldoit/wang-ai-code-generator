package com.wang.wangaicodegenerator.ai.model.message;

import lombok.Getter;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/19---14:07
 * @description: 流式消息类型枚举
 */
@Getter
public enum StreamMessageTypeEnum {

    /**
     * AI响应
     */
    AI_RESPONSE("ai_response", "AI响应"),
    /**
     * 工具请求
     */
    TOOL_REQUEST("tool_request", "工具请求"),
    /**
     * 工具执行结果
     */
    TOOL_EXECUTED("tool_executed", "工具执行结果");

    private final String value;
    private final String text;

    StreamMessageTypeEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 根据值获取枚举
     */
    public static StreamMessageTypeEnum getEnumByValue(String value) {
        for (StreamMessageTypeEnum typeEnum : values()) {
            if (typeEnum.getValue().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}

