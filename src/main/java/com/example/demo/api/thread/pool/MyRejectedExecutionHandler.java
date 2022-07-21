package com.example.demo.api.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/6/26
 */
@Slf4j
public class MyRejectedExecutionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        log.warn("队列已满，拒绝请求");
    }

}
