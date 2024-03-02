package org.example.synchronize;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * @author jason
 * @description
 * @create 2024/2/28 11:48
 **/
public class SynchronizedUpDemo {
    public static void main(String[] args) {
//        noLock();
//        biasedLock();
//        lightLock();
//        heavyLock();

//        lockWithHashCode01();
        lockWithHashCode02();
    }

    /**
     * 偏向鎖過程中遇到identity hash code計算請求，立馬撤銷偏向模式，膨脹為重量級鎖
     */
    private static void lockWithHashCode02() {
        try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
        Object o = new Object();
        synchronized (o) {
            o.hashCode(); //
            System.out.println("偏向鎖過程中遇到identity hash code計算請求，立馬撤銷偏向模式，膨脹為重量級鎖");
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }

    /**
     *  當一個對象已經計算過identity hash code，他就無法進入偏向鎖狀態，跳過偏向鎖，直接升級輕量級鎖
     */
    private static void lockWithHashCode01() {
        //先休眠5s，保證開啟偏向鎖
        try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
        Object o = new Object();
        System.out.println("本應是偏向鎖");
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        o.hashCode(); // 當一個對象已經計算過identity hash code，他就無法進入偏向鎖狀態

        synchronized (o) {
            System.out.println("本應是偏向鎖，但由於計算過identity hash code，會直接升級為輕量級鎖");
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }

    private static void heavyLock() {
        Object o = new Object();
        new Thread(() -> {
            synchronized (o) {
                System.out.println(ClassLayout.parseInstance(o).toPrintable());
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (o) {
                System.out.println(ClassLayout.parseInstance(o).toPrintable());
            }
        }, "t2").start();
    }

    private static void lightLock() {
        //關閉偏向鎖    -XX:-UseBiasedLocking  就會直接使用輕量級鎖
        Object o = new Object();
        new Thread(() -> {
            synchronized (o) {
                System.out.println(ClassLayout.parseInstance(o).toPrintable());
            }
        }, "t1").start();
    }

    private static void biasedLock() {
        /**
         * Edit configuration -> Add VM options
         *
         * 設置沒有延時  -XX:BiasedLockingStartupDelay=0 => Java8默認是4000ms
         * 開啟偏向鎖    -XX:+UseBiasedLocking   => Java15後默認關閉(默認不啟用偏向鎖)
         */

        //也可以不加參數，暫停5秒讓偏向鎖生效
//        try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }

        Object o = new Object();
        synchronized (o) {
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }

    private static void noLock() {
        Object o = new Object();

        //如果有調用hashCode()方法，才會去紀錄Hash編碼
        System.out.println("10進制：" + o.hashCode());
        System.out.println("16進制：" + Integer.toHexString(o.hashCode()));
        System.out.println("2進制：" + Integer.toBinaryString(o.hashCode()));

        System.out.println(ClassLayout.parseInstance(o).toPrintable());
    }
}
