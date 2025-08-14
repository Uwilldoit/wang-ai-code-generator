package com.wang.wangaicodegenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class WangAiCodeGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(WangAiCodeGeneratorApplication.class, args);
    }

}
