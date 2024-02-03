package org.example.interrupt;

/**
 * @author jason
 * @description
 * @create 2024/2/4 06:02
 *
 *  interrupted() 靜態方法：
 *      1) 返回當前線程的中斷狀態，測試當前線程是否已被中斷
 *      2) 將當前線程的中斷狀態清零並重新設為false，清除線程的中斷狀態
 **/
public class InterruptDemo4 {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted()); //false
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted()); //false
        System.out.println("----1");
        Thread.currentThread().interrupt(); //中斷標示位設置為true
        System.out.println("----2");
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted()); //true
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted()); //false
    }
}
