package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.Utils.DateUtils;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.service.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberServiceImpl;
    @Reference
    private SetmealService setmealServiceImpl;
    @Reference
    private OrderService orderServiceImpl;
    @Reference
    private HostSetmeal hostSetmealImpl;
    @Reference
    private ReportService reportServiceImpl;
    //获取会员数据折线图的数据
    @RequestMapping("/getMemberReport.do")
    public Result getMemberReport(){
        Map<String,Object> map = new HashMap<>();
        List<String> months = new ArrayList();
        Calendar calendar = Calendar.getInstance();//获得日历对象，模拟时间就是当前时间
        //计算过去一年的11个月 2019年4月18日
        calendar.add(Calendar.MONTH,-11);//获得当前时间往前推12个月的时间
        Date time = calendar.getTime();
        for(int i=0;i<12;i++){

            Date date = calendar.getTime();
            try {
                String s = DateUtils.parseDate2String(date, "yyyy-MM");//格式化日期
            months.add(s+"-31");
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put("months",months);
            calendar.add(Calendar.MONTH,1);//获得当前时间往后推一个月日期
        }
        List<Integer> memberCount = memberServiceImpl.findMemberCountByMonths(months);
        //[1,2,3,4,5]
        map.put("memberCount",memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    @RequestMapping("/getSetmealReport.do")
    public Result getSetmealReport(){
        Map map = new HashMap();//最终返回数据
        List<String> list = new ArrayList<String>();//饼块名称setmealNames
        List<Map<String,Object>> countList=setmealServiceImpl.findSetmealCount();
        for (Map<String,Object> stringIntegerMap : countList) {
           String name = (String) stringIntegerMap.get("name");
           list.add(name);
        }
        map.put("setmealNames",list);
        map.put("setmealCount",countList);
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);

    }
    @RequestMapping("/getBusinessReportData.do")
    public Result getBusinessReportData()  {
        try {
            Map<String, Object> map=  reportServiceImpl.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping("/exportBusinessReport.do")
    //导出数据统计到文件 交由浏览器下载
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try{
            Map<String,Object> result = reportServiceImpl.getBusinessReportData();

            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //request.getSession().getServletContext().getRealPath("template") 找到webapp目录下的template文件夹绝对路径 linux / windows \
            String filePath = request.getSession().getServletContext().getRealPath("template")+
                    File.separator+"report_template.xlsx";
            //基于提供的Excel模板文件在内存中创建一个Excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File(filePath)));
//            TemplateExportParams params = new TemplateExportParams(
//                    request.getSession().getServletContext().getRealPath("template")+
//                            File.separator+"report_template.xlsx"
//            );
//            Workbook sheets = ExcelExportUtil.exportExcel(params, result);
            //读取第一个工作表
            XSSFSheet sheet = excel.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);

                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //使用输出流进行表格下载,基于浏览器作为客户端下载
            OutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");//代表的是Excel文件类型
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定以附件形式进行下载
            excel.write(out);

            out.flush();
            out.close();
            excel.close();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping("/exportBusinessReport4PDF.do")
    //导出数据统计到PDF文件 交由浏览器下载
    public Result exportBusinessReport4PDF(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> businessReportData = reportServiceImpl.getBusinessReportData();
        //模板文件位置
        String jrxmlPath="D:\\java_java_java\\health\\health_parent\\health_backend\\src\\main\\webapp\\template\\health_business3.jrxml";
        //编译文件输出位置
        String jasperPath = "D:\\java_java_java\\health\\health_parent\\health_backend\\src\\main\\webapp\\template\\health_business3.jasper";
        List<Map> hotSetmeal = (List<Map>) businessReportData.get("hotSetmeal");
        try {
            //编译模板
            JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, businessReportData, new JRBeanCollectionDataSource(hotSetmeal));

            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/pdf");
            response.setHeader("content-Disposition", "attachment;filename=report.pdf");

            //输出文件
            JasperExportManager.exportReportToPdfStream(jasperPrint,out);
            out.flush();
            out.close();

            return null;

        } catch (Exception e) {
            e.printStackTrace();
        return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}
