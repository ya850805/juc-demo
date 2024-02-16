package org.example.volatiles;

import java.util.concurrent.TimeUnit;

/**
 * @author jason
 * @description
 * @create 2024/2/16 17:41
 *
 *  "volatile變量的複合操作不具有原子性，比如number++"
 *
 *  對於volatile變量具備可見性，JVM只是保證從主內存加載到線程工作內存的值是最新的，也"僅是數據加載時是最新的"。
 *  但是多線程環境下，"數據計算"和"數據賦值"操作可能出現多次，若數據在加載之後，主內存volatile修飾變量發生修改之後，
 *  現工作內存中的操作將會作廢去讀主內存最新值，操作出現寫丟失問題。即"各線程私有內存和主內存公共內存中變量不同步"，進而導致數據不一致。
 *  由此可見volatile解決的是變量讀取時的可見性問題，但"無法保證原子性，對於多線程修改主內存共享變量的場景必須使用加鎖同步"。
 *
 *
 *  為什麼不具備原子性？
 *      舉i++的例子，在文件中，i++分為3步驟(數據加載/數據計算/數據賦值)，間隙期間不同步非原子操作(如果第二個線程在第一個線程讀取舊值與寫回新值期間讀取共享變量的值，那麼第二個線程會將與第一個線程一起看到同一個值)
 *      對於volatile變量，JVM只是保證從主內存加載到線程工作內存的值是最新的，也就是數據加載時是最新的。
 *      如果第二個線程在第一個線程讀取舊值和寫回新值期間讀取i的值，也就造成了線程安全的問題
 *
 **/
public class VolatileNoAtomicDemo {
    public static void main(String[] args) {
        MyNumber myNumber = new MyNumber();

        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 1000; j++) {
                    myNumber.increment();
                }
            }, String.valueOf(i)).start();
        }

        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println(myNumber.number);
    }
}

class MyNumber {
    volatile int number;

    public void increment() {
        number++;
    }
}