package com.itstyle.seckill.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
* 同步锁 AOP
*
* */
@Component
@Scope
@Aspect
@Order(1)         //order越小越先执行
public class LockAspect {
    private static Lock lock=new ReentrantLock(true);  //互斥锁

    //Service层切入点 用于记录错误日志
    @Pointcut("@annotation(com.itstyle.seckill.aop.Servicelock)")
    public void lockAspect(){ }

    @Around("lockAspect()")
    public Object around(ProceedingJoinPoint joinPoint){
        lock.lock();
        Object obj=null;
        try {
            obj=joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }finally {
            lock.unlock();
        }
        return obj;
    }

}
