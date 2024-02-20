package org.example.atomics;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author jason
 * @description
 * @create 2024/2/20 12:35
 **/
public class AtomicIntegerArrayDemo {
    public static void main(String[] args) {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[]{1, 2, 3, 4, 5});

        for (int i = 0; i < atomicIntegerArray.length(); i++) {
            System.out.println(atomicIntegerArray.get(i));
        }

        System.out.println("===");

        int tmtInt;

        tmtInt = atomicIntegerArray.getAndSet(0, 1122);
        System.out.println(tmtInt + "\t" + atomicIntegerArray.get(0));

        tmtInt = atomicIntegerArray.getAndIncrement(0);
        System.out.println(tmtInt + "\t" + atomicIntegerArray.get(0));
    }
}
