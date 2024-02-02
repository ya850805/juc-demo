package org.example.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jason
 * @description
 * @create 2024/2/2 22:03
 *
 *  可重入鎖：
 *      指同一線程在外層方法獲取到鎖時，在進入該線程的內層方法時會"自動獲取鎖"(前提，鎖的是同一個對象)
 *      不會因為之前已經獲取過鎖還沒釋放而阻塞，可以一定程度避免死鎖
 *      => 在一個synchronized修飾的方法或代碼塊的內部調用本類的其他synchronized修飾的方法或代碼塊時，是永遠可以得到鎖的
 *******************************************************************************************************************
 *  "每個鎖對象擁有一個鎖計數器和一個指向持有該鎖的線程的指針"
 *
 *  當執行monitorenter時，如果目標鎖對象的計數器為0，那麼說明他沒有被其他線程所持有，Java虛擬機會將該鎖對象的持有線程設置為當前線程，並將計數器+1
 *  在目標鎖對象的計數器不為0的情況下，如果鎖對象的持有線程是當前線程，那麼Java虛擬機可以將其計數器+1，否則需要等待，直至持有線程釋放該鎖
 *  當執行monitorexit時，Java虛擬機則須將鎖對象的計數器-1。計數器為0代表鎖已經被釋放
 *
 **/
public class ReEntryLockDemo {
    static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
//        reEntryM1();

//        ReEntryLockDemo demo = new ReEntryLockDemo();
//
//        new Thread(() -> {
//            demo.m1();
//        }, "t1").start();

        /**
         * 使用顯示的ReentrantLock，加鎖幾次就需要解鎖幾次
         */
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\t ---come in外層調用");
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "\t ---come in內層調用");
                } finally {
                    lock.unlock();
                }
            } finally {
                //如果這邊沒有釋放鎖，那加鎖的次數和釋放的次數就會不一樣，第二個線程始終無法獲取到鎖，導致一直等待
                lock.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\t ---come in外層調用");
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }

    public synchronized void m1() {
        System.out.println(Thread.currentThread().getName() + "\t ---come in");
        m2();
        System.out.println(Thread.currentThread().getName() + "\t ---end");
    }

    public synchronized void m2() {
        System.out.println(Thread.currentThread().getName() + "\t ---come in");
        m3();
    }

    public synchronized void m3() {
        System.out.println(Thread.currentThread().getName() + "\t ---come in");
    }

    private static void reEntryM1() {
        final Object object = new Object();
        new Thread(() -> {
            synchronized (object) {
                System.out.println(Thread.currentThread().getName() + "\t ---外層調用");
                synchronized (object) {
                    System.out.println(Thread.currentThread().getName() + "\t ---中層調用");
                    synchronized (object) {
                        System.out.println(Thread.currentThread().getName() + "\t ---內層調用");
                    }
                }
            }
        }, "t1").start();
    }
}
