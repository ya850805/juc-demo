package org.example.threadlocal;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author jason
 * @description
 * @create 2024/2/24 07:39
 *
 *  需求1: 5個業務賣房子，集團高層只關心銷售"總量"的準確統計數
 *  需求2: 各憑業務本事提呈，按照出單數"各自"統計("獨立的銷售額度")
 **/
public class ThreadLocalDemo {
    public static void main(String[] args) {
        House house = new House();

        for (int i = 1; i <= 5; i++) {
            new Thread(() -> {
                int size = new Random().nextInt(5) + 1;
//                System.out.println(size);
                for(int j = 1; j <= size; j++) {
                    house.saleHouse();
                    house.saleVolumeByThreadLocal();
                }
                System.out.println(Thread.currentThread().getName() + "\t" + "號銷售賣出：" + house.saleVolume.get());
            }, String.valueOf(i)).start();
        }

        try { TimeUnit.MILLISECONDS.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println(Thread.currentThread().getName() + "\t 共計賣出多少套：" + house.saleCount);
    }
}

class House {
    int saleCount;

    public synchronized void saleHouse() {
        saleCount++;
    }

//    ThreadLocal<Integer> saleVolume = new ThreadLocal<>() {
//        @Override
//        protected Object initialValue() {
//            return 0;
//        }
//    };

    ThreadLocal<Integer> saleVolume = ThreadLocal.withInitial(() -> 0);

    public void saleVolumeByThreadLocal() {
        saleVolume.set(saleVolume.get() + 1);
    }
}
