package org.example.rwlock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author jason
 * @description
 * @create 2024/3/6 16:18
 **/
public class ReentrantReadWriteLockDemo {
    public static void main(String[] args) {
        MyResource myResource = new MyResource();

        for (int i = 1; i <= 10; i++) {
            int finalI = i;
            new Thread(() -> {
                myResource.write(finalI + "", finalI + "");
            }, String.valueOf(i)).start();
        }

        for (int i = 1; i <= 10; i++) {
            int finalI = i;
            new Thread(() -> {
                myResource.read(finalI + "");
            }, String.valueOf(i)).start();
        }

        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

        //讀沒有完成的時候，寫無法獲得
        for (int i = 1; i <= 3; i++) {
            int finalI = i;
            new Thread(() -> {
                myResource.write(finalI + "", finalI + "");
            }, "新寫鎖線程 ===> " + i).start();
        }
    }
}

class MyResource {
    Map<String, String> map = new HashMap<>();

    //ReentrantLock等同於synchronized，排他鎖
    Lock lock = new ReentrantLock();

    //ReentrantReadWriteLock一體兩面，讀寫互斥，讀讀共享
    ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void write(String key, String value) {
//        lock.lock();
        rwLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t" + "正在寫入");
            map.put(key, value);
            try { TimeUnit.MILLISECONDS.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName() + "\t" + "完成寫入");
        } finally {
//            lock.unlock();
            rwLock.writeLock().unlock();
        }
    }

    public void read(String key) {
//        lock.lock();
        rwLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t" + "正在讀取");
            String result = map.get(key);
//            try { TimeUnit.MILLISECONDS.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }

            //暫停2000ms，讀鎖沒有完成前，寫鎖無法獲得
            try { TimeUnit.MILLISECONDS.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName() + "\t" + "完成讀取" + "\t" + result);
        } finally {
//            lock.unlock();
            rwLock.readLock().unlock();
        }
    }
}