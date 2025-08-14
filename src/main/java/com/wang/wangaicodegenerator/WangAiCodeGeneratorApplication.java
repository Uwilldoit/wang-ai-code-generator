package com.wang.wangaicodegenerator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@MapperScan("com.wang.wangaicodegenerator.mapper")
@SpringBootApplication
public class WangAiCodeGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(WangAiCodeGeneratorApplication.class, args);
    }

}
