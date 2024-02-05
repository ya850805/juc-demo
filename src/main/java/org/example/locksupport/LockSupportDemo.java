package org.example.locksupport;

import java.util.concurrent.TimeUnit;

/**
 * @author jason
 * @description
 * @create 2024/2/5 10:20
 *
 *  1. 要使用wait()/notify()需要在同步代碼塊(synchronized)中才行 => 需要先持有鎖了才能使用
 *  2. wait()/notify() 需要成對使用，並且先使用wait()之後才能notify()
 **/
public class LockSupportDemo {
    public static void main(String[] args) {
        Object objectLock = new Object();

        new Thread(() -> {
//            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

            synchronized (objectLock) {
                System.out.println(Thread.currentThread().getName() + "\t --- come in");
                try {
                    objectLock.wait(); //交出鎖的控制權，讓別人去爭搶
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "\t --- 被喚醒");
            }
        }, "t1").start();

        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            synchronized (objectLock) {
                objectLock.notify();
                System.out.println(Thread.currentThread().getName() + "\t --- 發出通知");
            }
        }, "t2").start();
    }
}
