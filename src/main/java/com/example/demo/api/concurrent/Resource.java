package com.example.demo.api.concurrent;

        import java.util.concurrent.locks.*;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/6/30
 */
public class Resource {

    /**
     * 资源数量
     */
    private Integer num = 0;

    /**
     * 操作次数
     */
    static Integer count = 0;

    Lock lock = new ReentrantLock();

    final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    final Lock readLock = readWriteLock.readLock();

    final Lock writeLock = readWriteLock.writeLock();

    public synchronized int getNum() {
        return num;
    }

    /**
     * synchronized修饰非静态的方法
     */
    public synchronized void setNum(int num) {
        this.num = num;
    }

    /**
     * synchronized修饰静态变量
     */
    public synchronized static void add() {
        count++;
    }

    public Integer getCount() {
        return count;
    }

    /**
     * ReentrantLock
     * lockInterruptibly
     */
    public void add1() {
        lock.lock();
        try {
            num += 1;
        } finally {
            lock.unlock();
        }
    }

    public int get() {
        readLock.lock();
        try {
            return num;
        } finally {
            readLock.unlock();
        }
    }

    public void put(int num) {
        writeLock.lock();
        try {
            this.num += num;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 锁的升级和降级
     */
    public int read2Write(int newNum) {
        readLock.lock();
        try {
            if (num > 0) {
                return num;
            }
            // ReadWriteLock不允许读锁升级到写锁
            // 在获取写锁之前必须释放读锁
            readLock.unlock();
            writeLock.lock();
            try {
                num = newNum;
                // 写锁降级为读锁是可以的
                // 必须在写锁释放之前降级
                readLock.lock();
                try {
                    return num;
                } finally {
                    readLock.unlock();
                }
            } finally {
                writeLock.unlock();
            }
        } finally {
            readLock.unlock();
        }
    }

    final StampedLock stampedLock = new StampedLock();

    /**
     * 写锁
     */
    public void write(int newValue) {
        long stamp = stampedLock.writeLock();
        try {
            num += newValue;
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    /**
     * 悲观读锁
     */
    public int readPessimistically() {
        long stamp = stampedLock.readLock();
        try {
            return num;
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }

    /**
     * 乐观读
     * 无锁设计
     */
    public int readOptimistically() {
        long stamp = stampedLock.tryOptimisticRead();
        // 在使用共享变量之前检查共享变量是否被更改
        if (!stampedLock.validate(stamp)) {
            // 如果使用之前发现共享变量被修改则升级到悲观读锁
            stamp = stampedLock.readLock();
            try {
                return num;
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        return num;
    }
}
