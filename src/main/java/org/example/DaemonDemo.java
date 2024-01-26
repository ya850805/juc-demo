package org.example;

import java.util.concurrent.TimeUnit;

/**
 * 如果用戶線程全部結束意味著程序需要完成的業務操作已經結束了，守護線程隨著JVM一同結束工作
 * setDaemon(true)方法必須在start()之前設置，否則報IllegalThreadStateException異常
 *
 * 1. 若t1是"用戶線程"(daemon=false)，即便3秒過後main()線程完成終止了，但這段代碼還是不會停止，因為身為用戶線程的t1的任務還沒完成
 * 2. 若t1是"守護線程"(daemon=true)，3秒過後main()線程完成終止了，整段代碼也停止了，因為已經沒有用戶線程在運行，身為守護線程的t1也會自己終止
 *
 * @author jason
 * @description
 * @create 2024/1/26 19:06
 **/
public class DaemonDemo {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 開始運行，" + (Thread.currentThread().isDaemon() ? "守護線程" : "用戶線程"));
            while (true) {

            }
        }, "t1");

        //設定是否為守護線程
        t1.setDaemon(true);
        t1.start();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + "\t ---end 主線程");
    }
}
