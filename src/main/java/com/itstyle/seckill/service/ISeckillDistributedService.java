package com.itstyle.seckill.service;

import com.itstyle.seckill.pojo.Result;

public interface ISeckillDistributedService {
    /*
    * 秒杀单个产品
    * @Param seckillId 秒杀商品ID
    * @Param userId 用户ID
    * */
    Result startSeckilRedisLock(long seckillId,long userId);
    Result startSeckilZkLock(long seckillId,long userId);

    /*
    *
    * 秒杀多个商品
    * */
    Result startSeckillLock(long seckillId,long userId,long number);
}
