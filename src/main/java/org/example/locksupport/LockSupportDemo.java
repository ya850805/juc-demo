package org.example.locksupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jason
 * @description
 * @create 2024/2/5 10:20
 *
 *  1. 使用synchronized - wait() - notify() ===> 參考syncWaitNotify()
 *      1. 要使用wait()/notify()需要在同步代碼塊(synchronized)中才行，否則報IllegalMonitorStateException => 需要先持有鎖了才能使用
 *      2. wait()/notify() 需要成對使用，並且先使用wait()之後才能notify()
 *  2. 使用Lock Condition - await() - signal() ===> 參考lockConditionAwaitSignal()
 *      1. Condition中的線程等待和喚醒方法，需要先獲取鎖
 *      2. 一定要先await()後signal()，不能反了
 *
 *  上述兩種方式，線程需要獲得並持有鎖，必須在鎖區塊(synchronized或lock)中，
 *  必須要先等待後喚醒，線程才能正常被喚醒
 ***********************************************************************************************************************************
 **/
public class LockSupportDemo {
    public static void main(String[] args) {
//        syncWaitNotify();
//        lockConditionAwaitSignal();
    }

    private static void lockConditionAwaitSignal() {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
//            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\t --- come in");
                condition.await();
                System.out.println(Thread.currentThread().getName() + "\t --- 被喚醒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            lock.lock();
            try {
                condition.signal();
                System.out.println(Thread.currentThread().getName() + "\t --- 發出通知");
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }

    private static void syncWaitNotify() {
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
