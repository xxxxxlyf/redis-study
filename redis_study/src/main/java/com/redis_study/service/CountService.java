package com.redis_study.service;


public interface CountService {

    /**
     * 更新數據計數器 （Hash 更新時間片+計數次數   Zset 中的計數器列表）
     * @param name 計數器名稱
     * @param count 更新頻次
     */
    public void update_counter(String name,long count);



}
