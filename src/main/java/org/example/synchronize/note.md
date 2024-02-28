# Synchronized與鎖升級


## Synchronized的性能變化

* Java5以前，只有synchronized，這是操作系統級別的重量級操作

  * 重量級鎖，假如競爭激烈的話，性能急遽下降
  * **會牽涉用戶態和內核態的切換(容易導致阻塞)**![image.png](./assets/image.png)
* Java6開始為了減少獲得鎖和釋放鎖帶來的性能消耗，引入了**輕量鎖和偏向鎖**，需要有個逐步升級的過程，別一開始到重量級鎖。

## Synchronized鎖種類和升級流程

### 升級流程

* synchronized用的鎖是存在Java對象頭裡的Mark Word中，鎖升級功能主要依賴Mark Word中鎖標誌位和釋放偏向鎖標誌位![image.png](./assets/1709091816427-image.png)
* 鎖指向
  * 偏向鎖：Mark Word存儲的是偏向的線程ID
  * 輕量鎖：Mark Word存儲的是指向線程棧中的Lock Record的指針
  * 重量鎖：Mark Word存儲的是指向堆中的Monitor對象的指針
