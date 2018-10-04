package com.itstyle.seckill.queue.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/*
*
* 生产者
* */
@Service
public class RedisSender {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //向通道发送消息的方法
    public void sendChannelMess(String channel,String messgae ){
        stringRedisTemplate.convertAndSend(channel,messgae);
    }
}
