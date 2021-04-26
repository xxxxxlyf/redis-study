package com.redis_study.service.impl;

import com.redis_study.mapper.UserMapper;
import com.redis_study.model.Users;
import com.redis_study.model.res.ReturnMessageModel;
import com.redis_study.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;


/**
 * @CacheConfig 類前綴的註解，統一該類所有緩存的key前綴,eg users::
 * @Cacheable 方法級別的註解，作用於將方法的結果緩存下來，先去調用緩存，緩存不存在，則調用方法體，進入數據庫查詢，將查詢結果添加到緩存中
 * @
 */

@Service
@CacheConfig(cacheNames = {"users"})
public class UserServiceImpl implements UserService {


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;


    @Override
    public void initUser() {

        for (int i = 0; i <100 ; i++) {
            Users users = new Users("","lyf", "tttt",0 ,0,null,null);
            users.id= UUID.randomUUID().toString();
            userMapper.addUser(users);

        }
    }

    @Override
    @Cacheable(key="#id")
    public Users getUserById(String id) {

        Users user=userMapper.getUserById(id);
        return user;
    }

    @Override
    public void updateUser(Users user) {

    }
}
