package com.itstyle.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/*
* 通用访问拦截匹配
*
* */
@Controller
public class IndexController {
    /*
    * 页面跳转
    *
    * */
    @RequestMapping("{url}.shtml")
    public String page(@PathVariable("url") String url){return url;}

    /*
    * 页面跳转（二级目录）
    *
    * */
    @RequestMapping("{module}/{url}.shtml")
    public String page(@PathVariable("module") String moduce,@PathVariable("url") String url){
        return moduce+"/"+url;
    }
}
