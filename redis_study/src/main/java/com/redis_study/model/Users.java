package com.redis_study.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Serializable {


    public String id;
    public String username;
    public String password;
    public int sex;
    public int deleted;
    public Date update_time;
    public Date create_time;
}
