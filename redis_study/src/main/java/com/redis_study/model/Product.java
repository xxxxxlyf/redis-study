package com.redis_study.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("商品信息")
public class Product {


    @ApiModelProperty("商品編號")
    private  String product_no;
    @ApiModelProperty("商品產地")
    private  String origin_place;
    @ApiModelProperty("商品名称")
    private  String product_name;
    @ApiModelProperty("商品價格")
    private  float price;
    @ApiModelProperty("商品標籤")
    private  String product_tag;
}
