package org.example;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author jason
 * @description
 * @create 2024/1/27 09:50
 *
 * 異步多線程任務執行且有返回結果，有以下3個特點
 * 1. 多線程：Runnable接口
 * 2. 有返回：Callable接口
 * 3. 異步任務：Future接口
 *
 * => 我們可以使用  FutureTask(Callable<V> callable)  來實現上述3個特點
 * => FutureTask實現RunnableFuture接口，RunnableFuture又同時實現Runnable接口和Future接口，滿足1和3
 * => FutureTask(Callable<V> callable) 構造方法傳入Callable，滿足2
 *
 **/
public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new MyThread());

        Thread t1 = new Thread(futureTask, "t1"); //開啟一個異步線程
        t1.start();
        System.out.println(futureTask.get()); //get()方法可以獲得異步線程的處理結果
    }
}

//Runnable接口實現的run()"沒有返回值"
//class MyThread1 implements Runnable {
//
//    @Override
//    public void run() {
//
//    }
//}

//Callable接口實現的call()"有返回值"
class MyThread implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println("call()...");
        return "hello Callable";
    }
}
