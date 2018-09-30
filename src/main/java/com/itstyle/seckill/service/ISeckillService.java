package com.itstyle.seckill.service;

import com.itstyle.seckill.pojo.Result;
import com.itstyle.seckill.pojo.Seckill;

import java.util.List;

public interface ISeckillService {
    /*查询全部的秒杀记录*/
    List<Seckill> getSeckillList();
    /*查询单个秒杀记录*/
    Seckill getById(Long seckillId);
    /*查询秒杀售卖商品库存*/
    Long  getSeckillCount(Long seckillId);
    /*删除秒杀售卖商品记录*/
    void deleteCount(Long seckillId);
    /*秒杀1*/
    Result startSeckill(Long seckillId,Long userId);

}