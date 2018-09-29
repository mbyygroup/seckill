package com.itstyle.seckill.service;

import com.itstyle.seckill.pojo.Result;

public interface ISeckillDistributedService {
    /*
    * 秒杀单个产品
    * @Param seckillId 秒杀商品ID
    * @Param userId 用户ID
    * */
    Result startSeckilRedisLock(Long seckillId,Long userId);
    Result startSeckilZkLock(Long seckillId,Long userId);
}
