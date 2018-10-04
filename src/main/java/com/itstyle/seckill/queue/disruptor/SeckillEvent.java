package com.itstyle.seckill.queue.disruptor;

import java.io.Serializable;

/*
*
* 事物对象(秒杀事件)
* */
public class SeckillEvent implements Serializable {

    private static final long serialVersionUID = 6702462984129362428L;
    private long seckillId;
    private long userId;

    public SeckillEvent(){}

    public long getSeckillId(){return seckillId;}

    public void setSeckillId(long seckillId){
        this.seckillId=seckillId;
    }

    public long getUserId() {return userId;}

    public void setUserId(long userId){
        this.userId=userId;
    }
}
