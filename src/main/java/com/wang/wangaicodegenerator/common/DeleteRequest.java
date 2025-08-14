package com.wang.wangaicodegenerator.common;

import lombok.Data;

import java.io.Serializable;

/**
 * creator: Shajia Wang
 * createTime:2025/1/21---15:48
 * description:
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}

