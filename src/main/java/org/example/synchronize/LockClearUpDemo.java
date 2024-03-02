package org.example.synchronize;

/**
 * @author jason
 * @description
 * @create 2024/3/2 09:21
 *
 *  鎖消除
 *  從JIT的角度看相當於無視他，synchronized (o) 不存在了，
 *  這個鎖對象並沒有被擴散到其他線程使用，
 *  極端的說就是根本沒有加這個鎖對象的底層機器碼，消除了鎖的使用
 **/
public class LockClearUpDemo {
    static Object lock = new Object();

    public void m1() {
//        synchronized (lock) {
//            System.out.println("---- hello LockClearUpDemo");
//        }

        //鎖消除問題，JIT編譯器會無視他，synchronized(o)，每次new出來的，不存在了，非正常的
        //這樣寫沒意義，因為每個線程都搶佔不同的鎖(new出來的o)，正常情況下應該要多個線程去搶佔同一把鎖(lock)
        Object o = new Object();
        synchronized (o) {
            System.out.println("---- hello LockClearUpDemo" + "\t" + o.hashCode() + "\t" + lock.hashCode());
        }
    }

    public static void main(String[] args) {
        LockClearUpDemo lockClearUpDemo = new LockClearUpDemo();
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                lockClearUpDemo.m1();
            }, String.valueOf(i)).start();
        }
    }
}
