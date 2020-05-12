package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.CheckGroup_CheckItemDao;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)

public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;
    @Autowired
    private CheckGroup_CheckItemDao checkGroup_checkItemDao;

    @Override
    @Transactional
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();//当前页码数
        Integer pageSize = queryPageBean.getPageSize();//
        String queryString = queryPageBean.getQueryString();//查询关键字

        if(!(queryString==null||queryString=="")){
            queryString = "%" + queryString + "%"; //%null%
        }
        final String param = queryString;//分页组件想要一个确定不会再被碰过的花姑娘( final)
//        List<CheckItem> list = checkItemDao.pageQuery(param);
//        PageInfo pageInfo = PageHelper.startPage(currentPage, pageSize).doSelectPageInfo(() -> checkItemDao.pageQuery(param));

//给分页组件传入 当前页码数   每页显示记录数 他会拦截下个请求
        PageInfo<CheckItem> pageInfo = PageHelper.startPage(currentPage, pageSize).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
//                不要在该方法内写过多代码，只要一行查询方法最好  查询语句交给组件来执行 以确保一定拦截到这个查询语句
                checkItemDao.pageQuery(param);
            }
        });
//        PageHelper.startPage(currentPage, pageSize);
//        Page<CheckItem> checkItems = checkItemDao.pageQuery(queryString);
//        System.out.println(checkItems);
//        long total = checkItems.getTotal();//查询出的数据总量
//        List<CheckItem> result = checkItems.getResult();//查询出的数据

        long total = pageInfo.getTotal();//查询出的数据总量
        List<CheckItem> result = pageInfo.getList();//查询出的数据
        return new PageResult(total, result);
    }

    @Override
    public int delById(int id) {
        //查询这个检查项是否被检查组包含
        List<Integer> integers = checkGroup_checkItemDao.findByCheckitemId(id);
        if(integers.size()>0){//有关 不能删
            return 1;//辨别码 失败
        }else {
            checkItemDao.delById(id);//无关 删除
            return 2;//辨别码 成功
        }


    }

    @Override
    public void updateById(CheckItem checkItem) {
        checkItemDao.updateById(checkItem);
    }

    @Override//查询数据库中所有的检查项
    public PageResult findAll() {
        List<CheckItem> checkItems = checkItemDao.pageQuery(null);
        return new PageResult((long) checkItems.size(),checkItems);
    }

    @Override
    public CheckItem fandById(Integer integer) {
        CheckItem checkItem= checkItemDao.fandById(integer);
        return checkItem;
    }

}
