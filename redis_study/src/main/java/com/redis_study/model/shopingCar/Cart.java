package com.redis_study.model.shopingCar;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Cart {

    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("商品id")
    private Long productId;
    @ApiModelProperty("商品数量")
    private Integer amount;
}
