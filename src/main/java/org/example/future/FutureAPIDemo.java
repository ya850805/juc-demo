package org.example.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author jason
 * @description
 * @create 2024/1/28 09:59
 *
 * 1. get()方法一調用，非要等到結果才會離開，不管你是否計算完成，容易導致程序阻塞
 *      -> 通常get()會放到最後，這樣也不會阻塞到其他線程(main)
 * 2. 假如不願意等待很長時間，希望過時不候，可以自動離開
 *      -> 可以使用 get(long timeout, TimeUnit unit) ，一旦超時就自動拋出TimeoutException
 * 3. isDone()輪詢：如果想要異步獲取結果，通常都會以輪詢的方式去獲取結果，盡量不要阻塞
 *      -> 不過輪詢的方式會耗費無謂的CPU資源，而且也不見得能及時得到結果(下方while中的else區塊)
 *
 * 結論：Future對於結果的獲取不是很友好，只能透過"阻塞"或是"輪詢"的方式得到任務的結果！
 **/
public class FutureAPIDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + "\t --- come in");

            //暫停幾秒後返回
            TimeUnit.MILLISECONDS.sleep(5000);

            return "task over";
        });

        Thread t1 = new Thread(futureTask, "t1");
        t1.start();

        //get()會導致阻塞
//        System.out.println(futureTask.get());

        System.out.println(Thread.currentThread().getName() + "\t --- 忙其他任務了");

//        System.out.println(futureTask.get());
//        System.out.println(futureTask.get(3, TimeUnit.SECONDS));

        while (true) {
            if(futureTask.isDone()) {
                System.out.println(futureTask.get());
                break;
            } else {
                //暫停幾秒後，再去看futureTask是否完成了！
                TimeUnit.MILLISECONDS.sleep(500);
                System.out.println("正在處理中...");
            }
        }
    }
}
