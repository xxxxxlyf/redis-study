package com.redis_study.controller;


import com.redis_study.model.MarketInfo;
import com.redis_study.model.res.ReturnMessageModel;
import com.redis_study.service.MarketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("商场交易")
@RestController
@RequestMapping("/api/")
public class MarketController {

    @Autowired
    private MarketService marketService;


    @GetMapping("/getMarketInfo")
    @ApiImplicitParam(name = "sort",value = "排序",defaultValue = "asc")
    public ReturnMessageModel<List<MarketInfo>> getMarketInfo(String sort){

        return  marketService.getMarketInfo(sort);
    }




}
