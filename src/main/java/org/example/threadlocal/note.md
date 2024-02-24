# ThreadLocal

## 是什麼
ThreadLocal提供線程局部變量。這些變量與正常的變量不同，因為每一個線程在訪問ThreadLocal實例時(通過其get或set方法)都有自己的、獨立的初始化的變量副本。
ThreadLocal實例通常是類中的私有靜態字段，使用他的目的是希望將狀態(ex. 用戶ID或事務ID)與線程關聯起來。

## 能幹嘛
實現**每一個線程都有自己專屬的本地變量副本**。<br>
主要解決了讓每個線程綁定自己的值，通過使用`get()`和`set()`方法，獲取默認值或將其值更改為當前線程所存的副本的值，**從而避免了線程安全問題**。

## 小總結
因為每個Thread內有自己的**實例副本**且該副本只由當前線程自己使用。<br>
既然其他Thread不可訪問，那就不存在多線程間共享的問題。<br>
統一設置初始值，但是每個線程對這個值的修改都是各自線程互相獨立的。<br>

如何才能不爭搶？
1. 加入`synchronized`或者`Lock`控制資源的訪問順序
2. 使用`ThreadLocal`人手一份

## `Thread`、`ThreadLocal`、`ThreadLocalMap`
![img.png](img.png)

* `ThreadLocalMap`實際上就是一個以`ThreadLocal`為key，任意對象為value的Entry對象。
* 當我們為`ThreadLocal`變量賦值，實際上就是以當前`ThreadLocal`實例為key，值為value的Entry往這個`ThreadLocalMap`中存放。

![img_1.png](img_1.png)
JVM內部維護了一個線程版的`Map<ThreadLocal, Value>`(通過`ThreadLocal`對象的`set()`方法，把`ThreadLocal`對象自己當作key，放進`ThreadLocalMap`中)，
每個線程要用到這個`T`的時候，用**當前的線程**去`Map`中獲取，**通過這樣讓每個線程都擁有自己獨立的變量**。