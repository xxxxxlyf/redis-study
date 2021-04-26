package com.redis_study.service;

import com.redis_study.model.Users;


public interface UserService {

    /**
     * 初始化用戶對象
     */
    public void initUser();

    /**
     * 根據id查詢用戶數據
     * @param id
     * @return
     */
    public Users  getUserById(String id);

    /**
     * 更新用戶對象數據
     * @param user
     */
    public void updateUser(Users user);
}
