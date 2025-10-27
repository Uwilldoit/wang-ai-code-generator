package com.wang.wangaicodegenerator.monitor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/24---10:52
 * @description: 监控上下文
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitorContext implements Serializable {

    private String userId;

    private String appId;

    @Serial
    private static final long serialVersionUID = 1L;
}

