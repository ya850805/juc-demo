package org.example.future;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author jason
 * @description
 * @create 2024/1/29 18:20
 *
 * 电商网站比价需求分析：
 * 1. 需求说明：
 *   a. 同一款产品，同时搜索出同款产品在各大电商平台的售价
 *   b. 同一款产品，同时搜索出本产品在同一个电商平台下，各个入驻卖家售价是多少
 * 2. 输出返回：
 *   a. 出来结果希望是同款产品的在不同地方的价格清单列表，返回一个List<String>
 *      例如：《Mysql》 in jd price is 88.05  《Mysql》 in taobao price is 90.43
 * 3. 解决方案，对比同一个产品在各个平台上的价格，要求获得一个清单列表
 *   a. step by step，按部就班，查完淘宝查京东，查完京东查天猫....
 *   b. all in，万箭齐发，一口气多线程异步任务同时查询
 *
 **/
public class CompletableFutureMallDemo {
    //電商列表
    static List<NetMall> list = Arrays.asList(
        new NetMall("jd"),
        new NetMall("dangdang"),
        new NetMall("taobao")
    );

    /**
     * 方式一：一家一家查詢
     * @param list
     * @param productName
     * @return
     */
    public static List<String> getPrice(List<NetMall> list, String productName) {
        //《Mysql》 in jd price is 88.05
        return list.stream()
                .map(netMall -> String.format(productName + " in %s is %.2f", netMall.getNetMallName(), netMall.calPrice(productName)))
                .collect(Collectors.toList());
    }

    /**
     * 方式二：使用CompletableFuture同時異步查詢
     * @param list
     * @param productName
     * @return
     */
    public static List<String> getPriceByCompletableFuture(List<NetMall> list, String productName) {
        return list.stream()
                .map(netMall ->
                        CompletableFuture.supplyAsync(() -> String.format(productName + " in %s is %.2f", netMall.getNetMallName(), netMall.calPrice(productName))))
                .collect(Collectors.toList())
                .stream()
                .map(cf -> cf.join())
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        List<String> list1 = getPrice(list, "mysql");
        for (String s : list1) {
            System.out.println(s);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("---costTime: " + (endTime - startTime) + "毫秒");

        System.out.println("---------------------");

        long startTime1 = System.currentTimeMillis();
        List<String> list2 = getPriceByCompletableFuture(list, "mysql");
        for (String s : list2) {
            System.out.println(s);
        }
        long endTime1 = System.currentTimeMillis();
        System.out.println("---costTime: " + (endTime1 - startTime1) + "毫秒");
    }
}

@Data
@AllArgsConstructor
class NetMall {
    private String netMallName; //商城名稱

    public double calPrice(String productName) {
        //模擬查詢需要一秒
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //模擬返回一個價格
        return ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(0);
    }
}

/******************************************************************************************/
/******************************************************************************************/
/******************************************************************************************/
/******************************************************************************************/
/******************************************************************************************/
/******************************************************************************************/

class CompletableFutureMallPreDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Student student = new Student();

        //鏈式調用
        student
            .setId(1)
            .setStudentName("name1")
            .setMajor("English");

        /**************************************************************************/

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            return "hello 1234";
        });

        /**
         * get(): 會會在編譯期間拋出檢查型異常(ExecutionException, InterruptedException)
         * join(): 不會在編譯期間拋出檢查型異常
         */
//        System.out.println(completableFuture.get());
        System.out.println(completableFuture.join());
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
class Student {
    private Integer id;
    private String studentName;
    private String major;
}
