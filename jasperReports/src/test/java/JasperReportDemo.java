import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JasperReportDemo {
    @Test
    public void test() throws JRException {
        //模板文件位置
        String jrxmlPath="D:\\java_java_java\\health\\health_parent\\jasperReports\\src\\main\\resources\\demo.jrxml";
        //编译文件输出位置
        String jasperPath = "D:\\java_java_java\\health\\health_parent\\jasperReports\\src\\main\\resources\\demo.jasper";
        //模板编译，编译为后缀为jasper的二进制文件
        JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);
        //为模板文件准备数据，用于最终的PDF文件数据填充
        //构造数据
        Map paramters = new HashMap();
        paramters.put("reportDate","2019-10-10");
        paramters.put("company","itcast");

        List<Map> list = new ArrayList();
        Map map1 = new HashMap();
        map1.put("name","xiaoming");
        map1.put("address","beijing");
        map1.put("email","xiaoming@itcast.cn");
        Map map2 = new HashMap();
        map2.put("name","xiaoli");
        map2.put("address","nanjing");
        map2.put("email","xiaoli@itcast.cn");
        list.add(map1);
        list.add(map2);

        //填充数据                 填充代理人                   编译完文件的地址  普通数据    遍历数据
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath,paramters,new JRBeanCollectionDataSource(list));

        //输出文件  路径
        String pdfPath = "D:\\test.pdf";
        //输出代理人
        JasperExportManager.exportReportToPdfFile(jasperPrint,pdfPath);
    }
    @Test
    public void test2() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/health","jiang","shayebushi");
        //模板文件位置
        String jrxmlPath="D:\\java_java_java\\health\\health_parent\\jasperReports\\src\\main\\resources\\demo1.jrxml";
        //编译文件输出位置
        String jasperPath = "D:\\java_java_java\\health\\health_parent\\jasperReports\\src\\main\\resources\\demo1.jasper";
        //编译
        JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);
        //为模板文件准备数据，用于最终的PDF文件数据填充
        Map map = new HashMap();
        map.put("company","传智播客");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, map, connection);
        String pdfPath = "D:\\test1.pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint,pdfPath);
    }
    @Test
    public void test3() throws Exception{
        //模板文件位置
        String jrxmlPath="D:\\java_java_java\\health\\health_parent\\jasperReports\\src\\main\\resources\\demo2.jrxml";
        //编译文件输出位置
        String jasperPath = "D:\\java_java_java\\health\\health_parent\\jasperReports\\src\\main\\resources\\demo2.jasper";

        //模板编译，编译为后缀为jasper的二进制文件
        JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

        //为模板文件准备数据，用于最终的PDF文件数据填充
        Map map = new HashMap();
        map.put("company","传智播客");

        //Javabean数据源填充，用于填充列表数据
        List<Map> list = new ArrayList();
        Map map1 = new HashMap();
        map1.put("name","入职体检套餐");
        map1.put("code","RZTJ");
        map1.put("age","18-60");
        map1.put("tSex","男");

        Map map2 = new HashMap();
        map2.put("name","阳光爸妈老年健康体检");
        map2.put("code","YGBM");
        map2.put("age","55-60");
        map2.put("sex","女");
        list.add(map1);
        list.add(map2);

        //填充数据
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath,map,new JRBeanCollectionDataSource(list));

        //输出文件
        String pdfPath = "D:\\test2.pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint,pdfPath);
    }
}
