package example;

import lombok.Getter;
import lombok.Setter;
import utils.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public Student() {

    }

    public static void main(String[] args) throws IOException {
        String json = "{\"age\":123}";
        Student a = new Student("aaa", 123);
        Student b = new Student("bbb", 111);
        List<Student> students = new ArrayList<>();
        students.add(a);
        students.add(b);
        String string = JsonUtil.toJson(students);
        Map<String, Object> map = JsonUtil.jsonToMap("[{\"name\":\"aaa\",\"age\":123},{\"name\":\"bbb\",\"age\":111}]");
        List list = JsonUtil.jsonToList("[{\"name\":\"aaa\",\"age\":123},{\"name\":\"bbb\",\"age\":111}]", Student.class);
        Student student = JsonUtil.jsonToObject(json, Student.class);
    }
}
