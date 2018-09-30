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

@Api(tags = "秒杀")
@RestController
@RequestMapping("/seckill")
public class SeckillController {
    private final static Logger LOGGER = LoggerFactory.getLogger(SeckillController.class);

    @Autowired
    private ISeckillService seckillService;

    @GetMapping("/test")
    public String test() {
        return "成功";
    }

    @ApiOperation(value = "秒杀开始", nickname = "版权所属：芦望阳")
    @GetMapping("/start")
    public Result start(Long seckillId) {
        seckillService.deleteCount(seckillId);
        System.out.println("已删除秒杀商品记录");
        final long killId = seckillId;
        System.out.println("已获取商品id");
        LOGGER.info("开始秒杀");
        for (int i = 0; i <= 1000; i++) {
            System.out.println("进入循环了");
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


}
