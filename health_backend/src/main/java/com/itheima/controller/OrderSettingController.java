package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.Utils.POIUtils;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    private static Logger logger = Logger.getLogger(OrderSettingController.class);
    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload.do")//接收文件要用
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        try {
            List<String[]> strings = POIUtils.readExcel(excelFile);//使用POI解析表格数据 一行一行读取的
            for (int i = 0; i < 2; i++) {//两列用于校验文件是否正常的数据 所以先取两次 核对校验码
                String[] strings1 = strings.get(i);//取到第一行的数据
                if (i == 0) {
                    String s = strings1[1];//拿到第一行第二个数据  此码为校验文件 dsoi2Goih4GkjB7ib
                    boolean dsoi2Goih4GkjB7ib = s.equals("dsoi2Goih4GkjB7ib");
                    if (dsoi2Goih4GkjB7ib == false) {//判断是否错误上传其他文件
                        return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
                    }
                }
            }
            for (int i = 0; i < 2; i++) {//核对文件完成 删除用于核对的两行数据
                strings.remove(0);
            }

            List<OrderSetting> uc = new ArrayList<>();
            for (String[] string : strings) {
                uc.add(new OrderSetting(new Date(string[0]), Integer.parseInt(string[1])));
            }
            int add = orderSettingService.add(uc);
            if (add > 0) {//判断是否执行成功
                return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
            } else {
                return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
            }
        } catch (IOException e) {

            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/getOrderSettingByMonth.do")
    //根据年 月获得当前月已经设置过预约人数的信息
    public Result getOrderSettingByMonth(String data) {

        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(data);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        } catch (Exception e) {
            logger.error(e);
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/editNumberByDate.do")
    //设置可预约人数
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.editNumberByDate(orderSetting); //预约设置成功
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace(); //预约设置失败
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
