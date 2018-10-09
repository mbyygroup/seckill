package com.itstyle.seckill.mapper;

import com.itstyle.seckill.pojo.Seckill;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SeckillMapper {
    Seckill getById(Long seckillId);            //得到商品
    List<Seckill> getSeckillList();                 //得到全部商品列表
    Long getSeckillNumber(Long seckillId);               //检验库存
    Long updateSeckillNumber(Long seckillId);          //更新库存
    Long reduceSeckillNumber(Long seckillId);          //减少库存

}