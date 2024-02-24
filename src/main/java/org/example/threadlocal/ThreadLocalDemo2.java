package org.example.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jason
 * @description
 * @create 2024/2/24 08:01
 *
 *  使用完threadLocal需要remove避免多線程場景線程複用時內存泄露(在finally塊remove()回收)
 **/
public class ThreadLocalDemo2 {
    public static void main(String[] args) {
        MyData myData = new MyData();
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        try {
            for (int i = 0; i < 10; i++) {
                threadPool.submit(() -> {
                    try {
                        Integer beforeAdd = myData.threadLocal.get();
                        myData.add();
                        Integer afterAdd = myData.threadLocal.get();
                        System.out.println(Thread.currentThread().getName() + "\t beforeAdd: " + beforeAdd + "\t afterAdd: " + afterAdd);
                    } finally {
                        myData.threadLocal.remove();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}

class MyData {
    ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);

    public void add() {
        threadLocal.set(threadLocal.get() + 1);
    }
}
