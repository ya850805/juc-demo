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
