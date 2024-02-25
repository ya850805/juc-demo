package org.example.threadlocal;

import java.lang.ref.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author jason
 * @description
 * @create 2024/2/24 08:43
 **/
public class ReferenceDemo {
    public static void main(String[] args) {
//        strongReference();
//        softReference();
//        weakReference();
        phantomReference();
    }

    private static void phantomReference() {
        MyObject myObject = new MyObject();
        ReferenceQueue<MyObject> referenceQueue = new ReferenceQueue<>();
        PhantomReference<MyObject> phantomReference = new PhantomReference<>(myObject, referenceQueue);
//        System.out.println(phantomReference.get());

        //這邊設置runtime虛擬機參數設置內存
        /**
         * -Xms10m -Xmx10m
         */

        //模擬用光內存
        List<byte[]> list = new ArrayList<>();
        new Thread(() -> {
            while(true) {
                list.add(new byte[1 * 1024 * 1024]);
                try { TimeUnit.MILLISECONDS.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println(phantomReference.get() + "\t" + "list add done");
            }
        }, "t1").start();

        new Thread(() -> {
            while (true) {
                Reference<? extends MyObject> reference = referenceQueue.poll();
                if(null != reference) {
                    System.out.println("----有虛引用對象回收加入了對列");
                }
            }
        }, "t2").start();
    }

    private static void weakReference() {
        WeakReference<MyObject> weakReference = new WeakReference<>(new MyObject());

        System.out.println("before gc: " + weakReference.get());

        //呼叫垃圾回收並等待他執行
        System.gc();
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println("after gc: " + weakReference.get());
    }

    private static void softReference() {
        SoftReference<MyObject> softReference = new SoftReference<>(new MyObject());
        System.out.println("---- softReference: " + softReference.get());

        //呼叫垃圾回收並等待他執行
        System.gc();
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("---- after gc內存夠用：" + softReference.get());

        //這邊設置runtime虛擬機參數設置內存
        /**
         * -Xms10m -Xmx10m
         */

        try {
            byte[] bytes = new byte[20 * 1024 * 1024]; //20M 對象
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("---- after gc內存不夠：" + softReference.get());
        }
    }

    private static void strongReference() {
        MyObject myObject = new MyObject();
        System.out.println("before gc: " + myObject);

        myObject = null;
        System.gc(); //人工開啟gc，一般不用

        System.out.println("after gc: " + myObject);
    }
}

class MyObject {
    //這個方法一般來說不用重寫，只是為了演示
    @Override
    protected void finalize() throws Throwable {
        //finalize的目的是在對象被不可撤銷的丟棄之前執行清理操作
        System.out.println("------- invoke finalize method");
    }
}
