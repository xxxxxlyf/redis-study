package com.redis_study.service;

import com.redis_study.model.res.ReturnMessageModel;
import com.redis_study.model.shopingCar.Cart;
import com.redis_study.model.shopingCar.V_Cart;

public interface ShoppingCarService {

    /**
     * 用戶添加商品购物车
     * @param cart
     * @return
     */
    ReturnMessageModel<String> addUserCar(Cart cart);

    /**
     * 根据用户id获得用户的购物车详情
     * @param userId
     * @return
     */
    ReturnMessageModel<V_Cart>getUserCart(String userId);

    /**
     * 变更商品的数量
     * @param userId 用户id
     * @param productId 商品id
     * @param count  变更商品的数量 负数表示减少，正数表示增加
     * @return
     */
    ReturnMessageModel<String> delUserCartCount(String userId,String productId,int count) throws Exception;

    /**
     * 删除用中某商品
     * @param userId 用户id
     * @param productId 商品id
     * @return
     */
    ReturnMessageModel<String> delUserCartByProductId(String userId,String productId);
}
