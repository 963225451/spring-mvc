package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckGroupService;
import com.itheima.service.CheckItemService;
import com.itheima.service.SetmealService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/setmeal")
public class SetmealController {
   Logger logger = Logger.getLogger(SetmealController.class);
   @Reference
    private SetmealService setmealService;
   @Reference
   private CheckItemService checkItemService;
   @Reference
   private CheckGroupService checkGroupService;

    @RequestMapping("/getAllSetmeal.do")
    public Result getAllSetmeal() {
        try {
            List<Setmeal> list=setmealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        } catch (Exception e) {
           logger.error(e);
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }

    }
    @RequestMapping("/findById.do")
    public Result getAllSetmeal(Integer id){
        try {
            Setmeal setmeal= setmealService.finAllRelationdById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            logger.error(e);
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
//    @RequestMapping("/findById.do")
//    public Result getAllSetmeal(Integer id){
//        try {
//            //先去查找套餐内容
//            Setmeal setmeal=setmealService.findById(id);
//            //查询套餐跟检查组的关系表 得到相关检查组id
//            List<Integer> list= setmealService.fandGroupIdBySetmralId(id);
//            List<CheckGroup> checkGroups=new ArrayList<CheckGroup>();
//            if(list.size()>0){
//                for (Integer integer : list) {
//                    //查询相关检查组内容
//                    CheckGroup checkGroup=checkGroupService.fandById(integer);
//                    //查询检查组跟检查项的关系表 得到相关检查项id
//                    List<Integer> checkItemIdList=checkGroupService.fandCheckItemIdByCheckGroupId(checkGroup.getId());
//                    List<CheckItem> checkItemlist= new ArrayList<CheckItem>();
//                    if(checkItemIdList!=null&&checkItemIdList.size()>0){
//                        for (Integer integer1 : checkItemIdList) {
//                            CheckItem checkItem= checkItemService.fandById(integer1);
//                            checkItemlist.add(checkItem);
//                        }
//                    }
//                    checkGroup.setCheckItems(checkItemlist);
//                    checkGroups.add(checkGroup);
//                }
//            }
//            setmeal.setCheckGroups(checkGroups);
//            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
//        } catch (Exception e) {
//            logger.error(e);
//            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
//        }
//
//
//    }
}
