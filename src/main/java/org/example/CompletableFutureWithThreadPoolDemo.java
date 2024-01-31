package org.example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author jason
 * @description
 * @create 2024/1/31 15:42
 *
 * 1. 如果不傳入線程池，使用默認的ForkJoinPool
 * 2. 傳入一個自定義線程池：
 *      如果執行第一個任務時，傳入一個"自定義線程池"
 *          調用thenRun()方法執行第二個任務時，第二個任務和第一個任務是共用一個線程池
 *          調用thenRunAsync()方法執行第二個任務時，第一個任務是自己傳入的線程池，第二個任務用的是ForkJoinPool
 * 3. 有可能處理得太快，系統優化切換原則，後面的任務直接使用main線程處理
 **/
public class CompletableFutureWithThreadPoolDemo {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);

        try {
            CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
                try { TimeUnit.MILLISECONDS.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println("1號任務 \t" + Thread.currentThread().getName());
                return "abcd";
            }, threadPool).thenRun(() -> {
                try { TimeUnit.MILLISECONDS.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println("2號任務 \t" + Thread.currentThread().getName());
            }).thenRun(() -> {
                try { TimeUnit.MILLISECONDS.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println("3號任務 \t" + Thread.currentThread().getName());
            }).thenRun(() -> {
                try { TimeUnit.MILLISECONDS.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println("4號任務 \t" + Thread.currentThread().getName());
            });

            System.out.println(completableFuture.get(2L, TimeUnit.SECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
