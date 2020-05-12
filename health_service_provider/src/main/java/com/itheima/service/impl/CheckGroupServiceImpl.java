package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.CheckGroup_CheckItemDao;
import com.itheima.dao.CheckItemDao;
import com.itheima.dao.Setmeal_CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Autowired
    private CheckGroup_CheckItemDao checkGroup_checkItemDao;
    @Autowired
    private Setmeal_CheckGroupDao setmeal_checkgroupDao;
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer pageSize = queryPageBean.getPageSize();//每页显示数
        Integer currentPage = queryPageBean.getCurrentPage();//当前页码数
        String queryString = queryPageBean.getQueryString();//查询条件
        if (!(queryString == null || queryString == "")) {
            queryString = "%" + queryString + "%";
        }
        final String query = queryString;//分页组件想要一个确定不会再被碰过的花姑娘( final)
        PageInfo<CheckGroup> pageInfo = PageHelper.startPage(currentPage, pageSize).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                //交给分页组件执行查询
                checkGroupDao.findPage(query);
            }
        });
        List<CheckGroup> list = pageInfo.getList();//查询到的数据

        long total = pageInfo.getTotal();//总纪录数
        return new PageResult(total, list);

    }

    @Override
    @Transactional//涉及多次数据库修改操作  加事务
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //参数  检查组信息         检查组包含的检查项id
        checkGroupDao.add(checkGroup);//先去添加检查项表
        // 通过mybatis框架提供的selectKey标签获得自增产生的检查组主键ID值
        //这个值会被mybatis写回到传入的对象中
        Integer checkGroupId = checkGroup.getId();//拿到检查组主键id
        if (checkitemIds != null && checkitemIds.length > 0) {
//            根据检查组id 构建关联关系
            checkGroup_checkItemDao.setCheckGroupAndCheckItem(checkGroupId, checkitemIds);
        }


    }

    @Override//根据检查组id其包含的检查项id
    public PageResult find_CheckItem_By_CheckGroupId(Integer checkGroupId) {
        List<Integer> CheckItemids = checkGroup_checkItemDao.find_CheckItem_By_CheckGroupId(checkGroupId);
        return new PageResult((long) CheckItemids.size(), CheckItemids);
    }

    @Override
    @Transactional//涉及多次数据库修改数据操作 添加事务
    //根据前端传回最新的关联关系 修改至数据库 其核心关键在于如何确定哪些关联关系 需要新增 那些需要删除
    //两个list  A{1,2,3,4,5}   removeAll   B{2,3,4}==>{1,5}    B{2,3,4,6,7}  removeAll  A{1,2,3,4,5}==>{6,7}
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        //checkitemIds需要保存的数据
        //修改检查组信息 操作t_checkgroup表
        checkGroupDao.update(checkGroup);
        //拿到检查组id
        Integer checkGroupId = checkGroup.getId();
        //数据库中现存的关联关系
        List<Integer> CheckItemids = checkGroup_checkItemDao.find_CheckItem_By_CheckGroupId(checkGroup.getId());
        //前端想要让其保持的关联关系
        List<Integer> checkItemIdsEdit = new ArrayList<>(Arrays.asList(checkitemIds));
        List<Integer> tempList = new ArrayList<>(Arrays.asList(checkitemIds));
        //编辑后的减去编辑前的，获取到需要新增的检查项
        checkItemIdsEdit.removeAll(CheckItemids);
        //需要删除的检查项
        CheckItemids.removeAll(tempList);

        if (checkItemIdsEdit.size() > 0) {
            Integer[] integers = checkItemIdsEdit.toArray(new Integer[checkItemIdsEdit.size()]);
            //新增关联关系
            checkGroup_checkItemDao.addByCheckGroupId(checkGroupId, integers);
        }
        if (CheckItemids.size() > 0) {
            Integer[] integers = CheckItemids.toArray(new Integer[CheckItemids.size()]);
            //删除关联关系
            checkGroup_checkItemDao.delByCheckGroupId(checkGroupId,integers);

        }

    }

    @Override//根据检查组id查询该检查组是否包含检查项或被套餐包含 如都否则删除该检查组
    public int delCheckGroupById(Integer checkGroupId) {
        //查询这个检查组是否被套餐包含
        List<Integer> cc=setmeal_checkgroupDao.find_By_CheckGroup_Id(checkGroupId);
        if(cc.size()>0){
            //标识 -1不成功
            return -1;
        }else {
            //查询这个检查组是否包含检查项
            //这块应该返回前台包含的检查项列表 提示其确定删除否
            List<Integer> integers = checkGroup_checkItemDao.find_CheckItem_By_CheckGroupId(checkGroupId);
            if(integers.size()>0){
                //标识 -1不成功
                return -1;
            }
            checkGroupDao.delById(checkGroupId);
            //标识 1成功
            return 1;
        }
    }

    @Override//根据检查组id查找检查组以及其包含的检查项
    public CheckGroup fandById(Integer integer) {
        CheckGroup checkGroup=checkGroupDao.fandById(integer);
        return checkGroup;
    }


    @Override//根据检查组id查找关联检查项ids
    public List<Integer> fandCheckItemIdByCheckGroupId(Integer id) {
        List<Integer> checkItemId = checkGroup_checkItemDao.find_CheckItem_By_CheckGroupId(id);
        return checkItemId;
    }

}


