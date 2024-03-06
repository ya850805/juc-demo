package org.example.rwlock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author jason
 * @description
 * @create 2024/3/6 16:55
 *
 *  鎖降級：獲取寫鎖 -> 獲取讀鎖 -> 釋放寫鎖，   這個次序，寫鎖能降級成讀鎖
 *
 *  如果一個線程佔有了寫鎖，在不釋放寫鎖的情況下，他還能佔有讀鎖，即寫鎖降級為讀鎖
 *
 *  讀沒有完成時寫鎖無法獲取火，必須要等著讀鎖讀完後才有機會寫
 **/
public class LockDownGradingDemo {
    public static void main(String[] args) {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

        writeLock.lock();
        System.out.println("---寫入");

        /**
         * 執行業務邏輯...(在持有寫鎖中，也可以獲取讀鎖，就是鎖降級)(相反的話先持有讀鎖，則不能獲取讀鎖，鎖升級是不行的！)
         */
        readLock.lock();
        System.out.println("---讀取");

        writeLock.unlock();

        readLock.unlock();
    }
}
