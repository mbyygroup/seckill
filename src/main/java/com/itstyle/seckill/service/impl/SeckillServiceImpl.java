package com.itstyle.seckill.service.impl;

import com.itstyle.seckill.mapper.SeckillMapper;
import com.itstyle.seckill.mapper.SuccessKilledMapper;
import com.itstyle.seckill.pojo.Result;
import com.itstyle.seckill.pojo.Seckill;
import com.itstyle.seckill.pojo.SeckillStatEnum;
import com.itstyle.seckill.pojo.SuccessKilled;
import com.itstyle.seckill.service.ISeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service("seckillService")
public class SeckillServiceImpl implements ISeckillService {

    /*
    *
    * 创建互斥锁
    * */
    private Lock lock=new ReentrantLock(true);

    @Autowired
    private SuccessKilledMapper successKilledMapper;
    @Autowired
    private SeckillMapper seckillMapper;

    @Override
    public List<Seckill> getSeckillList() {
        return seckillMapper.getSeckillList();
    }

    @Override
    public Seckill getById(Long seckillId) {
        return seckillMapper.getById(seckillId);
    }


    @Override
    public Long getSeckillCount(Long seckillId) {
        return seckillMapper.getSeckillCount(seckillId);
    }

    @Override
    public void deleteCount(Long seckillId) {
        successKilledMapper.deleteSuccess(seckillId);
        seckillMapper.updateSeckillNumber(seckillId);
    }


    @Override
    @Transactional
    public Result startSeckill(Long seckillId, Long userId) {
        //校验库存
        Long number=seckillMapper.getSeckillNumber(seckillId);
        if(number>0){
            //扣库存
            seckillMapper.reduceSeckillNumber(seckillId);
            //创建订单
            SuccessKilled killed=new SuccessKilled();
            killed.setSeckillId(seckillId);
            killed.setUserId(userId);
            killed.setState((byte) 0);
            killed.setCreateTime(new Timestamp(new Date().getTime()));
            successKilledMapper.insert(killed);
            //支付
            return Result.ok(SeckillStatEnum.SUCCESS);
        }else {
            return Result.error(SeckillStatEnum.END);
        }
    }

    @Override
    @Transactional
    public Result startSeckillLock(Long seckillId, Long userId) {

        return Result.ok(SeckillStatEnum.SUCCESS);
    }
}
