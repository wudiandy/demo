package com.example.demo.api.thread.pool;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/6/26
 */
@Service
@EnableAsync
public class ThreadPoolService {

    @Async("myAsync")
    public void getHotWater() {
        try {
            System.out.println(Thread.currentThread().getName() + " - 开始烧水：" + new Date());
            TimeUnit.SECONDS.sleep(5);
            System.out.println(Thread.currentThread().getName() + " - 完成烧水" + new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
