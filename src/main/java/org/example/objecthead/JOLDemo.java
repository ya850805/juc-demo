package org.example.objecthead;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * @author jason
 * @description
 * @create 2024/2/27 12:52
 *
 *  JOL = Java Object Layout
 *  分析對象在JVM的內存和佈局
 **/
public class JOLDemo {
    public static void main(String[] args) {
        //VM的細節詳細信息
//        System.out.println(VM.current().details());

        //所有的對象分配的字節都是8的整數倍
//        System.out.println(VM.current().objectAlignment());

        Object o = new Object();
//        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        Customer c1 = new Customer();
        System.out.println(ClassLayout.parseInstance(c1).toPrintable());
    }
}

class Customer {
    int id;
    boolean flag = false;
}

/**
 * 1. 默認配置，啟動了壓縮指針  -XX:+UseCompressedClassPointers
 *      8 + 4 + 4(對齊填充) = 一個對象16字節
 * 2. 手動配置，關閉了壓縮指針  -XX: -UseCompressedClassPointers
 *      8 + 8 = 16字節
 */
