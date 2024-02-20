package org.example.atomics;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author jason
 * @description
 * @create 2024/2/20 12:59
 *
 *  "以一種線程安全的方式操作非線程安全對象內的某個字段"
 *
 *  1. 更新的對象屬性必須使用public volatile修飾符
 *  2. 因為對象的屬性修改類型原子類都是抽象類，所以每次使用都必須使用靜態方法newUpdater()創建一個更新器，並且需要設置想要更新的類和屬性
 *
 *  ===
 *
 *  需求：
 *  10個線程，每個線程轉帳1000，不使用synchronized，嘗試使用AtomicIntegerFieldUpdater來實現
 **/
public class AtomicIntegerFieldUpdaterDemo {
    public static void main(String[] args) throws InterruptedException {
        BankAccount bankAccount = new BankAccount();

        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                try {
                    for (int j = 1; j <= 1000; j++) {
                        bankAccount.transferMoney();
                    }
                } finally {
                    countDownLatch.countDown();
                }
            }, String.valueOf(i)).start();
        }

        countDownLatch.await();

        System.out.println(Thread.currentThread().getName() + "\t" + "result: " + bankAccount.money);
    }
}

class BankAccount {
    String bankName = "CCB";

    public volatile int money = 0;

    AtomicIntegerFieldUpdater<BankAccount> fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(BankAccount.class, "money");

    //不加synchronized，保證高性能原子性(局部)
    public void transferMoney() {
        fieldUpdater.getAndIncrement(this);
    }
}
