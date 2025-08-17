package com.wang.wangaicodegenerator.model.dto.chatHistory;

import lombok.Data;

import java.io.Serializable;

/**
 * 对话历史更新请求
 * @author Fugitive Mr.Wang
 */
@Data
public class ChatHistoryUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 消息类型(user/ai)
     */
    private String messageType;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 创建用户id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}