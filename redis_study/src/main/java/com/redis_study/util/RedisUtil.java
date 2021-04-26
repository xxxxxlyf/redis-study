package com.redis_study.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Map;
import java.util.Set;

/**
 * redis操作的实用工具类
 * @author lyf
 */
public class RedisUtil {

    private  RedisTemplate redisTemplate;

    public RedisUtil(RedisTemplate _redisTemplate){
        redisTemplate= _redisTemplate;
    }


    /**************************有序集合数据操作**********************/
    /**
     * 将一个给定分值的成员添加到有序集合中
     * @param key 键
     * @param member 成员
     * @param score 分值
     * @return 是否添加成功
     */
    public  Boolean zadd(String key,Object member,double score){
       return  redisTemplate.opsForZSet().add(key,member,score);
    }

    /**
     * 获得分值范围内中的所有元素集合
     * @return
     */
    public  Set<Object> zrangeMemberByScore(String key,double s1,double s2){

       return  redisTemplate.opsForZSet().rangeByScore(key,s1,s2);
    }

    /**
     * 获得有序集合中的成员的分值
     * @param key
     * @param member
     * @return
     */
    public  double getScoreBymember(String key,Object member){
        return redisTemplate.opsForZSet().score(key,member);
    }


    /**
     * 增加散列某對象的評分
     * @param key
     * @param member
     * @param s
     */
    public double zIncrby(String key,Object member,double s){

      return  redisTemplate.opsForZSet().incrementScore(key,member,s);
    }


    /**
     * 獲得有序散列中指定順序的成員對象（正序排列）
     * @param key
     * @param startIndex
     * @param endIndex
     * @return
     */
    public Set<Object>  getMembersAsc(String key,int startIndex,int endIndex){

      return   redisTemplate.opsForZSet().reverseRangeByScore(key,startIndex,endIndex);
    }

    /**
     * 移除zset数据集中成员
     * @param key zset的key
     * @param o zset的成员对象
     */
    public void zremMember(String key,Object o){
        redisTemplate.opsForZSet().remove(key,o);
    }


    /************************hash数据结构的操作***********************/
    /**
     * 批量插入数据到hashTable中
     * @param key
     * @param objectMap
     */
    public void hmset(String key, Map<String,Object>objectMap){

        redisTemplate.opsForHash().putAll(key,objectMap);
    }

    /**
     * 獲得 hash数据表中的 keyInfo对应的value值
     * @param key
     * @param keyInfo
     * @return
     */
    public Object hget(String key,String keyInfo){

        return  redisTemplate.opsForHash().get(key,keyInfo);
    }

    /**
     * 自增hash数据表中 object对象的value值 步长时num
     * @param key hash的key
     * @param o hash中的成员
     * @param num 步长 num>0 时自增 num<0 时自减
     * @return
     */
    public long hIncrBy(String key,Object o,long num){
        return redisTemplate.opsForHash().increment(key,o,num);
    }

    /***************************set数据结构操作**********************/
    /**
     * 为set集合中添加对象
     * @param key
     * @param value
     */
    public boolean sadd(String key,Object value) {

        Long add = redisTemplate.opsForSet().add(key, value);
        if(add>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 根据键查询redis set数据类型中的所有成员对象
     * @param key
     * @return
     */
    public Set<String> getSmembersByKey(String key){

        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 移除 set集合中 key中的item
     * @param key key
     * @param item item
     */
    public void remKeysItem(String key,String item){

        redisTemplate.opsForSet().remove(key,item);
    }

    /**
     * 判断 set数据集中 key中是否存在object 元素
     * @param key key
     * @param object 对象
     * @return
     */
    public boolean isMemberInSet(String key,Object object){

        return redisTemplate.opsForSet().isMember(key,object);
    }

    /**
     * 獲得set中的元素總量
     * @param key
     * @return
     */
    public long getCountIntSet(String key){

        return redisTemplate.opsForSet().size(key);
    }


    /****************************String数据结构操作******************/
    /**
     * 获得键中的数据
     * @param key
     * @return
     */
    public Object getStrByKey(String key){
       return  redisTemplate.opsForValue().get(key);
    }


    /**
     * 字符串数据中自增
     * @param key
     * @return
     */
    public long sIncrby(String key){
        return  redisTemplate.opsForValue().increment(key);
    }

    /**
     * 字符串自增 某個增量
     * @param key key
     * @param amount 增量
     * @return
     */
    public long sIncrby(String key,long amount){

        return redisTemplate.opsForValue().increment(amount);
    }

    /**
     * 字符串自減 某個減量
     * @param key
     * @param amount
     * @return
     */
    public long sDecyBy(String key,long amount){
        return redisTemplate.opsForValue().decrement(key,amount);
    }

    /*****************************事务操作****************************/

    /**
     * redis监视给定的redis键
     * @param key 键
     */
    public void watchKey(String key){

        redisTemplate.watch(key);
    }

    /**
     * 不监视键
     */
    public void unwatchKey(){
        redisTemplate.unwatch();
    }

    /**
     * multi之后，命令入队列
     */
    public void multi(){

        redisTemplate.multi();
    }

    public void exec(){
        redisTemplate.exec();
    }
}
