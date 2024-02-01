package org.example.locks;

import java.util.concurrent.TimeUnit;

/**
 * @author jason
 * @description
 * @create 2024/2/1 12:05
 *
 *  線程 操縱 資源類
 *
 * 1. 標準訪問有ab兩個線程，請問先打印郵件(sendEmail)還是短信(sendSMS) ===> 郵件
 * 2. sendEmail方法中加入暫停3秒，請問先打印郵件(sendEmail)還是短信(sendSMS) ===> 郵件
 * 3. 添加一個普通的hello方法(沒有synchronized)，請問先打印郵件還是hello ===> hello
 * 4. 有兩部手機，請問先打印郵件還是短信 ===> 短信
 * 5. 有兩個"靜態"同步方法，有一部手機，請問先打印郵件(sendEmail)還是短信(sendSMS) ===> 郵件
 * 6. 有兩個"靜態"同步方法，有兩部手機，請問先打印郵件(sendEmail)還是短信(sendSMS) ===> 郵件
 * 7. 有一個靜態同步方法，有一個普通同步方法，有一部手機，請問先打印郵件(sendEmail)(static)還是短信(sendSMS)(non static) ===> 短信
 * 8. 有一個靜態同步方法，有一個普通同步方法，有兩部手機，請問先打印郵件(sendEmail)(static)還是短信(sendSMS)(non static) ===> 短信
 *
 * 總結：
 * 1-2
 *      一個對象裡面如果有多個synchronized方法，某一個時刻內，只要一個線程去調用其中的一個synchronized方法了，
 *      其他的線程都只能等待，換句話說，某一個時刻內，只能有唯一的一個線程去訪問這些synchronized方法
 *      所的是當前對象this，被鎖定後，其他的線程都不能進入到當前對象的其他synchronized方法
 * 3-4
 *      hello()方法沒有synchronized修飾，所以不參與資源的爭搶，可以直接執行，因此先打印hello
 *      加了普通方法後發現和同步鎖無關
 *      換成兩個對象後，不是同一把鎖了，情況立刻改變
 * 5-6
 *      加上static就變成類鎖，相當於鎖的是同一個類(類模板)
 *      都換成靜態同步方法後，情況又有些變化
 *      三種synchronized鎖的內容有一些差別：
 *          1. 對於普通同步方法(synchronized)，鎖的是當前實例對象，通常指this，具體的一部部手機，所有的普通同步方法用的都是同一把鎖 -> "實例對象本身"
 *          2. 對於靜態同步方法(static synchronized)，"鎖的是當前類的Class對象"，如Phone.class唯一的一個模板
 *          3. 對於同步方法塊(synchronized(xxx) {})，鎖的是"synchronized括號內的對象"
 * 7-8
 *      當一個線程試圖訪問同步代碼塊時，他首先必須得到鎖，正常退出或是拋出異常時必須釋放鎖(避免死鎖或阻塞)
 *
 *      所有的普通同步方法用的都是同一把鎖 - 實例對象本身，就是new出來的具體實例對象本身，本類this
 *      也就是說如果一個實例對象的普通同步方法獲取鎖後，該實例對象的其他普通同步方法必須等待獲取鎖的方法釋放後鎖才能獲取鎖
 *
 *      所有的靜態同步方法用的也是同一把鎖 - 類對象本身，就是我們說過唯一模板Class
 *      具體實例對象this和唯一模板Class，這兩把鎖是兩個不同的對象，所以靜態同步方法與普通同步方法之間是不會有競爭條件的
 *      但是一但一個靜態同步方法獲取鎖後，其他的靜態同步方法都必須等待該方法釋放鎖後才能獲取鎖
 *
 **/
public class Lock8Demo {
    public static void main(String[] args) {
        Phone phone = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> {
            phone.sendEmail();
        }, "a").start();

        //暫停200毫秒，保證a線程先啟動
        try { TimeUnit.MILLISECONDS.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
//            phone.sendSMS();
//            phone.hello();
            phone2.sendSMS();
        }, "b").start();
    }
}

//資源類
class Phone {
    public static synchronized void sendEmail() {
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("----sendEmail");
    }

    public synchronized void sendSMS() {
        System.out.println("----sendSMS");
    }

    public void hello() {
        System.out.println("----hello");
    }
}
