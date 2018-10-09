package com.itstyle.seckill.mapper;

import com.itstyle.seckill.pojo.SuccessKilled;
import com.itstyle.seckill.pojo.SuccessKilledKey;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SuccessKilledMapper {
    int deleteSuccess(long seckill_id);
    int insert(SuccessKilled record);
    long getSeckillCount(long seckillId);               //得到某个商品出现次数

}