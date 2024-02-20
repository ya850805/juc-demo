package org.example.atomics;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author jason
 * @description
 * @create 2024/2/20 13:12
 *
 *  需求：
 *  多線程並發調用一個類的初始化方法，如果未被初始化過，將執行初始化工作
 *  要求只能被初始化一次，只有一個線程操作成功
 **/
public class AtomicReferenceFieldUpdaterDemo {
    public static void main(String[] args) {
        MyVar myVar = new MyVar();

        for (int i = 1; i <= 5; i++) {
            new Thread(() -> {
                myVar.init();
            }, String.valueOf(i)).start();
        }
    }
}

class MyVar {
    public volatile Boolean isInit = Boolean.FALSE;

    AtomicReferenceFieldUpdater<MyVar, Boolean> fieldUpdater = AtomicReferenceFieldUpdater.newUpdater(MyVar.class, Boolean.class, "isInit");

    public void init() {
        if (fieldUpdater.compareAndSet(this, Boolean.FALSE, Boolean.TRUE)) {
            System.out.println(Thread.currentThread().getName() + "\t" + "---- start init, need 3 seconds");
            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName() + "\t" + "---- init done");
        } else {
            System.out.println(Thread.currentThread().getName() + "\t" + "---- 已經有線程在進行初始化工作...");
        }
    }
}


