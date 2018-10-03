package com.itstyle.seckill.service.impl;

import com.itstyle.seckill.aop.ServiceLimit;
import com.itstyle.seckill.aop.Servicelock;
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
        try{
            lock.lock();
            Long number=seckillMapper.getSeckillNumber(seckillId);
            if(number>0){
                seckillMapper.reduceSeckillNumber(seckillId);
                SuccessKilled killed=new SuccessKilled();
                killed.setSeckillId(seckillId);
                killed.setUserId(userId);
                killed.setState(Byte.parseByte(number+""));
                killed.setCreateTime(new Timestamp(new Date().getTime()));
                successKilledMapper.insert(killed);
            }else{
                return Result.error(SeckillStatEnum.END);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return Result.ok(SeckillStatEnum.SUCCESS);
    }

    @Override
    @Servicelock
    @Transactional
    public Result startSeckillAopLock(Long seckillId, Long userId) {
        long num=seckillMapper.getSeckillNumber(seckillId);
        if (num>0){
            seckillMapper.reduceSeckillNumber(seckillId);
            SuccessKilled killed=new SuccessKilled();
            killed.setSeckillId(seckillId);
            killed.setUserId(userId);
            killed.setState(Byte.parseByte(num+""));
            killed.setCreateTime(new Timestamp(new Date().getTime()));
            successKilledMapper.insert(killed);
        }else {
            return Result.error(SeckillStatEnum.END);
        }
        return Result.ok(SeckillStatEnum.SUCCESS);
    }

    @Override
    @ServiceLimit
    @Transactional
    public Result startSeckillDBPCC_ONE(Long seckillId, Long userId) {
        //单用户抢购一件商品或者多件都没有问题
        long num=seckillMapper.getSeckillNumber(seckillId);
        if (num>0){
            seckillMapper.reduceSeckillNumber(seckillId);
            SuccessKilled killed=new SuccessKilled();
            killed.setSeckillId(seckillId);
            killed.setUserId(userId);
            killed.setState(Byte.parseByte(num+""));
            killed.setCreateTime(new Timestamp(new Date().getTime()));
            successKilledMapper.insert(killed);
            return Result.ok(SeckillStatEnum.SUCCESS);
        }else {
            return Result.error(SeckillStatEnum.END);
        }
    }

    /*
    * SHOW STATUS LIKE 'innodb_row_lock%';
    * 如果发现锁争用比较严重，如InnoDB_row_lock_waits和InnoDB_row_lock_time_avg的值比较高
    * */
    @Override
    @Transactional
    public Result startSeckillDBPCC_TWO(Long seckillId, Long userId) {
        //单用户抢购单个商品没有问题，但抢购多件商品可能有问题
        Long count= seckillMapper.reduceSeckillNumber(seckillId);
        if (count>0){
            SuccessKilled killed=new SuccessKilled();
            killed.setSeckillId(seckillId);
            killed.setUserId(userId);
            killed.setState(Byte.parseByte(0+""));
            killed.setCreateTime(new Timestamp(new Date().getTime()));
            successKilledMapper.insert(killed);
            return Result.ok(SeckillStatEnum.SUCCESS);
        }else {
            return Result.error(SeckillStatEnum.END);
        }
    }

    @Override
    @Transactional
    public Result startSeckillDBOCC(Long seckillId, Long userId, Long number) {
        Seckill seckill=seckillMapper.getById(seckillId);
        if (seckill.getNumber()>=number){   //剩余的数量应该大于秒杀的数量
            //乐观锁
            Long count=seckillMapper.reduceSeckillNumber(seckillId);
            if (count>0){
                SuccessKilled killed=new SuccessKilled();
                killed.setSeckillId(seckillId);
                killed.setUserId(userId);
                killed.setState(Byte.parseByte(0+""));
                killed.setCreateTime(new Timestamp(new Date().getTime()));
                successKilledMapper.insert(killed);
                return Result.ok(SeckillStatEnum.SUCCESS);
            }else {
                return Result.error(SeckillStatEnum.END);
            }
        }else {
            return Result.error(SeckillStatEnum.END);
        }
    }

    @Override
    public Result startSeckillTemplate(Long seckillId, Long userId, Long number) {
        return null;
    }


}
