package com.redis_study.service;

public interface UrlService {


    /**
     * 将长连接通过算法转化为短连接
     * @param longUrl
     * @return
     */
    String encodeLongUrlToShortUrl(String longUrl);


    /**
     * 获得短连接对应的真实长连接，路由到真正的网址
     * @param shortUrl
     * @return
     */
    String decodeLongToShortUrl(String shortUrl);
}
