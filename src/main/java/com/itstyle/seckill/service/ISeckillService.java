package com.itstyle.seckill.service;

import com.itstyle.seckill.pojo.Result;
import com.itstyle.seckill.pojo.Seckill;

import java.util.List;

public interface ISeckillService {
    /*查询全部的秒杀记录*/
    List<Seckill> getSeckillList();

    /*查询单个秒杀记录*/
    Seckill getById(long seckillId);

    /*查询秒杀售卖商品库存*/
    long getSeckillCount(long seckillId);

    /*删除秒杀售卖商品记录*/
    void deleteCount(long seckillId);

    /*秒杀一*/
    Result startSeckill(long seckillId, long userId);

    /*秒杀二 程序锁*/
    Result startSeckillLock(long seckillId, long userId);

    /*秒杀二 程序锁AOP*/
    Result startSeckillAopLock(long seckillId, long userId);

    /*秒杀二 数据库悲观锁*/
    Result startSeckillDBPCC_ONE(long seckillId, long userId);

    /*秒杀三 数据库悲观锁*/
    Result startSeckillDBPCC_TWO(long seckillId, long userId);

    /*秒杀三 数据库乐观锁*/
    Result startSeckillDBOCC(long seckillId, long userId, long number);

    /*秒杀四 事物模板*/
    Result startSeckillTemplate(long seckillId, long userId, long number);
}
