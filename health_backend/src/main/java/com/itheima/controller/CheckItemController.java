package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController//他就等于@Controller + @ResponseBody
@RequestMapping("/checkitem")
public class CheckItemController {
    private static Logger logger = Logger.getLogger(CheckItemController.class);
    @Reference
    private CheckItemService checkItemService;

    //新增检查项
    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckItem checkItem) {
//        调用远程接口
        if (checkItem.getCode() == "" || checkItem.getName() == ""
                || checkItem.getName() == null || checkItem.getCode() == null) {
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            logger.error(e);
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //检查项分页查询
    @RequestMapping("/findPage.do")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {

        try {
            PageResult pageResult = checkItemService.findPage(queryPageBean);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageResult);
        } catch (Exception e) {
            logger.error(e);
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL,null);
        }



    }

    @RequestMapping("/delete.do")
//    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")//必须具有权限
    @PreAuthorize("hasRole('ROLE_ADMIN')")//必须具有角色
    //删除检查项
    public Result delById(Integer id){
        checkItemService.delById(id);
        try {
            int i = checkItemService.delById(id);
            if(i==1){//删除的检查项跟跟检查组有关系 不能直接删除
                return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL_TO_RELATION);
            }
            return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            logger.error(e);
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }
    @RequestMapping("/update.do")
    //修改检查项
    public Result updateById(@RequestBody CheckItem checkItem){
        try {
            checkItemService.updateById(checkItem);
            return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            logger.error(e);
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }
}
