package com.redis_study.mapper;

import com.redis_study.model.secKill.Sec_Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface StockMapper {


    /**
     * 更新商品数量的缓存，缓存减一,數據層加鎖
     * @param productId 商品id
     * @return
     */
    @Update("update product set stock=stock-1 where stock>0 and id=#{productId}")
    int updateStockNum(String productId);

    /**
     * 根據商品id查詢相關的產品信息
     * @param productId
     * @return
     */
    @Select("select * from product where id=#{productId}")
    Sec_Product getProductById(String productId);


    /**
     * 用戶下單
     * @param id
     * @param product_id
     * @param price
     * @param user_id
     * @return
     */
    @Insert("insert into orders(id,product_id,price,user_id)VALUES(#{id},#{product_id},#{price},#{user_id})")
    int makeAnOrder(String id,String product_id,float price,String user_id);


    /**
     * 獲得商品的所有庫存
     * @return
     */
    @Select("select * from product")
    List<Sec_Product>getAllProducts();
}
