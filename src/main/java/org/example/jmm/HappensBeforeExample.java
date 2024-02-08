package org.example.jmm;

/**
 * @author jason
 * @description
 * @create 2024/2/8 17:16
 *
 *  1. 利用volatile保證讀取操作的可見性
 *  2. 利用synchronized保證操作的原子性結合使用鎖和volatile變量來減少同步的開銷
 **/
public class HappensBeforeExample {
    private volatile int value = 0;

    public int getValue() {
        return value;
    }

    public synchronized void setValue() {
        ++value;
    }
}
