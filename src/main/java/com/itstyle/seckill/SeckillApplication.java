package com.itstyle.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.itstyle.seckill.mapper")
public class SeckillApplication {
    private final static Logger LOGGER= LoggerFactory.getLogger(SeckillApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
        LOGGER.info("项目启动");
    }
}
