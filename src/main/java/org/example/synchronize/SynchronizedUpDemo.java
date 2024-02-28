package org.example.synchronize;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author jason
 * @description
 * @create 2024/2/28 11:48
 **/
public class SynchronizedUpDemo {
    public static void main(String[] args) {
        Object o = new Object();

        //如果有調用hashCode()方法，才會去紀錄Hash編碼
        System.out.println("10進制：" + o.hashCode());
        System.out.println("16進制：" + Integer.toHexString(o.hashCode()));
        System.out.println("2進制：" + Integer.toBinaryString(o.hashCode()));

        System.out.println(ClassLayout.parseInstance(o).toPrintable());
    }
}
