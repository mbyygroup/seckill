package com.itstyle.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.itstyle.seckill.mapper")
public class SeckillApplication {
    /*
    * 1. 数据库乐观锁
    * 2. 基于redis分布式锁
    * 3. 基于zookeeper分布式锁
    * 4. redis订阅监听
    * 5. kafka消息队列
    * */
    private final static Logger LOGGER= LoggerFactory.getLogger(SeckillApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
        LOGGER.info("项目启动");
    }
}
