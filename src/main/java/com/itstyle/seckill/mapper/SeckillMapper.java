package com.itstyle.seckill.mapper;

import com.itstyle.seckill.pojo.Seckill;

import java.util.List;

public interface SeckillMapper {
    Seckill getById(Long seckillId);            //得到商品
    List<Seckill> getSeckillList();                 //得到全部商品列表
    Long getSeckillCount(Long seckillId);               //得到某个商品出现次数
//    Long deleteSeckill(long seckillId);              //删除某个商品
    Long getSeckillNumber(Long seckillId);               //检验库存
    Long updateSeckillNumber(Long seckillId);          //更新库存
    Long reduceSeckillNumber(Long seckillId);          //减少库存

//    int insert(Seckill record);
//    int insertSelective(Seckill record);
//    int updateByPrimaryKeySelective(Seckill record);
//    int updateByPrimaryKey(Seckill record);


}