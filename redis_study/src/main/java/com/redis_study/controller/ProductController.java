package com.redis_study.controller;


import com.redis_study.model.Product;
import com.redis_study.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("商品管理")
@RequestMapping("/api")
public class ProductController {



    @Autowired
    private ProductService productService;


    @ApiOperation("/添加商品信息")
    @PostMapping(value = "/addProduct")
    public void addProduct(@RequestBody Product product){

        productService.createProduct(product);
    }


    @ApiImplicitParams({

            @ApiImplicitParam(name ="product_id",value = "商品id",defaultValue = "1"),
            @ApiImplicitParam(name ="price",value = "商品漲價金額",defaultValue = "1")
    })
    @ApiOperation("根據商品id修改商品的價格")
    @GetMapping("/addPriceAtProduct")
    public void addPriceAtProduct(String product_id,float price){
         productService.addPrice(product_id,price);
    }
}
