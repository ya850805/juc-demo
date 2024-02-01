package org.example.locks;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jason
 * @description
 * @create 2024/2/1 18:55
 *
 *  公平鎖：
 *      是指多個線程按照申請鎖的順序來獲取鎖，這裡類似排隊買票，先來的人先買後來的人在後面排著，這是公平的
 *      Lock lock = new ReentrantLock(true); //true 表示公平鎖，先來先得
 *  非公平鎖：
 *      是指多個線程獲取鎖的順序並不是按照申請鎖的順序了，有可能後申請的線程比先申請的線程優先獲取鎖，在高并發環境下，有可能造成優先級反轉或是飢餓狀態(某個線程一值得不到鎖)
 *      Lock lock = new ReentrantLock(false); //false 表示非公平鎖，後來的也可能先獲得鎖
 *      Lock lock = new ReentrantLock(); //默認非公平鎖
 *
 *  為什麼默認是非公平鎖？
 *      1. 恢復掛起的線程到真正鎖的獲取還是有時間差的，從開發人員來看看個時間微乎其微，但是從CPU的角度來看，這個時間差存在是明顯的。
 *          所以非公平鎖能更充分的利用CPU時間片，盡量減少CPU空閒狀態時間
 *      2. 使用多線程很重要的考慮點是線程切換的開銷，當採用非公平鎖時，"當一個線程請求鎖獲取同步狀態，然後釋放同步狀態，
 *          所以剛釋放鎖的線程在此刻再次獲取同步狀態的機率就變得很大，所以就減少了線程的開銷"
 *
 *  什麼時候使用公平鎖？什麼時候使用非公平鎖？
 *      => 如果為了更高的吞吐量，很顯然非公平鎖是比較合適的，因為節省很多線程切換時間，吞吐量自然就上去了，否則就使用公平鎖，大家公平使用，每個線程都有消費到
 **/
public class SaleTicketDemo {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        new Thread(() -> {
            for (int i = 0; i < 55; i++) ticket.sale();
        }, "a").start();

        new Thread(() -> {
            for (int i = 0; i < 55; i++) ticket.sale();
        }, "b").start();

        new Thread(() -> {
            for (int i = 0; i < 55; i++) ticket.sale();
        }, "c").start();
    }
}

//資源類，模擬3個售票員賣完50張票
class Ticket {
    private int number = 50;
    ReentrantLock lock = new ReentrantLock();

    public void sale() {
        lock.lock();
        try {
            if(number > 0) {
                System.out.println(Thread.currentThread().getName() + "賣出第 " + (number--) + " \t 還剩下" + number);
            }
        } finally {
            lock.unlock();
        }
    }
}
