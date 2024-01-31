package org.example.future;

import java.util.concurrent.CompletableFuture;

/**
 * @author jason
 * @description
 * @create 2024/1/30 19:29
 *
 * 消費處理結果
 *  thenAccept(Consumer<? super T> action) 接收一個Consumer，不會有返回值(thenApply是接收一個Function，是有返回值的)
 *
 *
 *  比較結論：
 *  1. thenRun(Runnable runnable) 任務A執行完執行任務B，並且B不需要A的結果
 *  2. thenAccept(Consumer action) 任務A執行完執行任務B，B需要A的結果，但是"B沒有返回值"
 *  3. thenApply(Function fn) 任務A執行完執行任務B，B需要A的結果，"B有返回值"
 **/
public class CompletableFutureAPI3Demo {
    public static void main(String[] args) {
//        CompletableFuture.supplyAsync(() -> {
//            return 1;
//        }).thenApply(f -> {
//            return f + 2;
//        }).thenApply(f -> {
//            return f + 3;
//        }).thenAccept(System.out::println);

        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenRun(() -> {}).join());
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenAccept(r -> System.out.println(r)).join());
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenApply(r -> r + "resultB").join());
    }
}
