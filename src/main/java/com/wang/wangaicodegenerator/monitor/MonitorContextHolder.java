package com.wang.wangaicodegenerator.monitor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/24---10:52
 * @description: 上下文持有者
 */

@Slf4j
public class MonitorContextHolder {

    private static final ThreadLocal<MonitorContext> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置监控上下文
     */
    public static void setContext(MonitorContext context) {
        CONTEXT_HOLDER.set(context);
    }

    /**
     * 获取当前监控上下文
     */
    public static MonitorContext getContext() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清除监控上下文
     */
    public static void clearContext() {
        CONTEXT_HOLDER.remove();
    }
}

