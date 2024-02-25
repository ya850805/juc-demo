package org.example.threadlocal;

/**
 * @author jason
 * @description
 * @create 2024/2/24 08:43
 **/
public class ReferenceDemo {
    public static void main(String[] args) {
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
