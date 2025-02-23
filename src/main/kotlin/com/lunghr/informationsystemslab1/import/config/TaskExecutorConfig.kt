package com.lunghr.informationsystemslab1.import.config

import com.lunghr.informationsystemslab1.import.executors.ForkJoinTaskExecutor
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.ForkJoinPool;

@Configuration
class ThreadPoolConfig {
    @Bean
    fun forkJoinTaskExecutor(): TaskExecutor {
        val forkJoinPool = ForkJoinPool(16)
        return ForkJoinTaskExecutor(forkJoinPool)
    }

    @Bean
    fun forkJoinPool(): ForkJoinPool {
        val forkJoinPool = ForkJoinPool(16)
        return forkJoinPool
    }
}

