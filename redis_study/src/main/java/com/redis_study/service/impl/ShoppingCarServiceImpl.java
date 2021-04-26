package com.redis_study.service.impl;

import com.redis_study.model.res.ReturnMessageModel;
import com.redis_study.model.shopingCar.Cart;
import com.redis_study.model.shopingCar.V_Cart;
import com.redis_study.service.ShoppingCarService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ShoppingCarServiceImpl implements ShoppingCarService {

    @Autowired
    private RedisTemplate redisTemplate;


    private static final String CAR_KEY = "car:user:";

    @Override
    public ReturnMessageModel<String> addUserCar(Cart cart) {
        ReturnMessageModel<String> res = new ReturnMessageModel<>();
        //用户的购物车key
        try {
            String car_key = CAR_KEY + cart.getUserId();

            //加入购物车
            redisTemplate.opsForHash().put(car_key, cart.getProductId().toString(), cart.getAmount());

            //设置key的过期时间为90天
            redisTemplate.expire(car_key, 90, TimeUnit.DAYS);
            res.setResponseCode(0);
            res.setResponseMsg("添加购物车成功");
        } catch (Exception e) {

            res.setResponseCode(1);
            res.setResponseMsg(e.getLocalizedMessage());
        }

        return res;
    }

    @Override
    public ReturnMessageModel<V_Cart> getUserCart(String userId) {

        ReturnMessageModel<V_Cart> res = new ReturnMessageModel<>();
        V_Cart data = new V_Cart();
        String car_key = CAR_KEY + userId;
        //判断是否存在用户的购物车
        Boolean hasKey = redisTemplate.opsForHash().getOperations().hasKey(car_key);
        if (hasKey) {
            //获得商品总量
            long size = redisTemplate.opsForHash().size(car_key);
            data.setCount((int) size);

            //遍历商品信息
            Map<String, Integer> entries = redisTemplate.opsForHash().entries(car_key);
            entries.keySet();
            List<Cart> carts = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : entries.entrySet()) {
                Cart cart1 = new Cart();
                cart1.setUserId(userId);
                cart1.setAmount(entry.getValue());
                cart1.setProductId(Long.parseLong(entry.getKey()));
                carts.add(cart1);
            }

            data.setCarts(carts);
            return new ReturnMessageModel<V_Cart>(0, "查詢购物车如下", data);
        } else {


            return new ReturnMessageModel<V_Cart>(0, "当前用户的购物车为空", null);
        }
    }

    @Override
    public ReturnMessageModel<String> delUserCartCount(String userId, String productId, int count) throws Exception {


        //判断是否存在当前用户的购物车
        String car_key = CAR_KEY + userId;
        boolean haskey = redisTemplate.opsForHash().getOperations().hasKey(car_key);
        if (!haskey) {
            throw new Exception("当前用户的购物车不存在");
        }else{
            //判断商品是否存在
            if(redisTemplate.opsForHash().hasKey(car_key,productId)){
                //增加商品数量
                if(count>0){
                    redisTemplate.opsForHash().increment(car_key,productId,count);
                }else{

                    //判断当前商品总量是否可以减少
                    int c= (int)redisTemplate.opsForHash().get(car_key,productId);
                    if(c>=Math.abs(count)){

                        redisTemplate.opsForHash().increment(car_key,productId,count);

                    }else{
                        //將商品数量减少至0
                        redisTemplate.opsForHash().put(car_key,productId,0);

                    }

                }

            }
        }
        return new ReturnMessageModel<>(0,"操作成功","");
    }

    @Override
    public ReturnMessageModel<String> delUserCartByProductId(String userId, String productId) {
        String car_key=CAR_KEY+userId;
        redisTemplate.opsForHash().delete(car_key,productId);
        return new ReturnMessageModel<>(0,"操作成功","");
    }


}
