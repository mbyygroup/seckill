package com.itstyle.seckill.controller;

import com.itstyle.seckill.common.redis.RedisUtil;
import com.itstyle.seckill.pojo.Result;
import com.itstyle.seckill.queue.activemq.ActiveMQSender;
import com.itstyle.seckill.queue.kafka.KafkaSender;
import com.itstyle.seckill.queue.redis.RedisSender;
import com.itstyle.seckill.service.ISeckillDistributedService;
import com.itstyle.seckill.service.ISeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Api(tags = "分布式秒杀")
@RestController
@RequestMapping("/seckillDistributed")
public class SeckillDistributed {
    private final static Logger LOGGER = LoggerFactory.getLogger(SeckillController.class);

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    //创建线程池，调整队列数 拒绝服务
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, corePoolSize + 1, 101, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1000));

    @Autowired
    private ISeckillService seckillService;
    @Autowired
    private ISeckillDistributedService seckillDistributedService;
    @Autowired
    private RedisSender redisSender;
    @Autowired
    private KafkaSender kafkaSender;
    @Autowired
    private ActiveMQSender activeMQSender;
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "秒杀一（redis分布式锁）",nickname = "版权所属：芦望阳")
    @PostMapping("/startRedisLock")
    public Result startRedisLock(long seckillId){
        seckillService.deleteCount(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀一");
        for (int i = 0; i <= 1000; i++) {
            final long userId = i;
            Runnable tsk = new Runnable() {
                @Override
                public void run() {
                    Result result=seckillDistributedService.startSeckilRedisLock(killId,userId);
                    LOGGER.info("用户:{}{}",userId,result.get("msg"));
                }
            };
            executor.execute(tsk);
        }
        try {
            Thread.sleep(1000);
            long seckillCount = seckillService.getSeckillCount(seckillId);
            LOGGER.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @ApiOperation(value = "秒杀二（zookeeper分布式锁）",nickname = "版权所属：芦望阳")
    @PostMapping("/startZkLock")
    public Result startZkLock(long seckillId){
        seckillService.deleteCount(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀二");
        for (int i = 0; i <= 1000; i++) {
            final long userId = i;
            Runnable tsk = new Runnable() {
                @Override
                public void run() {
                    Result result=seckillDistributedService.startSeckilZkLock(killId,userId);
                    LOGGER.info("用户:{}{}",userId,result.get("msg"));
                }
            };
            executor.execute(tsk);
        }
        try {
            Thread.sleep(1000);
            long seckillCount = seckillService.getSeckillCount(seckillId);
            LOGGER.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @ApiOperation(value = "秒杀五(ActiveMQ分布式队列)", nickname = "版权所属：芦望阳")
    @PostMapping("/startActiveMQQueue")
    public Result start(long seckillId) {
        seckillService.deleteCount(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀五");
        for (int i = 0; i <= 1000; i++) {
            final long userId = i;
            Runnable tsk = new Runnable() {
                @Override
                public void run() {
                    if (redisUtil.getValue(killId+"")==null){
                        Destination destination=new ActiveMQQueue("seckill.queue");
                        activeMQSender.sendChannelMess(destination,killId+";"+userId);
                        }else {
                        //秒杀结束
                    }
                }
            };
            executor.execute(tsk);
        }
        try {
            Thread.sleep(1000);
            redisUtil.cacheValue(killId+"",null);
            long seckillCount = seckillService.getSeckillCount(seckillId);
            LOGGER.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }
}
