package org.example.future;

import java.util.concurrent.*;

/**
 * @author jason
 * @description
 * @create 2024/1/29 17:53
 *
 * 異步任務結束時，會自動回調某個對象的方法
 * 主線程設置好回調後，不再關心異步任務的執行，異步任務之間可以順序執行
 * 異步任務出錯時，會自動回調某個對象的方法
 **/
public class CompletableFutureUseDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        future1();

        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        try {
            CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName() + "--- come in");
                int result = ThreadLocalRandom.current().nextInt(10);

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("1秒鐘後出結果：" + result);

                if(result > 5) {
                    int i = 10 / 0;
                }

                return result;
            }, threadPool).whenComplete((v, e) -> {
                if(null == e) {
                    System.out.println("(上一步沒有發生異常)----計算完成，更新系統updateValue：" + v);
                }
            }).exceptionally(e -> {
                e.printStackTrace();
                System.out.println("異常情況：" + e.getCause() + "\t" + e.getMessage());
                return null;
            });

            System.out.println(Thread.currentThread().getName() + "線程先去忙其他任務");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }


        //使用默認的ForkJoinPool.commonPool()線程池，主線程不要立刻結束，否則CompletableFuture默認使用的線程池會立刻關閉 -> 暫停3秒
//        try {
//            TimeUnit.SECONDS.sleep(3);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    //原本Future就可以完成的動作
    private static void future1() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "--- come in");
            int result = ThreadLocalRandom.current().nextInt(10);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("1秒鐘後出結果：" + result);
            return result;
        });

        System.out.println(Thread.currentThread().getName() + "線程先去忙其他任務");
        System.out.println(completableFuture.get());
    }
}
