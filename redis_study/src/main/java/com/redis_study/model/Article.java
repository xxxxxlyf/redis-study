package com.redis_study.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    public String title;
    public String link;
    public String poster;
    public Long  time;
    public int votes;
}
