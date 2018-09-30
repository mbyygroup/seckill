package com.itstyle.seckill.service.impl;

import com.itstyle.seckill.mapper.SeckillMapper;
import com.itstyle.seckill.pojo.Result;
import com.itstyle.seckill.pojo.Seckill;
import com.itstyle.seckill.service.ICreateHtmlService;
import com.mysql.cj.exceptions.ExceptionFactory;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.rmi.server.ExportException;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class CreateHtmlServiceImpl implements ICreateHtmlService {

    private static int corePoolSize=Runtime.getRuntime().availableProcessors();
    //多线程生成静态页面
    private static ThreadPoolExecutor executor=new ThreadPoolExecutor(corePoolSize,corePoolSize+1,101, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(1000));

    @Autowired
    public Configuration configuration;
    @Autowired
    public SeckillMapper seckillMapper;
    @Value("D://file//")
    private String path;

    @Override
    public Result createAllHtml() {
        List<Seckill> list=seckillMapper.getSeckillList();
        final List<Future<String>> resultList=new ArrayList<Future<String>>();
        for (Seckill seckill:list){
            resultList.add(executor.submit(new createhtml(seckill)));
        }
        for (Future<String> fs:resultList){
            try {
                System.out.println(fs.get());//打印各个线任务执行的结果，调用future.get() 阻塞主线程，获取异步任务的返回结果
            }catch (InterruptedException e){
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return Result.ok();
    }
    class createhtml implements Callable<String> {
        Seckill seckill;

        public createhtml(Seckill seckill){this.seckill=seckill;}

        @Override
        public String call() throws Exception {
            Template template=configuration.getTemplate("goods.flt");
            File file=new File(path+seckill.getSeckillId()+".html");
            Writer writer=new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
            template.process(seckill,writer);
            return "success";
        }
    }
}
