package org.example.volatiles;

import java.util.concurrent.TimeUnit;

/**
 * @author jason
 * @description
 * @create 2024/2/16 17:21
 *
 *  flag:
 *      不加volatile，沒有可見性，程序無法停止
 *      加了volatile，保證可見性，程序可以停止
 *
 *  ===============================================
 *
 *  線程t1中為何看不到被主線程main修改為false的flag的值？
 *      問題可能：
 *          1. 主線程修改了flag之後沒有將其刷新到主內存，所以t1線程看不到
 *          2. 主線程將flag刷新到主內存，但是t1一直讀取的是自己工作內存中flag的值，沒有去主內存中更新獲取flag最新的值
 *      我們的訴求：
 *          1. 線程中修改了自己工作內存中的副本之後，立即將其刷新到主內存
 *          2. 工作內存中每次讀取共享變量時，都去主內存中重新讀取，然後拷貝到工作內存
 *      解決：
 *          使用volatile修飾共享變量，就可以達到上面的效果，被volatile修改的變量有以下特點：
 *              1. 線程中讀取的時候，每次讀取都會去主內存中讀取共享變量最新的值，然後將其複製到工作內存
 *              2. 線程中修改了工作內存中變量的副本，修改後會立即刷新到主內存
 **/
public class VolatileSeeDemo {
//    static boolean flag = true;
    static volatile boolean flag = true;

    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t ---come in");
            while (flag) {

            }
            //flag是false才打印
            System.out.println(Thread.currentThread().getName() + "\t ---flag被設置為false，程序停止");
        }, "t1").start();

        //暫停幾秒
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }

        flag = false;

        System.out.println(Thread.currentThread().getName() + "\t  修改完成，flag=" + flag);
    }
}
