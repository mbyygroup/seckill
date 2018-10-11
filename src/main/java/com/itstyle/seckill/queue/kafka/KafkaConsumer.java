package com.itstyle.seckill.queue.kafka;

import com.itstyle.seckill.common.webSocket.WebSocketServer;
import com.itstyle.seckill.common.redis.RedisUtil;
import com.itstyle.seckill.pojo.Result;
import com.itstyle.seckill.service.ISeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/*消费者队列*/
@Component
public class KafkaConsumer {
    @Autowired
    private ISeckillService seckillService;

    private static RedisUtil redisUtil=new RedisUtil();
    /*
    *
    * 监听seckill主题，有消息就读取
    * */
    @KafkaListener(topics = {"seckill"})
    public void receiveMessage(String message){
        //收到通道消息后就执行秒杀操作
        String[] array=message.split(":");
        Result result=seckillService.startSeckill(Long.parseLong(array[0]),Long.parseLong(array[1]));
        if (result.equals(Result.ok())){
            WebSocketServer.sendInfo(array[0].toString(),"秒杀成功");
        }else {
            WebSocketServer.sendInfo(array[0].toString(),"秒杀失败");
            redisUtil.cacheValue(array[0],"ok");
        }
    }

}
