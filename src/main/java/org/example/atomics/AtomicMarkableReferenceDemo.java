package org.example.atomics;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @author jason
 * @description
 * @create 2024/2/20 12:41
 *
 *  AtomicStampedReference: version版本號 + 1 ===> 解決"修改過幾次"的問題
 *  AtomicMarkableReference: 一次 false true ===> 解決"是否有修改過"的問題
 *
 **/
public class AtomicMarkableReferenceDemo {
    //初始的時候值是100，false代表還沒有線程動過
    static AtomicMarkableReference<Integer> atomicMarkableReference = new AtomicMarkableReference<>(100, false);

    public static void main(String[] args) {
        new Thread(() -> {
            boolean marked = atomicMarkableReference.isMarked();
            System.out.println(Thread.currentThread().getName() + "\t" + "默認標示: " + marked);

            //暫停一秒，等待後面的t2和我拿到一樣的flag標示，都是false
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

            atomicMarkableReference.compareAndSet(100, 1000, marked, !marked);
        }, "t1").start();

        new Thread(() -> {
            boolean marked = atomicMarkableReference.isMarked();
            System.out.println(Thread.currentThread().getName() + "\t" + "默認標示: " + marked);

            try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }

            boolean b = atomicMarkableReference.compareAndSet(100, 2000, marked, !marked);
            System.out.println(Thread.currentThread().getName() + "\t" + "t2線程CAS result: " + b);
            System.out.println(Thread.currentThread().getName() + "\t" + atomicMarkableReference.isMarked());
            System.out.println(Thread.currentThread().getName() + "\t" + atomicMarkableReference.getReference());
        }, "t2").start();
    }
}
