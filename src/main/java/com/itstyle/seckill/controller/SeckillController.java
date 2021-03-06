package com.itstyle.seckill.controller;

import com.itstyle.seckill.pojo.Result;
import com.itstyle.seckill.pojo.SuccessKilled;
import com.itstyle.seckill.service.ISeckillService;
import com.itstyle.seckill.queue.jvm.SeckillQueue;
import com.itstyle.seckill.queue.disruptor.SeckillEvent;
import com.itstyle.seckill.queue.disruptor.DisruptorUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Api(tags = "秒杀")
@RestController
@RequestMapping("/seckill")
public class SeckillController {
    private final static Logger LOGGER = LoggerFactory.getLogger(SeckillController.class);

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    //创建线程池，调整队列数 拒绝服务
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, corePoolSize + 1, 101, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1000));

    @Autowired
    private ISeckillService seckillService;

    @GetMapping("/test")
    public String test() {
        return "成功";
    }

    @ApiOperation(value = "秒杀一开始", nickname = "版权所属：芦望阳")
    @PostMapping("/start")
    public Result start(long seckillId) {
        seckillService.deleteCount(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀一");
        for (int i = 0; i <= 1000; i++) {
            final long userId = i;
            Runnable tsk = new Runnable() {
                @Override
                public void run() {
                    Result result = seckillService.startSeckill(killId, userId);
                    if (result != null) {
                        LOGGER.info("用户{}{}", userId, result.get("msg"));
                    } else {
                        LOGGER.info("用户{}{}", userId, "哎呦喂，人也太多了，请稍后！");
                    }
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

    @ApiOperation(value = "秒杀二(程序锁)", nickname = "版权所属：芦望阳")
    @PostMapping("/startLock")
    public Result startLock(long seckillId) {
        seckillService.deleteCount(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀二（正常运行）");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    Result result = seckillService.startSeckillLock(killId, userId);
                    LOGGER.info("用户{}{}", userId, result.get("msg"));
                }
            };
            executor.execute(task);
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

    @ApiOperation(value = "秒杀三（AOP程序锁）", nickname = "版权所属：芦望阳")
    @PostMapping("/startAopLock")
    public Result startAopLock(long seckillId) {
        seckillService.deleteCount(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀三（正常）");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Result result = seckillService.startSeckillAopLock(killId, userId);
                    LOGGER.info("用户{}{}", userId, result.get("msg"));
                }
            };
            executor.execute(runnable);
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

    @ApiOperation(value = "秒杀四（数据库悲观锁）", nickname = "版权所有：芦望阳")
    @PostMapping("/startDBPCC_ONE")
    public Result startDBPCC_ONE(long seckillId) {
        seckillService.deleteCount(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀四（正常）");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    Result result = seckillService.startSeckillDBPCC_ONE(killId, userId);
                    LOGGER.info("用户:{}{}", userId, result.get("msg"));
                    //经过调试可以发现msg数据但是这里日志打印却出不来，先注释掉，
                    // 以后有时间解决
                }
            };
            executor.execute(task);
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

    @ApiOperation(value = "秒杀五（数据库悲观锁）", nickname = "版权所有：芦望阳")
    @PostMapping("/startDBPCC_TWO")
    public Result startDBPCC_TWO(long seckillId) {
        seckillService.deleteCount(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀五（正常，数据库锁最优实现）");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    Result result = seckillService.startSeckillDBPCC_TWO(killId, userId);
                    LOGGER.info("用户:{}{}", userId, result.get("msg"));
                }
            };
            executor.execute(task);
        }
        try {
            Thread.sleep(1000);
            Long seckillCount = seckillService.getSeckillCount(seckillId);
            LOGGER.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @ApiOperation(value = "秒杀六（数据库乐观锁）", nickname = "版权所有：芦望阳")
    @PostMapping("/startDBOCC")
    public Result startDBOCC(long seckillId) {
        seckillService.deleteCount(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀六（数据库乐观锁）");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    //这里使用的乐观锁、可以自定义抢购数量、如果配置的抢购人数比较少、比如120:100(人数:商品) 会出现少买的情况
                    //用户同时进入会出现更新失败的情况
                    Result result = seckillService.startSeckillDBOCC(killId, userId, 1);
                    LOGGER.info("用户:{}{}", userId, result.get("msg"));
                }
            };
            executor.execute(task);
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

    @ApiOperation(value = "秒杀七（进程内队列）",nickname ="版权所有：芦望阳" )
    @PostMapping("/startQueue")
    public Result startQueue(long seckillId){        //会少买很多
        seckillService.deleteCount(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀七（正常）");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                   SuccessKilled kill=new SuccessKilled();
                   kill.setUserId(userId);
                   kill.setSeckillId(killId);
                   try {
                       Boolean flag=SeckillQueue.getMailQueue().produce(kill);
                        if (flag){
                            LOGGER.info("用户:{}{}",kill.getUserId(),"秒杀成功");
                        }else {
                            LOGGER.info("用户:{}{}",userId,"秒杀失败");
                        }
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                }
            };
            executor.execute(task);
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

    @ApiOperation(value = "秒杀八（Disruptor队列）",nickname = "版权所有：芦望阳")
    @PostMapping("/startDisruptorQueue")
    public Result startDisruptorQueue(long seckillId){
        seckillService.deleteCount(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀八（正常）");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    SeckillEvent kill=new SeckillEvent();
                    kill.setUserId(userId);
                    kill.setSeckillId(killId);
                   DisruptorUtil.producer(kill);
                }
            };
            executor.execute(task);
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

}
