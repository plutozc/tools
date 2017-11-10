package example;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import utils.ExcelUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangchi on 2017/11/10.
 */
public class ExcelExample {

    public ResponseEntity<byte[]> test() throws IOException {
        Student A = new Student("A", 1);
        List<String> data = new ArrayList<>();
        StringBuffer row = new StringBuffer();
        //设置列名
        row.append("姓名,年龄");
        row = new StringBuffer();
        row.append(A.getName()).append(",");
        row.append(A.getAge()).append(",");
        data.add(row.toString());
        ExcelUtil.createExcel(data);
        String filename = "学生";
        HttpHeaders header = new HttpHeaders();
        //设置编码格式
        header.set("Content-Disposition", "form-data; name=\"attachment\"; filename*=UTF-8''" + URLEncoder.encode(filename + ".xls", "utf-8"));
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(ExcelUtil.createExcel(data), header, HttpStatus.OK);
    }
}
