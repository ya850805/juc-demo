package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author jason
 * @description
 * @create 2024/1/29 18:20
 **/
public class CompletableFutureMallDemo {
    public static void main(String[] args) {

    }
}

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
