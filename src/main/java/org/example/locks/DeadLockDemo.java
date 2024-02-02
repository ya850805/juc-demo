package org.example.locks;

import java.util.concurrent.TimeUnit;

/**
 * @author jason
 * @description
 * @create 2024/2/2 22:33
 *
 *  死鎖
 *      死鎖是指兩個或兩個以上的線程在執行過程中，因爭奪資源而造成的一種"互相等待的現象"，若無外力干涉那他們都將無法推進下去，
 *      如果系統資源充足，進程的資源請求都能夠得到滿足，死鎖出現的可能性就很低，否則就會因爭奪有限的資源而陷入死鎖
 *
 *      導致原因：
 *          1. 系統資源不足
 *          2. 進程運行推進順序不合適(下方的案例)
 *          3. 系統資源分配不當
 *******************************************************************************************************************
 *  排查死鎖
 *      方式一：純命令
 *          1. jps -l   => 得到一個進程編號
 *          2. jstack <進程編號> => 確定是造成了死鎖
 *      方式二：jconsole
 **/
public class DeadLockDemo {
    public static void main(String[] args) {
        final Object objectA = new Object();
        final Object objectB = new Object();

        new Thread(() -> {
            synchronized (objectA) {
                System.out.println(Thread.currentThread().getName() + "\t 自己持有A鎖，希望獲得B鎖");

                //暫停幾秒保證B線程開始了
                try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

                synchronized (objectB) {
                    System.out.println(Thread.currentThread().getName() + "\t 成功獲得B鎖");
                }
            }
        }, "A").start();

        new Thread(() -> {
            synchronized (objectB) {
                System.out.println(Thread.currentThread().getName() + "\t 自己持有B鎖，希望獲得A鎖");

                //暫停幾秒
                try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

                synchronized (objectA) {
                    System.out.println(Thread.currentThread().getName() + "\t 成功獲得A鎖");
                }
            }
        }, "B").start();
    }
}
