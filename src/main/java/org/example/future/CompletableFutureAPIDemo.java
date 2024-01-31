package org.example.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author jason
 * @description
 * @create 2024/1/30 18:59
 *
 * 1. get() 獲取結果
 * 2. get(long timeout, TimeUnit unit) 在指定時間內獲取結果，如果超過指定時間還沒有結果則拋出異常
 * 3. join() 相當於get()但是不會拋出異常
 * 4. getNow(T valueIfAbsent) 假設調用這個方法時已經有結果了，那就直接返回該結果，但是如果結果計算還沒完成，則先返回一個替代的結果
 * 5. complete(T value) 是否打斷get()/join()方法(需要看調用這個方法時結果是否已經計算出來了，有結果不打斷，沒結果直接打斷)，如果打斷則get()/join()立刻返回括號裡面的值(T value)
 **/
public class CompletableFutureAPIDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "abc";
        });

//        System.out.println(completableFuture.get());
//        System.out.println(completableFuture.get(1L, TimeUnit.SECONDS));
//        System.out.println(completableFuture.join());

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println(completableFuture.getNow("xxx"));

        System.out.println(completableFuture.complete("completeValue") + "\t" + completableFuture.join());
    }
}
