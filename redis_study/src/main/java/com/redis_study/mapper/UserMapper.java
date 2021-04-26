package com.redis_study.mapper;

import com.redis_study.model.Users;

public interface UserMapper {


    /**
     * 添加用戶信息
     * @param user
     * @return
     */
    public int addUser(Users user);

    /**
     * 根據用戶id查詢用戶數據
     * @param id
     * @return
     */
    public Users getUserById(String id);

    /**
     * 更新用戶數據
     * @param user
     * @return
     */
    public int updateUserInfo(Users user);


}
