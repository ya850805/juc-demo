package org.example.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author jason
 * @description
 * @create 2024/2/19 12:04
 *
 * 通過CAS操作完成自旋鎖，A線程先進來調用myLock方法自己持有鎖5秒鐘，B隨後進來後發現
 * 當前有線程持有鎖，所以只能通過自旋等待，直到A釋放鎖後B隨後搶到
 **/
public class SpinLockDemo {
    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    private void lock() {
        Thread thread = Thread.currentThread();

        System.out.println(Thread.currentThread().getName() + "\t --- come in");

        //期望目前是沒有線程，沒有線程才把他設置為當前線程
        while (!atomicReference.compareAndSet(null, thread)) {

        }
    }

    private void unlock() {
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread, null);
        System.out.println(Thread.currentThread().getName() + "\t --- task over, unlock...");
    }

    public static void main(String[] args) {
        SpinLockDemo spinLockDemo = new SpinLockDemo();

        new Thread(() -> {
            spinLockDemo.lock();
            try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
            spinLockDemo.unlock();
        }, "A").start();

        //暫停500ms，保證線程A先於B啟動
        try { TimeUnit.MILLISECONDS.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            spinLockDemo.lock();
            spinLockDemo.unlock();
        }, "B").start();
    }
}
