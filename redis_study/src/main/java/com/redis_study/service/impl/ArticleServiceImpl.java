package com.redis_study.service.impl;

import com.redis_study.model.Article;
import com.redis_study.model.res.ReturnMessageModel;
import com.redis_study.service.ArticleService;
import com.redis_study.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private RedisTemplate redisTemplate;

    private RedisUtil redisUtil;

    private static  final int ONE_WEEK_IN_SECONDS=7*86400;
    private static  final  int VOTE_SCORE=432;

    @Override
    public void voteArticle(String userId, String article_Id){

        redisUtil=new RedisUtil(redisTemplate);

         //计算时间是否还在投票的截止时间内(一周)
        long cutoff = (System.currentTimeMillis() / 1000) - ONE_WEEK_IN_SECONDS;
        //超出一周时间之后，用户无法对当前文章进行投票
        if(redisUtil.getScoreBymember("time","article:"+article_Id)<cutoff){
             return ;

        }else {
            //用户第一次对文章进行投票时
            if (redisUtil.sadd(article_Id, userId)) {
                //加入时间有序列表
                redisUtil.zIncrby("time", article_Id, VOTE_SCORE);
                //加入投票有序列表
                redisUtil.zIncrby("score", article_Id, 1);

            }else{
                System.out.println(userId+"对当前文章重复投票，计数无效");
            }
        }

    }

    @Override
    public String postArticle(Article article) {
        redisUtil=new RedisUtil(redisTemplate);

        //獲得文章id
        long id=redisUtil.sIncrby("article");
        long time=System.currentTimeMillis()/1000;

        //将文章信息加入到hashtable中
        Map<String,Object> map=new HashMap<>();
        map.put("title",article.title);
        map.put("link",article.link);
        map.put("poster",article.poster);
        map.put("time",time);
        map.put("votes",1);
        redisUtil.hmset("article:"+id,map);

        //文章信息加入到用户投票中
        redisUtil.sadd("voted:"+id,"user:"+article.poster);

        //文章加入到評分有序集合中
        redisUtil.zadd("time","article:"+id,1);
        redisUtil.zadd("score","article:"+id,time);

        return String.valueOf(id);
    }

    @Override
    public List<Article> getVotestArticles(int page, int size) {
        return null;
    }


    /**
     * 獲得文章的閱讀量（每篇文章的，每個用戶只計數一個用戶量）當前技術缺陷是需要頻繁的修改redis的數據
     * @param articleId 文章id
     * @param userId  用戶id
     * @return
     */
    @Override
    public ReturnMessageModel<Long> getArticleView(String articleId, String userId){
        String articleKey="article:"+articleId;

        String userKey="user:"+userId;

        //判斷是否存在用戶閲讀數據
        boolean flag=redisTemplate.opsForSet().isMember(articleKey,userKey);
        if(!flag){
            //添加閲讀的用戶
            redisTemplate.opsForSet().add(articleKey,userKey);
        }


        //獲得set的總數據
        long count=redisTemplate.opsForSet().size(articleKey);
        return new ReturnMessageModel<Long>(0,"id為"+articleId+"的閱讀量為："+count,count);

    }


    /**
     * 獲得文章的閱讀量 用戶可以重複閱讀
     * @param articleId 文章id
     * @return
     */
    @Override
    public ReturnMessageModel<Integer>getArticleView1(String articleId){

        String articleKey="article::"+articleId;

        //數據自增
        redisTemplate.opsForValue().increment(articleKey);

        //獲得數據量
        int count=(Integer) redisTemplate.opsForValue().get(articleKey);


        return new ReturnMessageModel<>(0,"id為"+articleId+"的閱讀量為："+count,count);
    }
}
