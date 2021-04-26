package com.redis_study.service;

import com.redis_study.model.Article;
import com.redis_study.model.res.ReturnMessageModel;

import java.util.List;

/**
 * 文章服务类
 */
public interface ArticleService {

    /**
     * 给文章点赞
     * @param userId 人员id
     * @param artileId 文章id
     */
     void voteArticle(String userId,String artileId);

    /**
     * 发布文章
     * @return
     */
     String postArticle(Article article);

    /**
     * 获得指定数量的投票数文章
     * @param page
     * @param size
     * @return
     */
    List<Article>getVotestArticles(int page,int size);

    /**
     * 獲得文章的閱讀量
     * @param articleId 文章id
     * @param userId  用戶id
     * @return
     */
     ReturnMessageModel<Long>getArticleView(String articleId,String userId);


    /**
     * 獲得文章的閱讀量 用戶可以重複閱讀
     * @param articleId 文章id
     * @return
     */
     ReturnMessageModel<Integer>getArticleView1(String articleId);
}
