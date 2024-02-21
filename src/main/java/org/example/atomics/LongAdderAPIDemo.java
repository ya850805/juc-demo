package org.example.atomics;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author jason
 * @description
 * @create 2024/2/21 10:53
 *
 *  LongAdder只能用來計算加法，且從零開始計算
 *  LongAccumulator提供了自定義的函數操作
 **/
public class LongAdderAPIDemo {
    public static void main(String[] args) {
        LongAdder longAdder = new LongAdder();
        longAdder.increment();
        longAdder.increment();
        longAdder.increment();
        System.out.println(longAdder.sum());

        LongAccumulator longAccumulator = new LongAccumulator((x, y) -> x + y, 0);
        longAccumulator.accumulate(1); //1
        longAccumulator.accumulate(3); //4
        System.out.println(longAccumulator.get());
    }
}
