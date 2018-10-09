package com.itstyle.seckill.pojo;

import java.io.Serializable;
import java.util.Date;

public class SuccessKilled extends SuccessKilledKey implements Serializable {
    private static final long serialVersionUID = -8323988269420485565L;
    private Long state;

    private Date createTime;

    public Long getState() {
        return state;
    }

    public void setState(long state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}