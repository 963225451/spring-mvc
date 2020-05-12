import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class POIText {
//    @Test
//    public void test1() throws IOException {
//        Workbook sheets = new XSSFWorkbook(new FileInputStream(new File("D:\\七牛云\\ordersetting_template.xlsx")));
//        Sheet sheetAt = sheets.getSheetAt(0);
//        for (Row cells : sheetAt) {
//            for (Cell cell : cells) {
//                int cellType = cell.getCellType();//1是字符组成 0 是非字符组成(数字或小数等)
//              if(cellType==1){
//                  String stringCellValue = cell.getStringCellValue();
//                  RichTextString richStringCellValue = cell.getRichStringCellValue();
//                  System.out.println(stringCellValue);
//                  System.out.println(richStringCellValue);
//              }else {
//                  System.out.println(cell);
//                  double numericCellValue = cell.getNumericCellValue();
//                  System.out.println(numericCellValue);
//              }
//            }
//        }
//        sheets.close();
//    }
//    @Test
//    public void test2() throws IOException {
//        Workbook sheets = new XSSFWorkbook(new FileInputStream(new File("D:\\七牛云\\ordersetting_template.xlsx")));
//        Sheet sheetAt = sheets.getSheetAt(0);
//        int lastRowNum = sheetAt.getLastRowNum();
//        for (int i = 0; i <= lastRowNum; i++) {
//            Row row = sheetAt.getRow(i);
//            short lastCellNum = row.getLastCellNum();
//            for (int c = 0; c < lastCellNum; c++) {
//                Cell cell = row.getCell(c);
//                int cellType = cell.getCellType();
//                if(cellType==1){
//                    String stringCellValue = cell.getStringCellValue();
//                    RichTextString richStringCellValue = cell.getRichStringCellValue();
//                    System.out.println(stringCellValue);
//                    System.out.println(richStringCellValue);
//                }else {
//                    double numericCellValue = cell.getNumericCellValue();
//                    System.out.println(numericCellValue);
//                }
//            }
//        }
//        sheets.close();
//    }
}
