package com.itstyle.seckill.mapper;

import com.itstyle.seckill.pojo.Seckill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SeckillMapper {
    Seckill getById(long seckillId);            //得到商品
    List<Seckill> getSeckillList();                 //得到全部商品列表
    long getSeckillNumber(long seckillId);               //检验库存
    long updateSeckillNumber(long seckillId);          //更新库存
    long reduceSeckillNumber(long seckillId);          //减少库存
    long getSeckillNumber_PLock(long seckillId);         //悲观锁检查库存
    long reduceSeckillNumber_OLock(@Param("number")long number,
         @Param("seckillId")long seckillId, @Param("version")long version);    //乐观锁更新库存
    long reduceSeckillNumber_PLock(long seckillId);         //悲观锁更改库存

}