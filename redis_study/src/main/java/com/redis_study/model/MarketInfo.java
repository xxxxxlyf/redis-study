package com.redis_study.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商品價目表
 */
@ApiModel
public class MarketInfo {

    @ApiModelProperty("商品名稱")
    public String item;

    @ApiModelProperty("價目表")
    public Double price;

    public MarketInfo(String item, Double price) {
        this.item = item;
        this.price = price;
    }


    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
