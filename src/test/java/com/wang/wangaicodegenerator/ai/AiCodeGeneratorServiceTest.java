package com.wang.wangaicodegenerator.ai;

import com.wang.wangaicodegenerator.ai.model.HtmlCodeResult;
import com.wang.wangaicodegenerator.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/15---01:29
 * @description:
 */

@SpringBootTest
class AiCodeGeneratorServiceTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generateHtmlCode() {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode("做个程序员在逃小王的工作记录小工具");
        Assertions.assertNotNull(result);
    }

    @Test
    void generateMultiFileCode() {
        MultiFileCodeResult multiFileCode = aiCodeGeneratorService.generateMultiFileCode("做个程序员在逃小王的留言板");
        Assertions.assertNotNull(multiFileCode);
    }
}

