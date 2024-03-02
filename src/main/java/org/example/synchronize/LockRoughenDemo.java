package org.example.synchronize;

/**
 * @author jason
 * @description
 * @create 2024/3/2 09:28
 *
 *  鎖粗化
 *  假如方法中首尾相接，前後相鄰的都是同一個鎖對象，那JIT編譯器就會把這幾個synchronized合併成一大塊，
 *  加粗加大範圍，一次申請鎖使用即可，避免次次的申請和釋放鎖，提升了性能
 **/
public class LockRoughenDemo {
    static Object lock = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock) {
                System.out.println("11111");
            }
            synchronized (lock) {
                System.out.println("22222");
            }
            synchronized (lock) {
                System.out.println("33333");
            }
            synchronized (lock) {
                System.out.println("44444");
            }

            /*************************************/

            //底層JIT的鎖粗化優化
            synchronized (lock) {
                System.out.println("11111");
                System.out.println("22222");
                System.out.println("33333");
                System.out.println("44444");
            }
        }, "t1").start();
    }
}
