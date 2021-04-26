package com.redis_study.service.impl;

import com.redis_study.model.Product;
import com.redis_study.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void createProduct(Product product) {

        //省略數據庫操作信息,01應該是從數據庫返回的唯一值id
        String key = "product:" + 01;

        Map<String, Object> map = getMapFromObject(product);

        //redis 操作寫入數據,等價於命令：Hmset key filed value[filed value],一次寫入多個屬性與值的數據
        redisTemplate.opsForHash().putAll(key, map);
    }


    /**
     * 商品漲價
     * @param product_id 商品id
     * @param price 漲價的錢
     */
    @Override
    public  void addPrice(String product_id,float price){

      //省略數據庫操作信息

      //更新數據庫欄位信息
      String key="product:"+product_id;
      redisTemplate.opsForHash().increment(key,"price",price);

    }

    /**
     * 將對象轉成hashmap對象，利用反射的性質，獲得鍵和值的數據
     *
     * @param o
     * @return
     */
    public Map<String, Object> getMapFromObject(Object o) {

        Map<String, Object> map = new HashMap<>();
        Class o_class = o.getClass();
        //獲得對象的所有屬性,getDeclaredFields,獲得public private protect修飾的屬性
        Field[] fields = o_class.getDeclaredFields();
        System.out.println(fields.length);
        for (Field field : fields) {
            //設置可以獲取到私有
            field.setAccessible(true);
            //獲得屬性名稱
            String fieldName = field.getName();
            System.out.println(fieldName);
            Object filedValue = null;
            //獲得屬性值
            try {
                filedValue = field.get(o);
                System.out.println(filedValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            map.put(fieldName, filedValue);


        }
        return map;
    }
}
