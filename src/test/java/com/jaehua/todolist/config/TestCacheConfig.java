package com.jaehua.todolist.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;

@TestConfiguration
@EnableAutoConfiguration(exclude = {CacheAutoConfiguration.class})  // 禁用缓存自动配置
public class TestCacheConfig {
    
    @Bean
    @Primary
    @ConditionalOnMissingBean  // 只有在没有其他 CacheManager 时才创建
    public CacheManager cacheManager() {
        return new NoOpCacheManager();  // 使用不执行任何缓存操作的 CacheManager
    }
} 