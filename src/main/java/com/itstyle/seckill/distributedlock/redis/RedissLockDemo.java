package com.itstyle.seckill.distributedlock.redis;

import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/*
* redis分布式锁Demo
* */
public class RedissLockDemo {
    /*
    *
    * 可重入锁
    * */
    public void testReentrantLock(RedissonClient redisson){
        RLock lock=redisson.getLock("anyLock");
        try {
            //1.最常见的使用方法
            //lock.lock();
            //2.支持过期解锁功能，10秒钟后自动解锁，无需调用unlock方法手动解锁
            //lock.lock(10,TimeUtil.SECONDS);
            //3.尝试加锁，最多等待三秒，上锁以后10秒自动解锁
            boolean res=lock.tryLock(3,10, TimeUnit.SECONDS);
            if (res){
                //成功
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
          lock.unlock();
        }
    }

    /*
    *
    * Redisson分布式锁异步执行的相关方法
    * */
    public void testAsyncReentrantLock(RedissonClient redissonClient){
        RLock lock=redissonClient.getLock("anyLock");
        try {
            lock.lockAsync();
            lock.lockAsync(10,TimeUnit.SECONDS);
            Future<Boolean> res=lock.tryLockAsync(3,10,TimeUnit.SECONDS);
            if (res.get()){
                //
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /*
    *
    * 联锁
    * */
    public void testMuitiLock(RedissonClient redisson1,RedissonClient redisson2,RedissonClient redisson3){
        RLock lock1=redisson1.getLock("lock1");
        RLock lock2=redisson2.getLock("lock2");
        RLock lock3=redisson3.getLock("lock3");
        RedissonMultiLock lock=new RedissonMultiLock(lock1,lock2,lock3);
        try {
            //同时加锁，所有锁都上锁成功才算成功
            lock.lock();
            //尝试加锁，最多等待一百秒，上锁之后10秒自动解锁
            boolean res=lock.tryLock(100,10,TimeUnit.SECONDS);
            if (res){

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
