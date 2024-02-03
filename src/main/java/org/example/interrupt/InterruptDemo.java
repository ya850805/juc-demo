package org.example.interrupt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author jason
 * @description
 * @create 2024/2/3 10:54
 *
 *  一個線程不應該由其他線程來強制中斷或停止，應該"由線程自己自行停止"
 *  所以Thread.stop/Thread.suspend/Thread.resume 都已經被廢棄了
 *
 *  在Java中沒有辦法立即停止一條線程，然而停止線程卻顯得極為重要(ex.取消一個耗時操作)
 *  因此Java提供了一種用於停止線程的協商機制-"中斷"，也即中斷標示協商機制
 *
 *  "中斷只是一種協作協商機制，Java沒有給中斷增加任何語法，中斷的過程完全需要程序員自己實現"
 *  如果要中斷一個線程，需要手動調用該線程的interrupt()方法，"該方法也僅僅是將線程對象的中斷標示設置成true"
 *  接著需要自己寫代碼不斷檢測當前線程的標示位，如果為true，表示別的線程請求這條線程中斷
 *  此時究竟需要做什麼需要自己寫代碼實現
 *
 *  每個線程對象中都有一個中斷標示位，用於表示線程是否被中斷。該標示位為true表示中斷，為false表示未中斷
 *  通過調用線程對象的interrupt()方法將該線程的標示位設置為true。可以在別的線程中調用，也可以在自己的線程中調用
 *
 *************************************************************************************************************
 *
 *  1. public void interrupt() : 實例方法，interrupt()僅僅是設置線程的中斷狀態為true，"發起一個協商而不會立刻停止線程"
 *  2. public static boolean interrupted() : 靜態方法，判斷線程是否被中斷並清除當前中斷狀態，該方法做了以下兩件事
 *      1) 返回當前線程的中斷狀態，測試當前線程是否已被中斷
 *      2) 將當前線程的中斷狀態清零並重新設為false，清除線程的中斷狀態
 *      => 如果連續兩次調用此方法，則第二次調用將返回false，可能連續兩次調用結果不一樣
 *  3. public boolean isInterrupted() : 實例方法，判斷當前線程是否被中斷(通過檢查中斷標示位)
 *
 *************************************************************************************************************
 *
 *  具體來說，當對一個線程調用interrupt()時：
 *  1. 如果線程處於"正常活動狀態"，那會將該線程的中斷標示設置為true，僅此而已
 *     被設置中斷標示的線程將繼續正常運行，不受影響
 *     所以，interrupt()並不能真正的中斷線程，需要被調用的線程自己進行配合才行
 *  2. 如果線程處於被阻塞狀態(例如處於sleep、wait、join等狀態)。在別的線程中調用當前線程對象的interrupt()方法，
 *     那麼線程將立刻退出被阻塞狀態，並"拋出一個InterruptedException異常"
 *
 *************************************************************************************************************
 *
 *  面試題：
 *  1. 如何停止中斷運行中的線程？
 *      1) 通過一個volatile變量實現 => 見 useVolatileVar() 方法範例
 *      2) 通過AtomicBoolean實現 => 見 useAtomicBooleanVar() 方法範例
 *      3) 通過Thread類自帶的中斷API實例方法實現
 *          在需要中斷的線程中不斷監聽中斷狀態，一但發生中斷，就執行相應的中斷處理業務邏輯stop線程
 *  2. 當前線程的中斷標示為true，是不是線程就立刻停止？
 *  3. 靜態方法Thread.interrupted()，談談你的理解
 **/
public class InterruptDemo {
    static volatile boolean isStop = false;
    static AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    public static void main(String[] args) {
//        useVolatileVar();
//        useAtomicBooleanVar();
        useInterruptAPI();
    }

    private static void useInterruptAPI() {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + "\t isStop被修改為true，程序停止");
                    break;
                }
                System.out.println("----- hello interrupt api");
            }
        }, "t1");
        t1.start();

        System.out.println("--- t1的默認中斷標示位：" + t1.isInterrupted());

        //暫停一小段時間後，t2執行
        try { TimeUnit.MILLISECONDS.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            t1.interrupt();
        }, "t2").start();

        //也可以直接自己將自己中斷
//        t1.interrupt();
    }

    private static void useAtomicBooleanVar() {
        new Thread(() -> {
            while (true) {
                if(atomicBoolean.get()) {
                    System.out.println(Thread.currentThread().getName() + "\t isStop被修改為true，程序停止");
                    break;
                }
                System.out.println("----- hello atomicBoolean");
            }
        }, "t1").start();

        //暫停一小段時間後，t2執行
        try { TimeUnit.MILLISECONDS.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            atomicBoolean.set(true);
        }, "t2").start();
    }

    private static void useVolatileVar() {
        new Thread(() -> {
            while (true) {
                if(isStop) {
                    System.out.println(Thread.currentThread().getName() + "\t isStop被修改為true，程序停止");
                    break;
                }
                System.out.println("----- hello volatile");
            }
        }, "t1").start();

        //暫停一小段時間後，t2執行
        try { TimeUnit.MILLISECONDS.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            isStop = true;
        }, "t2").start();
    }
}
