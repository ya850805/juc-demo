package org.example.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jason
 * @description
 * @create 2024/3/4 14:50
 **/
public class AQSDemo {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock(); //非公平鎖

        /**
         * A B C 三個顧客，去銀行辦理業務，A先到，此時窗口空無一人，他優先獲得辦理窗口的機會辦理業務
         */
        //A耗時嚴重，估計長期佔有窗口
        new Thread(() -> {
            reentrantLock.lock();

            try {
                System.out.println("--- come in A");
                //模擬耗時操作
                try { TimeUnit.SECONDS.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
            } finally {
                reentrantLock.unlock();
            }
        }, "A").start();

        //B是第二個顧客，B一看到受理窗口被A佔用，只能去候客區等待，進入AQS隊列，等待A辦理完成，嘗試去搶佔受理窗口
        new Thread(() -> {
            reentrantLock.lock();

            try {
                System.out.println("--- come in B");
            } finally {
                reentrantLock.unlock();
            }
        }, "B").start();

        //C是第二個顧客，C一看到受理窗口被A佔用，只能去候客區等待，進入AQS隊列，等待A辦理完成，嘗試去搶佔受理窗口，前面是B顧客，FIFO
        new Thread(() -> {
            reentrantLock.lock();

            try {
                System.out.println("--- come in C");
            } finally {
                reentrantLock.unlock();
            }
        }, "C").start();
    }
}
