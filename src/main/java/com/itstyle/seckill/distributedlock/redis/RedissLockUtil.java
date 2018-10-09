package com.itstyle.seckill.distributedlock.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/*
*
* redis分布式锁帮助类
* */
public class RedissLockUtil {
    private static RedissonClient redissonClient;

    public void setRedissonClient(RedissonClient locker){
        redissonClient=locker;
    }

    /*
    *
    * 加锁
    * */
    public static RLock lock(String lockKey){
        RLock lock=redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /*
    *
    * 释放锁
    * */
    public static void unlock(String lockKey){
        RLock lock=redissonClient.getLock(lockKey);
        lock.unlock();
    }

    public static void unlock(RLock lock){
        lock.unlock();
    }

    /*
    *
    * 带超时的锁
    * */
    public static RLock lock(String lockKey,int timeout){
        RLock lock=redissonClient.getLock(lockKey);
        lock.lock(timeout, TimeUnit.SECONDS);
        return lock;
    }

    public static RLock lock(String lockKey,TimeUnit unit,int timeout){
        RLock lock=redissonClient.getLock(lockKey);
        lock.lock(timeout,unit);
        return lock;
    }

    /*
    *
    * 尝试获取锁
    * */
    public static boolean tryLock(String lockKey,int waitTime,int leaseTime){
        RLock lock=redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime,leaseTime,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static boolean tryLock(String lockKey,TimeUnit unit,int waitTime,int leaseTime){
        RLock lock=redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime,leaseTime,unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

}
