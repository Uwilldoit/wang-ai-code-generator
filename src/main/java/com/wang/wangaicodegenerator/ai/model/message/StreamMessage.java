package com.wang.wangaicodegenerator.ai.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/19---14:04
 * @description: 流式消息响应基类
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamMessage {
    private String type;
}

