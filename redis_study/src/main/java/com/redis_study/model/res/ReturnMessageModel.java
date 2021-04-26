package com.redis_study.model.res;

import com.sun.xml.internal.ws.developer.Serialization;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnMessageModel<T>{


    @ApiModelProperty(value = "返回码：正确0, 警告2，错误为自定义码")
    private int responseCode;
    @ApiModelProperty(value = "返回消息")
    private String responseMsg;
    @ApiModelProperty(value = "返回具体内容")
    private T result;


    public ReturnMessageModel(T o){
        this.responseCode=0;
        this.responseMsg="success";
        this.result=o;
    }

}
