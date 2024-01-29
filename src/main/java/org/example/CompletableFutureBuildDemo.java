package org.example;

import java.util.concurrent.*;

/**
 * @author jason
 * @description
 * @create 2024/1/29 17:35
 *
 * 使用 CompletableFuture 4個靜態方法來創建異步任務
 *  runAsync(Runnable runnable)
 *  runAsync(Runnable runnable, Executor executor)
 *  supplyAsync(Supplier<U> supplier)
 *  supplyAsync(Supplier<U> supplier, Executor executor)
 *
 *  1. run系列"無返回值"
 *  2. supply系列"有返回值"
 *  3. 沒有指定Executor的方法，直接默認使用ForkJoinPool.commonPool()作為他的線程池執行異步代碼
 *  4. 如果指定線程池，則使用我們自定義的或者特別指定的線程池執行異步代碼
 *  5. 通常使用上述4個靜態方法，不使用new創建CompletableFuture
 **/
public class CompletableFutureBuildDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

//        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
//            System.out.println(Thread.currentThread().getName());
//            try {
//                //模擬執行一秒鐘
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, threadPool);
//
//        System.out.println(completableFuture.get());

        /***************************************************************************************************/

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                //模擬執行一秒鐘
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello supplyAsync";
        }, threadPool);

        System.out.println(completableFuture.get());

        threadPool.shutdown();
    }
}