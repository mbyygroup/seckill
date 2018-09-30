package com.itstyle.seckill.pojo;

import java.io.Serializable;

public class SuccessKilledKey implements Serializable {
    private static final long serialVersionUID = 6924521979901595914L;
    private Long seckillId;

    private Long userId;

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}