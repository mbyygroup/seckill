package com.itstyle.seckill.service.impl;

import com.itstyle.seckill.pojo.Result;
import com.itstyle.seckill.service.ISeckillDistributedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeckillDistributedServiceImpl implements ISeckillDistributedService {
    @Override
    @Transactional
    public Result startSeckilRedisLock(Long seckillId, Long userId) {
        boolean res=false;
        return null;
    }

    @Override
    public Result startSeckilZkLock(Long seckillId, Long userId) {
        return null;
    }
}
