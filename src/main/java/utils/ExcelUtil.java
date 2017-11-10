package utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by zhangchi on 2017/11/10.
 */
public class ExcelUtil {

    /**
     * 生成excel字节，不生成文件
     *
     * @param data
     * @return
     */
    public static byte[] createExcel(List<String> data) throws IOException {
        try (Workbook wb = new HSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Sheet1");
            int lineNo = 0;
            for (String line : data) {
                Row row = sheet.createRow(lineNo);
                int cellNo = 0;
                //根据 ，区分数据 可以自定义
                for (String value : line.split(",")) {
                    Cell cell = row.createCell(cellNo);
                    cell.setCellValue(value);
                    cellNo++;
                }
                lineNo++;
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024 * 2);
            wb.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
