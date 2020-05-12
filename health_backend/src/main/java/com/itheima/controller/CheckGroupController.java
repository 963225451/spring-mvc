package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import com.itheima.service.CheckItemService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    private static Logger logger = Logger.getLogger(CheckGroupController.class);
    @Reference
    private CheckGroupService checkGroupService;
    @Reference
    private CheckItemService checkItemService;



    @RequestMapping("/findPage.do")
    //分页查询所有检查组
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = checkGroupService.findPage(queryPageBean);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageResult);
        } catch (Exception e) {
            logger.error(e);
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL,null);
        }

    }
    @RequestMapping("/findAll_CheckItem.do")
    //查询数据库中所有的检查项
    public Result findAll_CheckItem(){
        try{
            //查询数据库中所有的检查项
            PageResult pageResult = checkItemService.findAll();
           return new Result(true,"",pageResult);
        }catch (Exception e){
            logger.error(e);
            return new Result(false,"",null);
        }


    }
    @RequestMapping("/add.do")
    //新增检查组
    public Result CheckGroupAdd(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.add(checkGroup,checkitemIds);
            return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            logger.error(e);
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }

    }
    @RequestMapping("/findCheckItemByCheckGroupId.do")
    //根据检查项id 查询包含的检查组
    public Result find_CheckItem_By_CheckGroupId(Integer id){
        try {
            PageResult pageResult=checkGroupService.find_CheckItem_By_CheckGroupId(id);
            return new Result(true,"",pageResult);
        } catch (Exception e) {
            logger.error(e);
            return new Result(false,"");

        }
    }
    @RequestMapping("/update.do")
    //根据检查组id 以及前端想要变更为的关联关系 修改检查组
    public Result update(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.update(checkGroup,checkitemIds);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
           logger.error(e);
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }
    @RequestMapping("/delCheckGroupById.do")
    //根据检查组id查询该检查组是否包含检查项或被套餐包含 如都否则删除该检查组
    public Result delCheckGroupById(Integer CheckGroupId){
        int i = checkGroupService.delCheckGroupById(CheckGroupId);
        if(i>0){
            //成功
            return  new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        }else{
            return  new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }
}
