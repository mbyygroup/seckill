package com.itstyle.seckill.distributedlock.zookeeper;

import org.apache.curator.*;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
* 基于curator的zookeeper分布式锁
* 这里开启五个线程，每个线程获取锁的最大等待时间为5秒，这里模拟业务逻辑，延时4秒
* 开始执行main方法，通过ZooInspector监控/curator/lock下的节点
* */
public class CuratorUtil {
    private static String address="192.168.1.180:2181";

    public static void main(String[] args) {
        //重试策略：初试时间为1s重试3次
        RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);
        //通过工厂创建连接
        CuratorFramework clent= CuratorFrameworkFactory.newClient(address,retryPolicy);
        //开启连接
        clent.start();
        //分布式锁
        final InterProcessMutex mutex=new InterProcessMutex(clent,"/curator/lock");

        ExecutorService fixedThreadPool= Executors.newFixedThreadPool(5);

        for (int i=0;i<5;i++){
            fixedThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    boolean flag=false;
                    try {
                        //尝试获取锁，最多等待4秒
                        flag=mutex.acquire(5, TimeUnit.SECONDS);
                        Thread currentThread=Thread.currentThread();
                        if (flag){
                            System.out.println("线程"+currentThread.getId()+"获取锁成功");
                        }else {
                            System.out.println("线程"+currentThread.getId()+"获取锁失败");
                        }
                        //延时4秒
                        Thread.sleep(4000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        if (flag){
                            try {
                                mutex.release();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }

    }
}
