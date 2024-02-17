package org.example.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jason
 * @description
 * @create 2024/2/17 19:38
 **/
public class CASDemo {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);
        System.out.println(atomicInteger.compareAndSet(5, 2022) + "\t" + atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(5, 2022) + "\t" + atomicInteger.get());
    }
}
