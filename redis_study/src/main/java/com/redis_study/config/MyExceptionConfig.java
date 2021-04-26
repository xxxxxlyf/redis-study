package com.redis_study.config;

import com.redis_study.model.res.ReturnMessageModel;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局異常處理器
 */
@Component
@RestControllerAdvice
public class MyExceptionConfig {

    @ExceptionHandler({Exception.class})
    public ReturnMessageModel<String> myExceptionHandel(Exception e){

      return new  ReturnMessageModel<String>(1,e.getMessage(),"");
    }
}
