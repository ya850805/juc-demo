package org.example.locks;

/**
 * @author jason
 * @description
 * @create 2024/2/2 22:03
 *
 *  可重入鎖：
 *      指同一線程在外層方法獲取到鎖時，在進入該線程的內層方法時會"自動獲取鎖"(前提，鎖的是同一個對象)
 *      不會因為之前已經獲取過鎖還沒釋放而阻塞，可以一定程度避免死鎖
 *      => 在一個synchronized修飾的方法或代碼塊的內部調用本類的其他synchronized修飾的方法或代碼塊時，是永遠可以得到鎖的
 *
 **/
public class ReEntryLockDemo {
    public static void main(String[] args) {
//        reEntryM1();

        ReEntryLockDemo demo = new ReEntryLockDemo();

        new Thread(() -> {
            demo.m1();
        }, "t1").start();
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
