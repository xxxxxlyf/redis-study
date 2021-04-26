package com.redis_study.service;

import com.redis_study.model.res.ReturnMessageModel;

public interface SeckillService {

    /**
     * 对商品下订单
     * @param productId
     * @param userId
     * @return
     */
    ReturnMessageModel<String>makeAnOrder(String productId,String userId) throws  Exception;


}
