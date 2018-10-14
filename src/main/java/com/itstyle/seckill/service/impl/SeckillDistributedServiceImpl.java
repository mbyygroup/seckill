package com.itstyle.seckill.service.impl;

import com.itstyle.seckill.common.enums.SeckillStatEnum;
import com.itstyle.seckill.distributedlock.redis.RedissLockUtil;
import com.itstyle.seckill.distributedlock.zookeeper.ZkLockUtil;
import com.itstyle.seckill.mapper.SeckillMapper;
import com.itstyle.seckill.mapper.SuccessKilledMapper;
import com.itstyle.seckill.pojo.Result;
import com.itstyle.seckill.pojo.SuccessKilled;
import com.itstyle.seckill.service.ISeckillDistributedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
public class SeckillDistributedServiceImpl implements ISeckillDistributedService {

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private SuccessKilledMapper successKilledMapper;

    @Override
    @Transactional
    public Result startSeckilRedisLock(long seckillId, long userId) {
        boolean res=false;
        //尝试获取锁，最多等待3秒，上锁以后20秒自动解锁防止死锁
        try {
            res=RedissLockUtil.tryLock(seckillId+"", TimeUnit.SECONDS,3,20);
            if (res){
                long num=seckillMapper.getSeckillNumber(seckillId);
                if (num>0){
                    seckillMapper.reduceSeckillNumber(seckillId);
                    SuccessKilled killed=new SuccessKilled();
                    killed.setSeckillId(seckillId);
                    killed.setUserId(userId);
                    killed.setState(num);
                    successKilledMapper.insert(killed);
                    seckillMapper.reduceSeckillNumber_PLock(seckillId);
                }else {
                    return Result.error(SeckillStatEnum.END);
                }
            }else {
                return Result.error(SeckillStatEnum.MUCH);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (res){         //释放锁
                RedissLockUtil.unlock(seckillId+"");
            }
        }
        return Result.ok(SeckillStatEnum.SUCCESS);
    }

    @Override
    @Transactional
    public Result startSeckilZkLock(long seckillId, long userId) {
        boolean res=false;
        try {
            res= ZkLockUtil.acquire(3,TimeUnit.SECONDS);
            if (res){
                long number=seckillMapper.getSeckillNumber(seckillId);
                if (number>0){
                    SuccessKilled killed = new SuccessKilled();
                    killed.setSeckillId(seckillId);
                    killed.setUserId(userId);
                    killed.setState(0);
                    successKilledMapper.insert(killed);
                    seckillMapper.reduceSeckillNumber_PLock(seckillId);
                }else {
                    return Result.error(SeckillStatEnum.END);
                }
            }else {
                return Result.error(SeckillStatEnum.MUCH);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (res){
                ZkLockUtil.release();     //释放锁
            }
        }
        return Result.ok(SeckillStatEnum.SUCCESS);
    }

    @Override
    public Result startSeckillLock(long seckillId, long userId, long number) {
        return null;
    }
}
