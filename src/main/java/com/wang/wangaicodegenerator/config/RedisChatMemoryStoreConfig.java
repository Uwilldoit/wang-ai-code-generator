package com.wang.wangaicodegenerator.config;

import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Fugitive Mr.Wang
 * @createTime: 2025/8/17---20:40
 * @description:
 */

@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
@Data
public class RedisChatMemoryStoreConfig {

    private String host;

    private int port;

    private String password;

    private long ttl;

    @Bean
    public RedisChatMemoryStore redisChatMemoryStore() {
        return RedisChatMemoryStore.builder()
                //todo 如果redis密码不为空还需要填写用户名
//                .user("你的用户名")
                .host(host)
                .port(port)
                .password(password)
                .ttl(ttl)
                .build();
    }
}

