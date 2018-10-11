数据库脚本在src/main/resource/sql下，启动前请自行导入
访问地址为http://localhost:8000/seckill/swagger-ui.html

## 秒杀api测试及结果分析
    秒杀一（超卖）         不涉及各种锁，因此存在多用户同时抢夺资源的情况
    秒杀二（超卖）         虽然有lock锁，但是因为事务提交和释放锁的顺序问题，所以也会超卖
    秒杀三（正常）         给予秒杀二场景修复，锁上移，事物提交后在释放锁，不会超卖
    秒杀四（正常）         基于悲观锁，查询加锁然后更新，因为使用了限流注解所以会少买，限流注解可以注释掉就正常了
    秒杀五（正常）         基于悲观锁，更新加锁并判断剩余数量
    秒杀六（正常）         基于乐观锁实现，先查询商品版本号，然后根据版本号更新，判断更新数量，少数用户抢购时会出现少买的情况
    秒杀七（少卖）         基于进程内队列实现，会出现少卖情况，也有较低概率丢失数据
    秒杀八（少卖）         基于Disruptor高校队列实现，也会导致少卖

## 项目截图
![在线监控](src/main/resources/img/control.png)
![主页面](src/main/resources/img/main.png)
![秒杀api](src/main/resources/img/seckill_api.png)
![秒杀测试](src/main/resources/img/seckill_test.png)

## 业务难点：瞬间高并发
规模：几百几千不成问题，如果是百万级，千万级，建议使用分布式集群
## 实现原理：
- 1.用户访问会首先经过一个高防ip，来防止ddos攻击
- 2.经过slb对多台云服务器进行流量分发，通过流量分发扩展应用系统对外的服务能力，通过消除单点故障提升应用系统的可用性。
- 3.针对slb价格和灵活性考虑，接入nginx做限流分发，保证后端服务的正常运行
- 4.后台秒杀基于redis或者Zookeeper分布式锁，Kifka或者Redis做消息队列，drds数据库中间件实现数据的读写分离
## 秒杀架构
![架构图](https://gitee.com/uploads/images/2018/0515/184617_c7e13059_87650.png "秒杀架构.png")

## 优化思路：
- 分流
- 限流
- 缓存
- 异步
- 主备容灾方案
- 对服务器部署模型进行优化，部分请求直接返回秒杀失败
## 前端优化：
活动开始前生成静态商品页面推送缓存和cdn,静态文件（js/css）请求推送至文件服务器和cdn
## 应用服务优化：
nginx最佳配置，tomcat连接池优化，数据库配置优化和数据库连接池优化

## 多线程使用
先创建私有化的静态线程池,然后在控制层方法中创建Runable对象,在接口
方法的实现内书写程序的逻辑代码

## 程序锁使用
相对于Synchronize，Lock在获取锁的操作上提供了阻塞、非阻塞可中断、超时设置等机制
service在程序中是默认单例的,并发下lock只有一个实例
这里使用lock来实现程序锁,预先在程序中创建一个lock对象,使用时调用lock()加锁
,最后调用unlock（）释放锁，因为这一过程可能发生错误，所以使用try是必要的
在test目录下做了两者性能的测试，可以做大致的性能参考

## aop
这里的实现方法是先写一个注解类,在另一个类中使用@Pointcut来定义切点，括号内写入
注解类的路径，被这个注解修饰的方法即为注解方法，在其他方法可以按照先后顺序使用
@Before()或者@AfterReturning来只用这些注解方法，也可以用 @Around，在使用了切点
的类上面加入@Aspect即可把这个类变为切面

## redis缓存操作
先定义一个redisTemplate对象,使用hasKey判断缓存是否存在,opsForValue来获取缓存,
delete移除.获取到的缓存是一个ValueOperations对象,使用get和set来取值赋值
因为对于缓存的操作不一定保证不会出错,有必要加try,异常类型为Throwable

## 悲观锁和乐观锁
悲观锁：在执行数据库操作的时候会加一把锁，事物提交后释放锁，如果有别的线程进行数
据库操作，会阻塞，如果一直占用其他线程就一直无法操作数据，
悲观锁：不会加锁, 但是更新的时候会进行判断, 判断跟原始的数据一不一样,返回受影响
行数, 如果不一样,他会返回受影响的行数为0, 如果一样,会返回受影响的具体数量,
使用：频繁写入使用悲观锁，频繁读取使用乐观锁

## 队列使用
生产者向通道发送消息,消费者收到消息后执行相应方法
这里有mq,redis,kafka,disruptor及java原生实现五种方式
这里提供网上搜索到的disruptor的使用链接
（http://www.importnew.com/27652.html）

这是kafka的链接（https://www.cnblogs.com/likehua/p/3999538.html）

## 部分算法参考
位于common文件夹内,对shell排序，快速排序，直接插入排序，直接选择排序，冒泡排序
堆排序，归并排序的执行速度做了比较，大致排序为：很快（快速排序，归并排序，堆排
序，shell排序），较快（直接插入排序），较慢（直接选择排序），很慢（冒泡排序）

## 代码不息，优化不止。我会对代码进行长期的优化和更新，融入更多新技术
    