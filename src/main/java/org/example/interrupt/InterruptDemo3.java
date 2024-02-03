package org.example.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * @author jason
 * @description
 * @create 2024/2/4 05:41
 *
 *  1. 中斷標示位，默認false
 *  2. t2 ----> t1發出了中斷協商，t2調用t1.interrupt()，中斷標示位=true
 *  3. 中斷標示位true，正常情況，程序停止
 *  4. 中斷標示位true，異常情況(線程處於sleep狀態)，會拋出InterruptedException，並"把中斷狀態清除"，中斷標示位=false
 *  5. 因為中斷標示位已被清除，所以在catch區塊需要再次給中斷標示位設置為true，2次調用程序才會停止
 *
 **/
public class InterruptDemo3 {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while(true) {
                if(Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + "\t 中斷標示位：" + Thread.currentThread().isInterrupted() + " 程序停止");
                    break;
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    /**
                     * 為什麼要在異常處，再調用一次？
                     *
                     * => 線程處於sleep狀態時，如果去interrupt()這個線程，則拋出InterruptedException，並"把中斷狀態清除"(中斷標示位=false)
                     *    因為中斷標示位已被清除，所以在catch區塊需要再次給中斷標示位設置為true，2次調用程序才會停止
                     */
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }

                System.out.println("-------hello InterruptDemo3");
            }
        }, "t1");
        t1.start();

        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            //設置t1的中斷標示位為true
            t1.interrupt();
        }, "t2").start();
    }
}
