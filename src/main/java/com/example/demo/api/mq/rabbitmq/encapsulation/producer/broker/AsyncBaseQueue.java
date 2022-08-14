package com.example.demo.api.mq.rabbitmq.encapsulation.producer.broker;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步发送消息
 *
 * @author wudi.andy@outlook.com
 * @date 2022/8/10
 */
@Slf4j
public class AsyncBaseQueue {

    public static void submit(Runnable runnable) {
        SENDER_ASYNC.submit(runnable);
    }

    private final static int THREAD_SIZE = Runtime.getRuntime().availableProcessors();

    private final static int QUEUE_SIZE = 1000;

    private final static ArrayBlockingQueue<Runnable> WORK_QUEUE = new ArrayBlockingQueue<>(QUEUE_SIZE);

    private final static ThreadFactory THREAD_FACTOR = r -> new Thread(r, "message-sending-thread");

    private final static RejectedExecutionHandler REJECTED_EXECUTION_HANDLER = (r, executor) -> log.warn("Async sender is full, task is rejected, r = {}, executor = {}", r, executor);

    private final static ExecutorService SENDER_ASYNC = new ThreadPoolExecutor(THREAD_SIZE, THREAD_SIZE,
            60L, TimeUnit.SECONDS,
            WORK_QUEUE,
            THREAD_FACTOR,
            REJECTED_EXECUTION_HANDLER);
}
