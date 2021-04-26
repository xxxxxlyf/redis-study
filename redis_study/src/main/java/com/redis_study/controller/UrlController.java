package com.redis_study.controller;


import com.redis_study.model.res.ReturnMessageModel;
import com.redis_study.service.UrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("URL")
@RequestMapping("/api")
public class UrlController {


    @Autowired
    private UrlService urlService;


    @ApiOperation("将长连接通过算法转化为短连接")
    @GetMapping("/encodeLongToShortUrl")
    @ApiImplicitParam(name="longUrl",value = "真实长连接")
    public ReturnMessageModel<String>encodeLongToShortUrl(String longUrl){
        String shortUrl=urlService.encodeLongUrlToShortUrl(longUrl);
        return new ReturnMessageModel<>(shortUrl);
    }


    @ApiOperation("通过短连接映射到真实长连接")
    @GetMapping("/decodeShortToLongUrl")
    @ApiImplicitParam(name="shortUrl",value = "短連接")
    public ReturnMessageModel<String>decodeShortToLongUrl(String shortUrl){
        String longUrl=urlService.decodeLongToShortUrl(shortUrl);
        return new ReturnMessageModel<>(longUrl);
    }
}
