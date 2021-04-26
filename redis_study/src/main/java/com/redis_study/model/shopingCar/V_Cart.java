package com.redis_study.model.shopingCar;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class V_Cart {

    @ApiModelProperty("购物车商品")
    public List<Cart>carts;
    @ApiModelProperty("商品总量")
    public Integer count;
}
