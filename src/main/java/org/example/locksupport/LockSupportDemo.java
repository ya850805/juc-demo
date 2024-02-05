package org.example.locksupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
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
 *
 *  3. 使用LockSupport的park() & unpark() ===> 參考lockSupportParkUnpark()
 *      1. LockSupport類使用了一種名為Permit(許可)的概念來做到"阻塞和喚醒線程"的功能，每個線程都有一個許可(Permit)，但與Semaphore不同的是，"許可的累加上限是1"
 *      2. permit許可證默認沒有不能放行，所以一開始調用park()方法當前線程就會阻塞，直到別的線程給當前線程發放permit，park方法才會被喚醒
 *      3. 調用unpark(thread)方法後，就會將thread線程的許可證permit發放，會自動喚醒park線程，即之前阻塞中的LockSupport.park()方法會立即返回
 *      4. "不需要在鎖塊裡面使用"
 *      5. LockSupport支持先unpark()再park()，"先喚醒後等待"，就相當於是先給了permit(先發通行證)，之後被park()攔截時直接交出通行證
 *      6. 每個線程都有一個相關的permit，permit最多只有一個，"重複調用unpark()也不會積累許可"
 *
 *      Q1. 為什麼可以突破wait()/notify()的原有調用順序？
 *          A1. 因為unpark()獲得了一個憑證，之後再調用park()方法，就可以名正言順的憑證消費，故不會阻塞。先發放了憑證後續可以暢通無阻
 *      Q2. 為什麼喚醒兩次後阻塞兩次，但最終的結果線程還是會"阻塞"？
 *          A2. 因為"憑證的數量最多為1"，連續要用兩次unpark()和調用一次unpark()效果一樣，只會增加一個憑證。而調用兩次park()卻需要消費兩個憑證，permit不夠，故不能放行
 **/
public class LockSupportDemo {
    public static void main(String[] args) {
//        syncWaitNotify();
//        lockConditionAwaitSignal();

        lockSupportParkUnpark();
    }

    private static void lockSupportParkUnpark() {

        Thread t1 = new Thread(() -> {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

            System.out.println(Thread.currentThread().getName() + "\t --- come in");

            LockSupport.park(); //攔截
//            LockSupport.park(); //攔截
            System.out.println(Thread.currentThread().getName() + "\t --- 被喚醒");
        }, "t1");
        t1.start();

//        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            //多次調用也只會累積一個permit
            LockSupport.unpark(t1); //給t1線程發通行證，放行
            LockSupport.unpark(t1); //給t1線程發通行證，放行
            System.out.println(Thread.currentThread().getName() + "\t --- 發出通知");
        }, "t2").start();
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
