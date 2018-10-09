package com.itstyle.seckill.mapper;

import com.itstyle.seckill.pojo.Seckill;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SeckillMapper {
    Seckill getById(long seckillId);            //得到商品
    List<Seckill> getSeckillList();                 //得到全部商品列表
    long getSeckillNumber(long seckillId);               //检验库存
    long updateSeckillNumber(long seckillId);          //更新库存
    long reduceSeckillNumber(long seckillId);          //减少库存

}