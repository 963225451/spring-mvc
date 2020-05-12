package com.itheima.jobs;

import com.itheima.Utils.QiniuUtils;
import com.itheima.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class Clear_Img_Job {
    @Autowired
    private JedisPool jedisPool;

    public void Clear_Img_For_TenSecond() {
        Set<String> shujuku = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(shujuku.size()>0){
            for (String s : shujuku) {
                String replace = s.replace("http://q914onw3u.bkt.clouddn.com/", "");
                QiniuUtils.deleteFileFromQiniu(replace);//删除七牛云图片
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,s);
            }
        }

    }
}
