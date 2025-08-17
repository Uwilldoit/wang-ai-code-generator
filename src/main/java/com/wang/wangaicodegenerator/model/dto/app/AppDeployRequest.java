package com.wang.wangaicodegenerator.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/17---10:59
 * @description:
 */

@Data
public class AppDeployRequest implements Serializable {

    /**
     * 应用 id
     */
    private Long appId;

    private static final long serialVersionUID = 1L;
}

