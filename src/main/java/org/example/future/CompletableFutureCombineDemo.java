package org.example.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author jason
 * @description
 * @create 2024/1/31 16:08
 *
 * 兩個CompletionStage任務都完成後，最終能把兩個任務的結果一起交給thenCombine處理
 * 先完成的先等著，等待其他分支任務
 **/
public class CompletableFutureCombineDemo {
    public static void main(String[] args) {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t --- 啟動");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t --- 啟動");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 20;
        });

        CompletableFuture<Integer> result = completableFuture1.thenCombine(completableFuture2, (x, y) -> {
            System.out.println("---開始兩個結果合併");
            return x + y;
        });

        System.out.println(result.join());
    }
}
