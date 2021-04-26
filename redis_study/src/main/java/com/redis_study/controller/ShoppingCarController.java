package com.redis_study.controller;


import com.redis_study.model.res.ReturnMessageModel;
import com.redis_study.model.shopingCar.Cart;
import com.redis_study.model.shopingCar.V_Cart;
import com.redis_study.service.ShoppingCarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("京东购物车实战")
@RequestMapping("/api")
public class ShoppingCarController {


    @Autowired
    private ShoppingCarService shoppingCarService;

    @ApiOperation("用戶添加商品购物车")
    @PostMapping("/addUserCar")
    public ReturnMessageModel<String> addUserCar(@RequestBody Cart cart){
        return shoppingCarService.addUserCar(cart);
    }

    @ApiOperation("根据用户id获得商品的购物车")
    @GetMapping("getUserCart")
    public ReturnMessageModel<V_Cart> getUserCart(String userId){
        return shoppingCarService.getUserCart(userId);

    }

    @ApiImplicitParams({
            @ApiImplicitParam(name ="userId",value = "用户id"),
            @ApiImplicitParam(name ="productId",value = "商品id"),
            @ApiImplicitParam(name ="count",value = "变更数量")
    })
    @ApiOperation("变更商品数量")
    @GetMapping("/delUserCartCount")
    public ReturnMessageModel<String> delUserCartCount(String userId, String productId, int count) throws Exception{
        return shoppingCarService.delUserCartCount(userId,productId,count);
    }

    @ApiOperation("刪除商品")
    @DeleteMapping("/delUserCartByProductId")
    public ReturnMessageModel<String> delUserCartByProductId(String userId, String productId){
        return shoppingCarService.delUserCartByProductId(userId, productId);
    }
}
