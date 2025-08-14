package com.wang.wangaicodegenerator.common;

import lombok.Data;

/**
 * creator: Shajia Wang
 * createTime:2025/1/21---15:48
 * description:
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private int current = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认降序）
     */
    private String sortOrder = "descend";
}

