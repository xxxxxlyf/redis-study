package com.redis_study;


import com.redis_study.service.CountService;
import com.redis_study.service.MarketService;
import com.redis_study.service.SeckillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.transform.Source;
import java.util.*;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisStudyApplicationTests {


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MarketService marketService;

    @Autowired
    private CountService countService;

    @Autowired
    private SeckillService seckillService;

    @Test
    public void contextLoads() {


//        //準備數據
//        Map<String,Object>user01=new HashMap<>();
//        user01.put("name","Frank");
//        user01.put("funds",42);
//        redisTemplate.opsForHash().putAll("user:01",user01);
//
//        Map<String,Object>user02=new HashMap<>();
//        user02.put("name","Bill");
//        user02.put("funds",129);
//        redisTemplate.opsForHash().putAll("user:02",user02);
//
//        redisTemplate.opsForSet().add("inventory:01","item1","item2","item3");
//        //判斷是否存在對象
//        Boolean member = redisTemplate.opsForSet().isMember("inventory:01", "item1");
//        System.out.println(member);
//
//        //獲得set中的所有對象
//        Set<String> members = redisTemplate.opsForSet().members("inventory:01");
//        for(String oneMember:members){
//
//            System.out.println(oneMember);
//        }
//
//        redisTemplate.opsForSet().add("inventory:02","itemA","itemB","item3");


//        redisTemplate.opsForZSet().add("market","item:07",90);
//        redisTemplate.opsForZSet().add("market","item:78",90);
//        redisTemplate.opsForZSet().add("market","item:04,",194);

//        redisTemplate.opsForZSet().remove("market","item:04,");
//        redisTemplate.opsForZSet().remove("market","item101");
//        redisTemplate.opsForZSet().add("market","item:04",56);

//        Set<String> market = redisTemplate.opsForZSet().reverseRange("market", 0, 200);
//        for (String item:market){
//            System.out.println(item);
//        }
//
//        //查詢用戶1
//        Map map = redisTemplate.opsForHash().entries("user:01");
//        Set<String> keySet = map.keySet();
//        Iterator iterator = keySet.iterator();
//        while (iterator.hasNext()){
//            String key=(String) iterator.next();
//            System.out.println(key+"===="+map.get(key));
//        }
//
//        //查詢所有鍵數據
//        Set<String> keys = redisTemplate.keys("*");
//        for(String key :keys){
//            System.out.println(key);
//        }

        Long item1 = redisTemplate.opsForSet().add("inventory:01", "item1");
        System.out.println(item1);
    }

    @Test
    public void text() {
       redisTemplate.opsForSet().add("article:1","user:1");
        Boolean member = redisTemplate.opsForSet().isMember("article:1", "user:1");
        //獲得總數
        long count=redisTemplate.opsForSet().size("article:1");
        System.out.println(count);
        System.out.println(member);
    }

    @Test
    public void test(){
        countService.update_counter("hits",1);
    }


    @Test
    public void testSeckill(){
        CountDownLatch latch=new CountDownLatch(100);
        //模仿100個下單請求
        for(int i=0;i<100;i++){
            Thread thread = new Thread(()->{
                try {
                    seckillService.makeAnOrder("1","1");
                } catch (Exception e) {
                    System.out.println(e.getMessage());

                }finally {
                    latch.countDown();
                }
            });

            //運行線程
            thread.start();

        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDecr(){
        String key="stock:1";
        Long aLong = redisTemplate.opsForValue().decrement(key);
        System.out.println(aLong);
    }
}
