package com.example.demo.api.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/6/29
 */
@Slf4j
@Controller
@RequestMapping("/concurrent")
public class ConcurrentController {

    public static void main(String[] args) {
        // 利用双重检查创建单例对象
        createSingletonObjectWithDoubleChecking();

        // synchronized的用法
        testSynchronized();

        log.info("Resource.count: " + Resource.count.hashCode());
        log.info("new Resource().getCount(): " + new Resource().getCount().hashCode());

        countDownLatch();

        cyclicBarrier();

        completableFuture();
    }

    static class MyThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "my-thread");
        }
    }

    private static void testSynchronized() {
        Resource resource = new Resource();

        // 操作次数
        int times = 10000;

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                2,
                30, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(2),
                new MyThreadFactory());

        Future<?> future1 = executor.submit(() -> {
            for (int i = 0; i < times; i++) {
                int r = resource.getNum();
                r += 1;
                resource.setNum(r);
                Resource.add();
            }
        });

        Future<?> future2 = executor.submit(() -> {
            for (int i = 0; i < times; i++) {
                int r = resource.getNum();
                r += 2;
                resource.setNum(r);
                Resource.add();
            }
        });

        try {
            // 使用get()等待线程结束
            future1.get();
            future2.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            log.info("resource = {}, count = {}", resource.getNum(), Resource.count);
            executor.shutdown();
        }

    }

    /**
     * 利用双重检查创建单例对象
     */
    public static void createSingletonObjectWithDoubleChecking() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                2,
                30, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(2),
                new MyThreadFactory());

        executor.execute(() -> {
            Singleton singleton = Singleton.getInstance();
            log.info("singleton hash code: " + singleton.hashCode());
        });

        executor.execute(() -> {
            Singleton singleton = Singleton.getInstance();
            log.info("singleton hash code: " + singleton.hashCode());
        });
    }

    /**
     * CountDownLatch
     */
    public static void countDownLatch() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                2,
                30, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(2),
                new MyThreadFactory());

        CountDownLatch countDownLatch = new CountDownLatch(2);

        executor.execute(() -> {
            log.info("Thread 1 finished its job");
            countDownLatch.countDown();
        });

        executor.execute(() -> {
            log.info("Thread 2 finished its job");
            countDownLatch.countDown();
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("Thread 1 and Thread 2 both finished their job");
    }

    /**
     * CyclicBarrier
     */
    public static void cyclicBarrier() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                2,
                30, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(2),
                new MyThreadFactory());

        AtomicInteger result1 = new AtomicInteger(0);
        AtomicInteger result2 = new AtomicInteger(0);

        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> {
            log.info("cyclicBarrier - Thread 1 and Thread 2 all finished their job, and result is {}", result1.get() + result2.get());
            executor.shutdown();
        });

        executor.execute(() -> {
            try {
                int times = 10000;
                for (int i = 0; i < times; i++) {
                    result1.getAndIncrement();
                }
                log.info("cyclicBarrier - thread 1 is finished");
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.execute(() -> {
            try {
                int times = 20000;
                for (int i = 0; i < times; i++) {
                    result2.getAndAdd(2);
                }
                log.info("cyclicBarrier - thread 2 is finished");
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * CompletableFuture
     * t1 -> t2 ------> t6 -> result
     *              |
     * t3 -> t4 -> t5
     */
    public static void completableFuture() {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            log.info("t1 is working...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("t1 is done");
            return "-result1";
        }).thenApply(result -> {
            log.info("t2 is working...");
            log.info("t2 get " + result);
            try {
                result += "-result2";
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("t2 is done");
            return result;
        });

        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            log.info("t3 is working...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("t3 is done");
            return "-result3";
        }).thenApply(result -> {
            log.info("t4 is working...");
            log.info("t4 get " + result);
            try {
                result += "-result4";
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("t4 is done");
            return result;
        }).thenApply(result -> {
            log.info("t5 is working...");
            log.info("t5 get " + result);
            try {
                result += "-result5";
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("t5 is done");
            return result;
        }).thenCombine(f1, (result2, result1) -> {
            log.info("t6 is working...");
            log.info("t6 get result1" + result1);
            log.info("t6 get result2" + result2);
            String result = "";
            try {
                result = result1 + result2 + "-result6";
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("t6 is done");
            return result;
        });

        try {
            log.info("final result = " + f2.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
