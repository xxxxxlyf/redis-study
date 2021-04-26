package com.redis_study.service.impl;

import com.redis_study.mapper.StockMapper;
import com.redis_study.model.res.ReturnMessageModel;
import com.redis_study.model.secKill.Sec_Product;
import com.redis_study.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

@Service
public class SeckillServiceImpl implements SeckillService {


    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    private  static  final  String STOCK_PRX="stock:";

    @PostConstruct
    public void init()
    {
        //初始化加載數據庫中的所有商品的庫存信息加入到redis中
        List<Sec_Product> products=stockMapper.getAllProducts();
        for (Sec_Product product : products){
            String stockKey=STOCK_PRX+product.getId();
            redisTemplate.opsForValue().set(stockKey,product.getStock());
        }

    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public ReturnMessageModel<String> makeAnOrder(String productId,String userId) throws Exception {


        //查詢商品信息
        Sec_Product product=stockMapper.getProductById(productId);
        //更新商品庫存
        int cc= stockMapper.updateStockNum(productId);
        //下訂單
        int cc1=stockMapper.makeAnOrder(UUID.randomUUID().toString(),productId,product.getPrice(),userId);
        if(cc!=1||cc1!=1){
            throw  new Exception("商品庫存不足，無法下單");
        }else{
            return new ReturnMessageModel<String>(0,"success","");
        }

    }
}
