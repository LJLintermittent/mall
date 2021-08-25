package com.learn.mall.seckill.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * Description:
 * date: 2021/5/23 20:31
 * Package: com.learn.mall.seckill.scheduled
 *
 * @author 李佳乐
 * @version 1.0
 */

/**
 * 定时任务
 * 1、@EnableScheduling 开启定时任务
 * 2、@Scheduled  开启一个定时任务
 * 3、自动配置类 TaskSchedulingAutoConfiguration
 * <p>
 * 异步任务
 * 1、@EnableAsync 开启异步任务功能
 * 2、@Async 给希望异步执行的方法上标注
 * 3、自动配置类 TaskExecutionAutoConfiguration 属性绑定在TaskExecutionProperties
 */
@EnableAsync
@EnableScheduling
@Component
@Slf4j
@SuppressWarnings("all")
public class IndexSchedule {

    /**
     * 1、Spring中6位组成，不允许第7位的年
     * 2、在周几的位置，1-7代表周一到周日； MON-SUN
     * 3、定时任务不应该阻塞。默认是阻塞的
     * 1）、可以让业务运行以异步的方式，自己提交到线程池
     * CompletableFuture.runAsync(()->{
     * xxxxService.hello();
     * },executor);
     * 2）、支持定时任务线程池；设置 TaskSchedulingProperties；
     * spring.task.scheduling.pool.size=5
     * <p>
     * 3）、让定时任务异步执行
     * 异步任务；
     * <p>
     * 解决：使用异步+定时任务来完成定时任务不阻塞的功能；
     */

    /**
     *     @Async
     *     @Scheduled(cron = "* * * ? * 7")
     *     public void hello() throws InterruptedException {
     *         log.info("hello...");
     *         Thread.sleep(3000);
     *     }
     */

}
