package com.redis_study.service;

import com.redis_study.model.MarketInfo;
import com.redis_study.model.res.ReturnMessageModel;

import java.util.List;

public interface MarketService {

    /**
     * 把商品放置在市场上销售
     * @param sellerId 卖家id
     * @param item 商品
     * @param price 商品价格
     */
     void addItemToMarket(String sellerId,String item,long price);

    /**
     * 买家购买商品上的商品
     * @param buyerId 买家id
     * @param sellerId 卖家id
     * @param item  商品
     * @param price  商品价格
     */
    void buyItemFromSeller(String buyerId,String sellerId,String item,long price);

    /**
     * 獲得商場上的所有商品（升序或者降序排序）
     * @param sort  順序
     * @return
     */
    ReturnMessageModel<List<MarketInfo>>getMarketInfo(String sort);
}
