package com.wang.wangaicodegenerator.constant;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/16---18:13
 * @description:
 */

public interface AppConstant {

    /**
     * 精选应用的优先级
     */
    Integer RECOMMEND_APP_PRIORITY = 99;

    /**
     * 默认应用优先级
     */
    Integer DEFAULT_APP_PRIORITY = 0;

    /**
     * 应用生成目录
     */
    String CODE_OUTPUT_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * 应用部署目录
     */
    String CODE_DEPLOY_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_deploy";

    /**
     * 应用部署域名
     */
    String CODE_DEPLOY_HOST = "http://localhost:1243";

}

