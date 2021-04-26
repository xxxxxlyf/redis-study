package com.redis_study.model.secKill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("秒杀中商品信息")
public class Sec_Product implements Serializable {


    @ApiModelProperty("唯一行标")
    private String id;
    @ApiModelProperty("商品名称")
    private String name;
    @ApiModelProperty("商品单价")
    private float price;
    @ApiModelProperty("商品库存")
    private int stock;



}
