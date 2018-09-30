package com.itstyle.seckill.controller;

import com.itstyle.seckill.pojo.Result;
import com.itstyle.seckill.pojo.Seckill;
import com.itstyle.seckill.service.ISeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "秒杀商品")
@RestController
@RequestMapping("/seckillPage")
public class SeckillPageController {
    @Autowired
    private ISeckillService seckillService;

    @ApiOperation(value = "秒杀商品列表",nickname = "木本芽衣")
    @PostMapping("/list")
    public Result list(){
        //f返回json数据,前端vue迭代即可
        List<Seckill> list=seckillService.getSeckillList();
        return Result.ok(list);
    }

}
