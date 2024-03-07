package org.example.rwlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * @author jason
 * @description
 * @create 2024/3/7 16:31
 *
 *  StampedLock = ReentrantReadWriteLock + 讀的過程也允許獲取寫鎖介入
 **/
public class StampedLockDemo {
    static int number = 37;
    static StampedLock stampedLock = new StampedLock();

    public static void main(String[] args) {
//        pessimisticReadWrite();

        //樂觀讀寫
        StampedLockDemo resource = new StampedLockDemo();
        new Thread(() -> {
            resource.optimisticRead();
        }, "readThread").start();

        //暫停2秒後，讀過程中寫可以介入
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }

//        //暫停6秒後(寫線程沒有介入)
//        try { TimeUnit.SECONDS.sleep(6); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---- come in");
            resource.write();
        }, "writeThread").start();
    }


    //悲觀讀寫
    private static void pessimisticReadWrite() {
        StampedLockDemo resource = new StampedLockDemo();
        new Thread(() -> {
            resource.read();
        }, "readThread").start();

        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "--- come in");
            resource.write();
        }, "writeThread").start();

        try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println(Thread.currentThread().getName() + "\t" + "number: " + number);
    }

    public void write() {
        long stamp = stampedLock.writeLock(); //返回一個流水戳記，解鎖時要使用
        System.out.println(Thread.currentThread().getName() + "\t" + "寫線程準備修改");
        try {
            number = number + 13;
        } finally {
            stampedLock.unlockWrite(stamp);
        }
        System.out.println(Thread.currentThread().getName() + "\t" + "寫線程修改完成");
    }

    //悲觀讀，讀沒有完成時，寫鎖無法獲得
    public void read() {
        long stamp = stampedLock.readLock();
        System.out.println(Thread.currentThread().getName() + "\t" + "come in read lock code block, 4 seconds continue...");
        for (int i = 0; i < 4; i++) {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName() + "\t" + "正在讀取中...");
        }

        try {
            int result = number;
            System.out.println(Thread.currentThread().getName() + "\t" + "獲得成員變量 result: " + result);
            System.out.println("寫線程沒有修改成功，讀鎖時寫鎖無法介入，傳統的讀寫互斥");
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }

    //樂觀讀，讀的過程中也允許寫鎖介入
    public void optimisticRead() {
        long stamp = stampedLock.tryOptimisticRead();
        int result = number;
        //故意間隔4秒鐘，很樂觀認為讀取中沒有其他線程修改過number值，具體靠判斷
        System.out.println("4秒前stampedLock.validate方法值(true無修改，false有修改)" + "\t" + stampedLock.validate(stamp));

        for (int i = 0; i < 4; i++) {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName() + "\t" + "正在讀取中..." + i + "秒後" +
                    "stampedLock.validate方法值(true無修改，false有修改)" + "\t" + stampedLock.validate(stamp));
        }

        //有人修改過
        if(!stampedLock.validate(stamp)) {
            System.out.println("有人修改過-----有寫操作！！！");

            //有人修改過，改成悲觀讀，重新讀一次
            stamp = stampedLock.readLock();
            try {
                System.out.println("從樂觀讀升級為悲觀讀");
                result = number;
                System.out.println("重新悲觀讀後 result: " + result);
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }

        System.out.println(Thread.currentThread().getName() + "\t" + "finally value: " + result);
    }
}
