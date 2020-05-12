package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.Utils.QiniuUtils;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckGroupService;
import com.itheima.service.SetmealService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    private static Logger logger = Logger.getLogger(SetmealController.class);
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private SetmealService setmealService;
    @Reference
    private CheckGroupService checkGroupService;
    @RequestMapping("/find_checkgroup.do")
    public Result find_checkgroup() {
        try {
            PageResult pageResult = checkGroupService.findPage(new QueryPageBean(1, 10000, null));
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageResult);
        } catch (Exception e) {
            logger.error(e);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }

    }
    @RequestMapping("/findAll.do")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = setmealService.findPage(queryPageBean);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,pageResult);
        } catch (Exception e) {
            logger.error(e);
            return new Result(true, MessageConstant.QUERY_SETMEAL_FAIL);
        }

    }
    @RequestMapping("/add.do")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            int add = setmealService.add(setmeal, checkgroupIds);
            if(add<0){
                return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
            }

            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            logger.error(e);
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }

    }
    @RequestMapping("/upload.do")
    public Result upload(@RequestParam("imgFile") MultipartFile impFile){
        String tou="http://q914onw3u.bkt.clouddn.com/";
        String originalFilename = impFile.getOriginalFilename();//拿到原始文件名
        int i = originalFilename.lastIndexOf(".");
        String substring = originalFilename.substring(i);
        String imgName = UUID.randomUUID().toString()+substring;
        try {
            QiniuUtils.upload2Qiniu(impFile.getBytes(),imgName);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,tou+imgName);
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,imgName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

}
