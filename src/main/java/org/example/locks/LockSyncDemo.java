package org.example.locks;

/**
 * @author jason
 * @description
 * @create 2024/2/1 18:20
 *
 *  javap -c Xxx.class 文件反編譯(可以使用 -v 查看更詳細反編譯信息)
 *
 *  可以發現：synchronized同步代碼塊，實現使用的是monitorenter和monitorexit指令
 *  一般情況是一個monitorenter和兩個monitorexit(第二個確保可以釋放鎖)
 *
 *  synchronized普通同步方法
 *      調用指令會檢查方法的ACC_SYNCHRONIZED訪問標誌是否被設置
 *      如果設置了，執行線程會將先持有monitor鎖，然後再執行方法
 *      最後在方法完成(無論是正常完成還是非正常完成)時釋放monitor
 *
 *  synchronized靜態同步方法
 *      ACC_STATIC, ACC_SYNCHRONIZED 方問標誌區分該方法是否靜態同步方法(看要使用類鎖還是對象鎖)
 **/
public class LockSyncDemo {
    Object object = new Object();

    public void m1() {
        synchronized (object) {
            System.out.println("---hello synchronized code block");
        }
    }

    public synchronized void m2() {
        System.out.println("---hello synchronized m2");
    }

    public static synchronized void m3() {
        System.out.println("---hello static synchronized m3");
    }

    public static void main(String[] args) {

    }
}
