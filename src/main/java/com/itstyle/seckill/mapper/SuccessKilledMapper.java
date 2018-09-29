package com.itstyle.seckill.mapper;

import com.itstyle.seckill.pojo.Seckill;
import com.itstyle.seckill.pojo.SuccessKilled;
import com.itstyle.seckill.pojo.SuccessKilledKey;

public interface SuccessKilledMapper {

    int deleteSuccess(Long seckill_id);
    int deleteByPrimaryKey(SuccessKilledKey key);
    int insert(SuccessKilled record);
    int insertSelective(SuccessKilled record);
    SuccessKilled selectByPrimaryKey(SuccessKilledKey key);
    int updateByPrimaryKeySelective(SuccessKilled record);
    int updateByPrimaryKey(SuccessKilled record);
}