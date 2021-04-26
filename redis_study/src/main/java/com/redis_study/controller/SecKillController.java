package com.redis_study.controller;

import com.redis_study.model.res.ReturnMessageModel;
import com.redis_study.service.SeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/api/")
@Api(description = "商品秒殺")
@SuppressWarnings({"all"})
public class SecKillController {


    @Autowired
    private SeckillService seckillService;


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private Redisson redisson;

    private static final String STOCK_PRX = "stock:";


    /**
     * 線程安全的map,標識商品是否完全售完的標記，當確認賣完後，直接return。減少了JVM與redis通信的花費的網絡開銷。
     */
    private static ConcurrentHashMap<Long, Boolean> map = new ConcurrentHashMap<>();


    /**
     * 獲得商品是否賣完的標記
     *
     * @return
     */
    public static ConcurrentHashMap<Long, Boolean> getSoldOutMap() {
        return map;
    }

    @ApiOperation("用戶對商品下單")
    @GetMapping("/makeAnOrder")
    public ReturnMessageModel<String> makeAnOrder(String productId, String userId) throws Exception {

        if (map.get(Long.parseLong(productId)) != null) {
            System.out.println("商品庫存不足.無法下單");
            return new ReturnMessageModel<String>(1, "商品庫存不足，無法下單", "");
        }

        //請求經過redis緩存，提高並發性
        String key = STOCK_PRX + productId;
        Long decrement = redisTemplate.opsForValue().decrement(key);
        if (decrement < 0) {
            //標記當前商品已經賣完
            map.put(Long.parseLong(productId), true);
            redisTemplate.opsForValue().increment(key);
            return new ReturnMessageModel<String>(1, "商品庫存不足，無法下單", "");
        }
        try {
            return seckillService.makeAnOrder(productId, userId);
        } catch (Exception e) {
            //redis庫存加一,確保緩存與數據庫的數據的一致性，原子操作
            redisTemplate.opsForValue().increment(key);
            if (map.get(Long.parseLong(productId)) != null) {
                //清除售完標誌位
                map.remove(Long.parseLong(productId));
            }
            return new ReturnMessageModel<String>(1, "商品庫存不足，無法下單", "");
        }

    }


    @ApiOperation("商品库存问题，JVM级别")
    @DeleteMapping("/decrProductStock1")
    public ReturnMessageModel<String> decrProductStock1(String productId) {

        String key = STOCK_PRX + productId;
        //同步代码块，避免多线程下的商品超卖问题
        //问题一，分布式环境下，synchronized关键字只在一个进程JVM中有效，锁失效，搭建的集群任然会出现数据不一致的问题。
        synchronized (this) {
            //获得商品的库存
            int stock = (int) redisTemplate.opsForValue().get(key);
            if (stock <= 0) {
                return new ReturnMessageModel<String>(1, "商品库存不足", "");
            } else {
                //商品库存减一
                stock -= 1;
                //新的库存信息写入redis
                redisTemplate.opsForValue().set(key, stock);
                return new ReturnMessageModel<String>(0, "剩余库存", stock + "");
            }
        }
    }

    @ApiOperation("商品库存问题,redis简易分布式锁的实现")
    @DeleteMapping("/decrProductStock2")
    public ReturnMessageModel<String> decrProductStock2(String productId) {

        //每个线程独占的客户id值，确保锁操作的唯一性。解决分布式架构高并发下的超卖问题
        String clientId = UUID.randomUUID().toString();
        String lockKey = "lock";
        String key = STOCK_PRX + productId;

        //设置分布式锁,为了解决分布式环境下的数据不一致性，需要使用分布式锁，是方案一的简易解决方式
        //Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, "lock");
        //为了避免分布式环境下，出现服务宕机的情形下，导致的锁资源无法释放的问题，可以设置redis简易分布式锁的过期时间,设置过期时间为10s钟。在出现服务宕机的情形
        //当前锁的资源占有的时间为10s钟,可能出现的问题就是 上锁和设置的超时时间的命令存在被打断的可能。因为底层是非原子性的操作。为了解决这个问题使用原子性操作命令
        //redisTemplate.expire(lockKey,10, TimeUnit.SECONDS);


        //可能出现的问题。分布式架构在高并发的环境下，由于锁资源的失效时间设置不当，会导致锁资源永久失效的情形，为了解决加锁和释放锁资源是同一个线程操作的。需要进行一个验证
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 10, TimeUnit.SECONDS);
        if (!lock) {

            return new ReturnMessageModel<String>(1, "error code,系统繁忙，请稍后再试", "");
        }
        //获得商品的库存(获得锁之后处理业务逻辑)
        int stock = (int) redisTemplate.opsForValue().get(key);
        try {
            if (stock <= 0) {
                return new ReturnMessageModel<String>(1, "商品库存不足", "");
            } else {
                //商品库存减一
                stock -= 1;
                //新的库存信息写入redis
                redisTemplate.opsForValue().set(key, stock);

            }

        } finally {
            //处理业务逻辑之后，释放锁资源，为了避免资源在业务处理代码块中出现异常导致锁资源的无法释放，使用try finally代码块。保证不会因为
            //异常未被处理的原因而导致锁的资源无法释放
            //判断是同一个线程操作资源，需要验证为线程设定的客户id值
            if (clientId.equals(redisTemplate.opsForValue().get(lockKey))) {
                redisTemplate.delete(lockKey);
            }

        }

        return new ReturnMessageModel<String>(0, "剩余库存:" + stock, stock + "");

    }


    @ApiOperation("商品库存问题,分布式框架Redisson实现分布式锁")
    @DeleteMapping("/decrProductStock3")
    public ReturnMessageModel<String> decrProductStock3(String productId) {

        //当前方案可以解决分布式架构下高并发下由于锁延时导致的数据不一致性。但是涉及到redis主从结构下，主redis的数据未及时缓存到从redis上时，任然会出现数据不一致的问题
        //为了确保数据的高度一致性。可以采用Zookeeper来实现分布式锁，需要学习。前提下是zookeeper牺牲了一定的高可用性（Availability）

        String lockKey = "lock";
        String key = STOCK_PRX + productId;
        int stock = (int) redisTemplate.opsForValue().get(key);
        //获得锁
        RLock lock = redisson.getLock(lockKey);
        //设置锁资源，等价于Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 10, TimeUnit.SECONDS);
        lock.lock(10,TimeUnit.SECONDS);
        try {
            //获得商品的库存(获得锁之后处理业务逻辑)
            stock = (int) redisTemplate.opsForValue().get(key);

            if (stock <= 0) {
                return new ReturnMessageModel<String>(1, "商品库存不足", "");
            } else {
                //商品库存减一
                stock -= 1;
                //新的库存信息写入redis
                redisTemplate.opsForValue().set(key, stock);
            }
        }finally {
            //释放锁
            lock.unlock();
        }
        return new ReturnMessageModel<String>(0, "剩余库存:" + stock, stock + "");

    }


    @ApiOperation("商品库存问题,分布式框架Redisson实现分布式锁，分段加锁，提升高并发的性能")
    @DeleteMapping("/decrProductStock3")
    public ReturnMessageModel<String> decrProductStock4(String productId) {

        //当前方案可以解决分布式架构下高并发下由于锁延时导致的数据不一致性。但是涉及到redis主从结构下，主redis的数据未及时缓存到从redis上时，任然会出现数据不一致的问题
        //为了确保数据的高度一致性。可以采用Zookeeper来实现分布式锁，需要学习。前提下是zookeeper牺牲了一定的高可用性（Availability）

        String lockKey = "lock";
        String key = STOCK_PRX + productId;
        int stock = (int) redisTemplate.opsForValue().get(key);
        //获得锁
        RLock lock = redisson.getLock(lockKey);
        //设置锁资源，等价于Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 10, TimeUnit.SECONDS);
        lock.lock(10,TimeUnit.SECONDS);
        try {
            //获得商品的库存(获得锁之后处理业务逻辑)
            stock = (int) redisTemplate.opsForValue().get(key);

            if (stock <= 0) {
                return new ReturnMessageModel<String>(1, "商品库存不足", "");
            } else {
                //商品库存减一
                stock -= 1;
                //新的库存信息写入redis
                redisTemplate.opsForValue().set(key, stock);
            }
        }finally {
            //释放锁
            lock.unlock();
        }
        return new ReturnMessageModel<String>(0, "剩余库存:" + stock, stock + "");

    }
}
