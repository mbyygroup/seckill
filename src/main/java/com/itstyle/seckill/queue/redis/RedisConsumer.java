package com.itstyle.seckill.queue.redis;

import com.itstyle.seckill.controller.config.RedisUtil;
import com.itstyle.seckill.controller.config.WebSocketServer;
import com.itstyle.seckill.pojo.Result;
import com.itstyle.seckill.pojo.SeckillStatEnum;
import com.itstyle.seckill.service.ISeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
*
* 消费者
* */
@Service
public class RedisConsumer {
    @Autowired
    private ISeckillService seckillService;
    @Autowired
    private RedisUtil redisUtil;

    public void receiveMessage(String message){
        //收到通道消息后执行秒杀操作
        String[] array=message.split(":");
        Result result=seckillService.startSeckillDBPCC_TWO(Long.parseLong(array[0]),Long.parseLong(array[1]));
        if (result.equals(Result.ok(SeckillStatEnum.SUCCESS))){
            WebSocketServer.sendInfo(array[0].toString(),"秒杀成功");  //推送到前台
        }else {
            WebSocketServer.sendInfo(array[0].toString(),"秒杀失败");
            redisUtil.cacheValue(array[0],"ok"); //秒杀结束
        }
    }
}
