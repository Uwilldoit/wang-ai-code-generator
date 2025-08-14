package com.wang.wangaicodegenerator.controller;

import com.wang.wangaicodegenerator.common.BaseResponse;
import com.wang.wangaicodegenerator.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Shajia Wang
 * @createTime: 2025/8/14---22:48
 * @description:
 */

@RestController
@RequestMapping("/health")
@Slf4j
public class HealthController {
    @GetMapping
    public BaseResponse<String> health(){
        return ResultUtils.success("ok" );
    }
}
