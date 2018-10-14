package com.itstyle.seckill.distributedlock.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/*
*
* zookeeper分布式锁
* */
public class ZkLockUtil {
    public static String address="192.168.1.180:2181";

    public static CuratorFramework client;

    static {
        RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);
        client= CuratorFrameworkFactory.newClient(address,retryPolicy);
        client.start();
    }

    /*
    * 私有化构造方法
    * */
    private ZkLockUtil(){}

    /*
    * 创建静态内部类实现延迟加载
    * */
    private static class SingletonHolder {
        /*
        * 静态初始机器，由jvm保证线程安全
        * */
        private static InterProcessMutex mutex=new InterProcessMutex(client,"/curator/lock");
    }
    public static InterProcessMutex getMutex(){
        return SingletonHolder.mutex;
    }
    //获得锁
    public static boolean acquire(long time, TimeUnit unit){
        try {
            return getMutex().acquire(time,unit);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //释放锁
    public static void release(){
        try {
            getMutex().release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

