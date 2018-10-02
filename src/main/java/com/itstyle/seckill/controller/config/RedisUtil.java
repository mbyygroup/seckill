package com.itstyle.seckill.controller.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/*
*
* 缓存工具类
* */
@Component
public class RedisUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(RedisUtil.class);

    @Resource
    private RedisTemplate<Serializable,Serializable> redisTemplate;

    /*
    *
    * 前缀
    * */
    public static final String KEY_PREFIX_VALUE="itstyle:seckill:value:";

    /*
    *
    * 缓存value操作
    * */
    public boolean cacheValue(String k,Serializable v,Long time){
        String key=KEY_PREFIX_VALUE+k;
        try {
            ValueOperations<Serializable,Serializable> valueOps=redisTemplate.opsForValue();
            valueOps.set(key,v);
            if (time>0) redisTemplate.expire(key,time, TimeUnit.SECONDS);
            return true;
        }catch (Throwable t){
            LOGGER.error("缓存[{}]失败，value[{}]",key,v,t);
        }
        return false;
    }



}
