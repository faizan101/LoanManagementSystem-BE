package com.dtone.lending.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Configuration
@EnableConfigurationProperties(ConfigProperties.class)
public class ThreadPoolExecutorConfig {

    @Bean("AsyncThreadPool")
    public TaskExecutor getPushAsyncExecutor(ConfigProperties configProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); //TODO Pick from properties file
        executor.setMaxPoolSize(2 * 10);//TODO Pick from properties file
        executor.setThreadNamePrefix("Loan-Thread-");
        executor.setQueueCapacity(3 * 10);//TODO Pick from properties file
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}