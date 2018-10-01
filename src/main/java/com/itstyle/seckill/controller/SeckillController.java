package com.itstyle.seckill.controller;

import com.itstyle.seckill.pojo.Result;
import com.itstyle.seckill.service.ISeckillService;
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
    @GetMapping("/start")
    public Result start(Long seckillId) {
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
        }
        try {
            Thread.sleep(10000);
            Long seckillCount = seckillService.getSeckillCount(seckillId);
            LOGGER.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @ApiOperation(value = "秒杀二", nickname = "版权所属：芦望阳")
    @GetMapping("/startLock")
    public Result startLock(Long seckillId) {
        seckillService.deleteCount(seckillId);
        final long killId = seckillId;
        LOGGER.info("开始秒杀二（正常运行）");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
//                    Result result = seckillService.startSeckill(killId, userId);
//                    LOGGER.info("用户{}{}", userId, result.get("msg"));
                }
            };
            executor.execute(task);
        }
        try {
            Thread.sleep(10000);
            Long seckillCount = seckillService.getSeckillCount(seckillId);
            LOGGER.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

}
