package com.redis_study.service.impl;

import com.redis_study.service.CountService;
import com.redis_study.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 *  计数功能管理
 */
@Service
@SuppressWarnings({"all"})
public class CountServiceImpl implements CountService {

    //计数器精度 1s,5s,1min,5min,1h,5h,24h
    public  static  final  int[] PRECISION=new int[]{1,5,60,300,3600,18000,86400};

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void update_counter(String name, long count) {
        RedisUtil redisUtil=new RedisUtil(redisTemplate);

        for(int prec:PRECISION){
            //获得当前时间片开始起点时间
            long pnow = (System.currentTimeMillis() / prec) * prec;
            String hash = String.valueOf(prec) + ':' + name;
            redisUtil.zadd("known:", hash, 0);
            redisUtil.hIncrBy("count:" + hash, String.valueOf(pnow), count);
        }
    }


}
