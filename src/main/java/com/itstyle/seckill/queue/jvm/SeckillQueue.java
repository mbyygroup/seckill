package com.itstyle.seckill.queue.jvm;

import com.itstyle.seckill.pojo.SuccessKilled;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
*
* 秒杀队列
* */
public class SeckillQueue {
    //队列大小
    static final int QUEUE_MAX_SIZE = 100;
    /*用于多线程间下单的队列*/
    static BlockingQueue<SuccessKilled> blockingQueue=new LinkedBlockingQueue<SuccessKilled>(QUEUE_MAX_SIZE);

    /*
    * 私有的默认构造子，保证外界无法直接实例化
    * */
    private SeckillQueue(){};

    /*
    * 类级的内部类
    * */
    private static class SingletonHolder{
        /*
        * 静态初始机器，用jvm来保证线程安全
        * */
        private static SeckillQueue queue=new SeckillQueue();
    }

    //单例队列
    public static SeckillQueue getMailQueue(){return SingletonHolder.queue;}

    /**
     * 生产入队
     * @param killed
     * @throws InterruptedException
     * add(e) 队列未满时，返回true；队列满则抛出IllegalStateException(“Queue full”)异常——AbstractQueue
     * put(e) 队列未满时，直接插入没有返回值；队列满时会阻塞等待，一直等到队列未满时再插入。
     * offer(e) 队列未满时，返回true；队列满时返回false。非阻塞立即返回。
     * offer(e, time, unit) 设定等待的时间，如果在指定时间内还不能往队列中插入数据则返回false，插入成功返回true。
     */
    public Boolean produce(SuccessKilled killed) throws InterruptedException {
        return blockingQueue.offer(killed);
    }

    /*
    *
    * 消费队列
    * */
    public SuccessKilled consume() throws InterruptedException {
        return blockingQueue.take();
    }

    //获取队列大小
    public int size(){return blockingQueue.size();}
}
