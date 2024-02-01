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
