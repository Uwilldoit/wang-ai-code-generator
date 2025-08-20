package com.wang.wangaicodegenerator.service;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/20---17:58
 * @description:
 */

public interface ProjectDownloadService {
    void downloadProjectAsZip(String projectPath, String downloadFileName, HttpServletResponse response);
}
