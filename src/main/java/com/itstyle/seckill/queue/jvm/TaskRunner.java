package com.itstyle.seckill.queue.jvm;

import com.itstyle.seckill.pojo.SuccessKilled;
import com.itstyle.seckill.service.ISeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/*
* 消费秒杀队列
*
* */
@Component
public class TaskRunner implements ApplicationRunner {

    @Autowired
    private ISeckillService seckillService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        while (true){
            //进程内队列
            SuccessKilled killed=SeckillQueue.getMailQueue().consume();
            if (killed!=null){
                seckillService.startSeckill(killed.getSeckillId(),killed.getUserId());
            }
        }
    }
}
