package org.example.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * @author jason
 * @description
 * @create 2024/2/4 05:29
 **/
public class InterruptDemo2 {
    public static void main(String[] args) {
        //實例方法interrupt()僅僅是設置線程的中斷狀態為true，不會停止線程
        Thread t1 = new Thread(() -> {
            for(int i = 1; i <= 300; i++) {
                System.out.println("----" + i);
            }

            System.out.println("t1線程調用interrupt()後的中斷標示02：" + Thread.currentThread().isInterrupted());
        }, "t1");
        t1.start();

        System.out.println("t1線程默認的中斷標示：" + t1.isInterrupted()); //false

        try { TimeUnit.MILLISECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }

        t1.interrupt(); //true
        System.out.println("t1線程調用interrupt()後的中斷標示01：" + t1.isInterrupted());

        try { TimeUnit.MILLISECONDS.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        /**
         * t1線程已經完成任務(結束)，去判斷中斷標示位就會是false！
         */
        System.out.println("t1線程調用interrupt()後的中斷標示03：" + t1.isInterrupted()); //false
    }
}
