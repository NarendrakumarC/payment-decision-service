package com.core.payment_decision_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean
    public Executor taskExecutor() {
        return java.util.concurrent.Executors.newFixedThreadPool(3);
    }
}
