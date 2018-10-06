package com.itstyle.seckill.mapper;

import com.itstyle.seckill.pojo.SuccessKilled;
import com.itstyle.seckill.pojo.SuccessKilledKey;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SuccessKilledMapper {
    int deleteSuccess(Long seckill_id);
    int insert(SuccessKilled record);

}