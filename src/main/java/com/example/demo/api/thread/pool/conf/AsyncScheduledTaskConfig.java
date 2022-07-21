package com.example.demo.api.thread.pool.conf;

import com.example.demo.api.thread.pool.MyRejectedExecutionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/6/26
 */
@Configuration
public class AsyncScheduledTaskConfig {

    @Value("${spring.task.execution.pool.core-size}")
    private int corePoolSize;

    @Value("${spring.task.execution.pool.max-size}")
    private int maxSize;

    @Value("${spring.task.execution.pool.queue-capacity}")
    private int queueCapacity;

    @Value("${spring.task.execution.pool.keep-alive}")
    private int keepAlive;

    @Bean
    public Executor myAsync() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("my-async");
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAlive);
        // 拒绝策略
        // {@code ThreadPoolExecutor.AbortPolicy()}直接抛出异常
        // {@code ThreadPoolExecutor.CallerRunsPolicy()}交由调用方线程运行，例如主线程
        // {@code ThreadPoolExecutor.DiscardPolicy()}直接丢弃
        // {@code ThreadPoolExecutor.DiscardOldestPolicy()}丢弃队列中最老的任务
        executor.setRejectedExecutionHandler(new MyRejectedExecutionHandler());
        executor.initialize();
        return executor;
    }

}
