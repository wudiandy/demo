package com.example.demo.api.concurrent;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/6/29
 */
public class Singleton {
    static volatile Singleton instance;
    static Singleton getInstance() {
        // 这里检查一次
        if (instance == null) {
            synchronized (Singleton.class) {
                // 当线程获得锁后再检查一次
                if (instance == null) {
                    // 问题出现再new上
                    // 编译器优化后的指令顺序是这样的：
                    // 1 - 分配一块内存M
                    // 2 - 将M的地址赋值给instance
                    // 3 - 在M上初始化Singleton对象
                    // 如果在2完成后切换线程，那么，后执行的线程就会拿到一个未被初始化instance变量。
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
