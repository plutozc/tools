package example;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhangchi on 2017/11/10.
 */
@Getter
@Setter
public class Student {
    private String name;
    private int age;

    public Student(String name, int age){
        this.name = name;
        this.age = age;
    }
}
