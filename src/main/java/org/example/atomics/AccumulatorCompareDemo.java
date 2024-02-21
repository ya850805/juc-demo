package org.example.atomics;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author jason
 * @description
 * @create 2024/2/21 11:02
 *
 *  需求：50個線程，每個線程點讚300w次，輸出總點讚數
 **/
public class AccumulatorCompareDemo {
    public static final int _1W = 10000;
    public static final int _threadNumber = 50;

    public static void main(String[] args) throws InterruptedException {
        ClickNumber clickNumber = new ClickNumber();

        long startTime;
        long endTime;

        CountDownLatch countDownLatch1 = new CountDownLatch(_threadNumber);
        CountDownLatch countDownLatch2 = new CountDownLatch(_threadNumber);
        CountDownLatch countDownLatch3 = new CountDownLatch(_threadNumber);
        CountDownLatch countDownLatch4 = new CountDownLatch(_threadNumber);

        startTime = System.currentTimeMillis();
        for(int i = 1; i <= _threadNumber; i++) {
            new Thread(() -> {
                try {
                    for (int j = 1; j <= 300 * _1W; j++) {
                        clickNumber.clickBySynchronized();
                    }
                } finally {
                    countDownLatch1.countDown();
                }

            }, String.valueOf(i)).start();
        }
        countDownLatch1.await();
        endTime = System.currentTimeMillis();
        System.out.println("--- costTime: " + (endTime - startTime) + "毫秒" + "\t clickBySynchronized: " + clickNumber.number);

        /*******************************************************************/

        startTime = System.currentTimeMillis();
        for(int i = 1; i <= _threadNumber; i++) {
            new Thread(() -> {
                try {
                    for (int j = 1; j <= 300 * _1W; j++) {
                        clickNumber.clickByAtomicLong();
                    }
                } finally {
                    countDownLatch2.countDown();
                }

            }, String.valueOf(i)).start();
        }
        countDownLatch2.await();
        endTime = System.currentTimeMillis();
        System.out.println("--- costTime: " + (endTime - startTime) + "毫秒" + "\t clickByAtomicLong: " + clickNumber.atomicLong.get());

        /*******************************************************************/

        startTime = System.currentTimeMillis();
        for(int i = 1; i <= _threadNumber; i++) {
            new Thread(() -> {
                try {
                    for (int j = 1; j <= 300 * _1W; j++) {
                        clickNumber.clickByLongAdder();
                    }
                } finally {
                    countDownLatch3.countDown();
                }

            }, String.valueOf(i)).start();
        }
        countDownLatch3.await();
        endTime = System.currentTimeMillis();
        System.out.println("--- costTime: " + (endTime - startTime) + "毫秒" + "\t clickByLongAdder: " + clickNumber.longAdder.sum());

        /*******************************************************************/

        startTime = System.currentTimeMillis();
        for(int i = 1; i <= _threadNumber; i++) {
            new Thread(() -> {
                try {
                    for (int j = 1; j <= 300 * _1W; j++) {
                        clickNumber.clickByLongAccumulator();
                    }
                } finally {
                    countDownLatch4.countDown();
                }

            }, String.valueOf(i)).start();
        }
        countDownLatch4.await();
        endTime = System.currentTimeMillis();
        System.out.println("--- costTime: " + (endTime - startTime) + "毫秒" + "\t clickByLongAccumulator: " + clickNumber.longAccumulator.get());
    }
}

class ClickNumber {
    //1. synchronized
    int number = 0;
    public synchronized void clickBySynchronized() {
        number++;
    }

    //2. AtomicLong
    AtomicLong atomicLong = new AtomicLong(0);
    public void clickByAtomicLong() {
        atomicLong.getAndIncrement();
    }

    //3. LongAdder
    LongAdder longAdder = new LongAdder();
    public void clickByLongAdder() {
        longAdder.increment();
    }

    //4. LongAccumulator
    LongAccumulator longAccumulator = new LongAccumulator((x, y) -> x + y, 0);
    public void clickByLongAccumulator() {
        longAccumulator.accumulate(1);
    }
}
