package org.example;

import java.util.concurrent.*;

/**
 * @author jason
 * @description
 * @create 2024/1/28 09:48
 **/
public class FutureThreadPoolDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        m1();

        //3個任務，開啟多個異步線程來處理，計算耗時

        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        long startTime = System.currentTimeMillis();

        //任務一
        FutureTask<String> futureTask1 = new FutureTask<>(() -> {
            TimeUnit.MILLISECONDS.sleep(500);
            return "task1 over";
        });
        threadPool.execute(futureTask1);

        //任務二
        FutureTask<String> futureTask2 = new FutureTask<>(() -> {
            TimeUnit.MILLISECONDS.sleep(300);
            return "task2 over";
        });
        threadPool.execute(futureTask2);

        //需要取得返回結果時(調用get())，會比不需要返回值(不調用get())還要耗時
        System.out.println(futureTask1.get());
        System.out.println(futureTask2.get());

        //任務三
        TimeUnit.MILLISECONDS.sleep(300);

        long endTime = System.currentTimeMillis();
        System.out.println("costTime: " + (endTime - startTime) + "毫秒");

        System.out.println(Thread.currentThread().getName() + "\t ---end");

        threadPool.shutdown();
    }


    private static void m1() throws InterruptedException {
        //3個任務，目前只有一個main線程來處理，計算耗時
        long startTime = System.currentTimeMillis();

        TimeUnit.MILLISECONDS.sleep(500);
        TimeUnit.MILLISECONDS.sleep(300);
        TimeUnit.MILLISECONDS.sleep(300);

        long endTime = System.currentTimeMillis();
        System.out.println("costTime: " + (endTime - startTime) + "毫秒");

        System.out.println(Thread.currentThread().getName() + "\t ---end");
    }
}
