package com.redis_study.controller;

import com.redis_study.model.Article;
import com.redis_study.model.res.ReturnMessageModel;
import com.redis_study.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api("文章管理")
@RestController
@RequestMapping("/api/")
public class ArticleController {


    @Autowired
    private ArticleService articleService;


    @ApiOperation("發佈一篇文章")
    @PostMapping("/postAnArticle")
    public ReturnMessageModel<String>postAnArticle(@RequestBody Article article){
        return new ReturnMessageModel<String>( articleService.postArticle(article));
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name="articleId",value = "文章id"),
            @ApiImplicitParam(name="userId",value = "用戶id")
    })
    @ApiOperation("用戶為指定的文章投票")
    @GetMapping("/VoteArticle")
    public ReturnMessageModel<String>VoteArticle(String articleId,String userId){

        articleService.voteArticle(userId,articleId);
        return new ReturnMessageModel<>("");
    }



    @ApiImplicitParams({

            @ApiImplicitParam(name="page",value = "每頁"),
            @ApiImplicitParam(name="size",value = "每頁數量")
    })
    @ApiOperation("/分頁獲得排名前列的文章列表")
    @GetMapping("/getTopArticleList")
    public ReturnMessageModel<List<Article>>getTopArticleList(int page,int size){

        List<Article> votestArticles = articleService.getVotestArticles(page, size);
        return  new ReturnMessageModel<>(votestArticles);

    }


    @ApiOperation("/獲得文章的閱讀量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId",value = "文章id",defaultValue = "1"),
            @ApiImplicitParam(name = "userId",value = "用戶id",defaultValue = "1"),
    })
    @GetMapping("/getArticleView")
    public ReturnMessageModel<Long> getArticleView(String articleId, String userId){

        return articleService.getArticleView(articleId,userId);
    }

    @ApiOperation("/獲得文章的閱讀量,可以重複閱讀")
    @GetMapping("/getArticleView1")
    @ApiImplicitParam(name = "articleId",value = "文章id",defaultValue = "1")
    public ReturnMessageModel<Integer>getArticleView1(String articleId){
        return articleService.getArticleView1(articleId);
    }
}
