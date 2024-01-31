package org.example.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author jason
 * @description
 * @create 2024/1/30 19:17
 *
 * 對計算結果進行處理
 * 1. thenApply()   計算結果存在依賴關係，這些線程"串行化"，由於存在依賴關係，當前步驟有異常的話就停止
 * 2. handle()  發生異常可以針對該異常進行處理，不會馬上中斷程序會繼續往下走
 **/
public class CompletableFutureAPI2Demo {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("第一步");
            return 1;
        }, threadPool).handle((f, e) -> {
            int i = 10 / 0;
            System.out.println("第二步");
            return f + 2;
        }).handle((f, e) -> {
            System.out.println("第三步");
            return f + 3;
        }).whenComplete((v, e) -> {
            if(null == e) {
                System.out.println("計算結果：" + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        });

        System.out.println(Thread.currentThread().getName() + "---主線程去忙其他任務");

        threadPool.shutdown();
    }
}
