package com.redis_study.service.impl;

import com.redis_study.service.UrlService;
import com.redis_study.util.ShortUrlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class UrlServiceImpl implements UrlService {


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *   存储短连接与长连接映射hash表的键名
     */
    private  static  final  String URL_KEY="short:url";

    @Override
    public String encodeLongUrlToShortUrl(String longUrl) {

        String[] shortDecodes= ShortUrlGenerator.shortUrl(longUrl);
        //默认以第一个元素作为短连接字符串
        String shortUrl=shortDecodes[0];

        //长短连接的映射关系写入hash表中
        redisTemplate.opsForHash().put(URL_KEY,shortUrl,longUrl);
        return shortUrl;
    }

    @Override
    public String decodeLongToShortUrl(String shortUrl) {

        String longUrl=(String) redisTemplate.opsForHash().get(URL_KEY,shortUrl);
        return longUrl;
    }
}
