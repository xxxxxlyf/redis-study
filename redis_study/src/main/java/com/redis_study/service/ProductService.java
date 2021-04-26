package com.redis_study.service;

import com.redis_study.model.Product;

public interface ProductService {


    /**
     * 錄入商品信息
     * @param product
     */
    void createProduct(Product product);

    /**
     * 商品漲價
     * @param product_id 商品id
     * @param price 漲價的錢
     */
    void addPrice(String product_id,float price);
}
