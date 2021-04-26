package com.redis_study.service.impl;


import com.redis_study.model.MarketInfo;
import com.redis_study.model.res.ReturnMessageModel;
import com.redis_study.service.MarketService;
import com.redis_study.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MarketServiceImpl implements MarketService {


    @Autowired
    private RedisTemplate redisTemplate;

    private RedisUtil redisUtil;

    /**
     * 把商品放置在市场上销售
     * @param sellerId 卖家id
     * @param item 商品
     * @param price 商品价格
     */
    @Override
    public void addItemToMarket(String sellerId,String item,long price){
        redisUtil=new RedisUtil(redisTemplate);

            //卖家仓库
            String inventorySeller="inventory:"+sellerId;

            //监视卖家 sellerId 仓库 销售的产品时候还存在 (当前键的数据发生变化时，无法执行命令入队列)
            redisUtil.watchKey(inventorySeller);

            //判斷卖家仓库中是否存在产品
            Boolean flag =redisUtil.isMemberInSet(inventorySeller,item);

            if(!flag){
                redisUtil.unwatchKey();
            }else{
                //开启事务,之后的命令人命令队列
                redisUtil.multi();
                //移除卖家仓库中的数据
                redisUtil.remKeysItem(inventorySeller,item);
                //卖家指定的商品入库
                redisUtil.zadd("market",item+":"+sellerId,price);
                //执行入队列指令
                redisUtil.exec();
            }

        }

    /**
     * 买家购买商品上的商品
     * @param buyerId 买家id
     * @param sellerId 卖家id
     * @param item  商品
     * @param price  商品价格
     */
    @Override
    public void buyItemFromSeller(String buyerId,String sellerId,String item,long price){

        //监视商品
        redisUtil.watchKey("market");
        //监视买家个人信息
        redisUtil.watchKey("user:"+buyerId);

        //获得买家的资金
        Long funds= (Long)redisUtil.hget("user:"+buyerId,"funds");
        //对比买家的资金与商品价格
        boolean flag=funds>price?true:false;

        if(flag){
            //开启事务
            redisUtil.multi();

            //命令进入执行队列中
            //商品放入到买家仓库
            redisUtil.sadd("inventory:"+buyerId,item);
            //个人资金减少
            redisUtil.hIncrBy("user:"+buyerId,"funds",Math.abs(price));
            //卖家个人资金增加
            redisUtil.hIncrBy("user:"+sellerId,"funds",price);
            //商品从商场中撤出
            redisUtil.zremMember("market",item+":"+sellerId);
            //商品加到买家仓库中
            redisUtil.sadd("inventory"+buyerId,item);

            //执行任务队列中的所有命令
            redisUtil.exec();

        }else{

            System.out.println("余额不足，无法购买当前产品");
        }


    }

    @Override
    public ReturnMessageModel<List<MarketInfo>> getMarketInfo(String sort) {
        List<MarketInfo>marketInfos=new ArrayList<>();
        //升序查詢所有價目表
        if(sort.equals("asc")){
            Set<ZSetOperations.TypedTuple<Double>> market = redisTemplate.opsForZSet().rangeWithScores("market", 0, -1);
            for(ZSetOperations.TypedTuple<Double> value : market){
                MarketInfo info = new MarketInfo(String.valueOf(value.getValue()), value.getScore());
                marketInfos.add(info);
            }


        }else if(sort.equals("desc")){


        }
        return new ReturnMessageModel<List<MarketInfo>>(marketInfos);
    }


}
