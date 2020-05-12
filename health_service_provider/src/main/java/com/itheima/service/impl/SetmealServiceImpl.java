package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.MemberDao;
import com.itheima.dao.SetmealDao;
import com.itheima.dao.Setmeal_CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Member;
import com.itheima.pojo.Setmeal;
import com.itheima.service.MemberService;
import com.itheima.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private Setmeal_CheckGroupDao setmeal_checkGroupDao;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Autowired
    private MemberService memberServiceImpl;
    @Autowired
    private MemberDao memberDao;
    @Value("${out_put_path}")
    private String outPutPath;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        String queryString = queryPageBean.getQueryString();
        if (!(queryString == null || queryString.equals(""))) {
            queryString = "%" + queryString + "%";
        }
        final String query = queryString;
        PageInfo<Setmeal> pageInfo = PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize()).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                setmealDao.findPage(query);
            }
        });
        List<Setmeal> list = pageInfo.getList();
        long total = pageInfo.getTotal();
        return new PageResult(total, list);
    }

    @Override
    public int add(Setmeal setmeal, Integer[] checkgroupIds) {

        try {
            setmealDao.add(setmeal);
        } catch (Exception e) {
            return -1;
        }
        if (setmeal.getImg() != null && setmeal.getImg().length() > 0) {
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        }
        if (checkgroupIds.length > 0) {
            setmeal_checkGroupDao.add(setmeal.getId(), checkgroupIds);
            try {
                generateMobileStaticHtml(setmeal.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        } else {
            return -1;
        }

    }

    @Override
    public List<Setmeal> findAll() {
        List<Setmeal> cc = setmealDao.fandAll();
        return cc;
    }

    @Override
    public Setmeal findById(Integer id) {
        Setmeal setmeal = setmealDao.fandById(id);
        return setmeal;
    }

    @Override
    public List<Integer> fandGroupIdBySetmralId(Integer id) {
        List<Integer> list = setmeal_checkGroupDao.fandGroupIdBySetmralId(id);
        return list;
    }

    @Override//查询套餐详情(包括关联信息)
    public Setmeal finAllRelationdById(Integer id) {
        Setmeal setmeal = setmealDao.finAllRelationdById(id);
        return setmeal;
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        List<Map<String, Object>> maps =setmealDao.findSetmealCount();
        return maps;
    }

    @Override//查询指定日期之后新增的会员
    public List<Member> findMemberAfterDate(String DateString) {
        List<Member> afterDateMember =memberDao.findMemberAfterDate(DateString);
        return afterDateMember;
    }

    @Override
    public List<Member> findMemberOfMonth(String s) {
        List<Member> list=  memberDao.findMemberOfMonth(s);
        return list;
    }

    //通用的方法，用于生成静态页面
    public void generteHtml(String templetName, String htmlPageName, Map map) throws IOException, TemplateException {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //设置模板文件所在目录
//        configuration.setDirectoryForTemplateLoading(new File(""));
        //加载模板文件

            Template template = configuration.getTemplate(templetName);

            //准备输出流对象，用于输出静态文件
//            Writer writer = new FileWriter(outPutPath+"/"+htmlPageName);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPutPath  + htmlPageName)),"utf-8"));
            //输出
            template.process(map,writer);
            //关闭流
            writer.close();

        //准备模板文件中所需要的数据，通常是通过Map进行构造
//        Map map = new HashMap();
//        map.put("name","itcast");
//        map.put("message","hello freemarker!!");
        //设置字符集
//        configuration.setDefaultEncoding("utf-8");


    }
    //生成当前方法所需的静态页面
    public  void generateMobileStaticHtml(Integer id) throws IOException, TemplateException {

        //在生成静态页面之前需要查询数据
//        List<Setmeal> setmeals = setmealDao.fandAll();

        //需要生成套餐列表静态页面
        generateMobileSetmealListHtml();

        //需要生成套餐详情静态页面
        generateMobileSetmealDetailHtml(id);
    }
    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml() throws IOException, TemplateException {
        List<Setmeal> setmeals = setmealDao.fandAll();
        Map map = new HashMap();
        //为模板提供数据，用于生成静态页面
        map.put("setmealList",setmeals);
        generteHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }

    //生成套餐详情静态页面（可能有多个）
    public void generateMobileSetmealDetailHtml(Integer id) throws IOException, TemplateException {

            Map map = new HashMap();
            map.put("setmeal",setmealDao.finAllRelationdById(id));
            generteHtml("mobile_setmeal_detail.ftl","setmeal_detail_" + id + ".html",map);

    }

    //预约提交

}
