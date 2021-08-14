package com.learn.mall.product.web;

import com.learn.mall.product.entity.CategoryEntity;
import com.learn.mall.product.entity.vo.front.Catelog2Vo;
import com.learn.mall.product.service.CategoryService;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Description:
 * date: 2021/4/25 20:14
 * Package: com.learn.mall.product.web
 *
 * @author 李佳乐
 * @version 1.0
 */
@Controller
@SuppressWarnings("all")
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 进入商城首页的跳转，顺便查询所有的一级分类数据在首页一进来时显示
     */
    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        //查出所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categorys();
        model.addAttribute("categorys", categoryEntities);
        return "index";
    }

    /**
     * 查询二级分类和三级分类
     * 请求接口：localhost:10001/index/catalog.json 单压接口，不过网关和nginx
     * 最大堆内存512M，线程数1000，ramp-up：1s，循环次数：永远，样本：10000
     * 数据库不加索引:吞吐量为：104/sec
     * 数据库加索引:吞吐量为：111/sec
     * 由于数据量过少，加索引优化效果不是特别明显，然后尝试取消JVM参数设置，直接默认压测
     * 在之前的基础上，最大堆内存依然为512M，初始堆内存也为512M，线程数200，ramp-up：1s，循环次数：永远
     * 样本数：20000，吞吐量为：830/sec
     */
    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        Map<String, List<Catelog2Vo>> map = categoryService.getCatalogJson();
        return map;
    }

    /**
     * 测试Redisson分布式锁（测试接口，无实际意义）
     */
    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        RLock lock = redissonClient.getLock("my-lock");
        //看门狗机制：如果业务超长，锁会自动续期 默认加的锁是30s 阻塞等待锁
        //加锁业务只要完成，就不会给当前锁续期，即使不手动解锁，锁默认在30s后自动删除
        /**
         * 如果我们指定了锁的超时时间，就会发送给redis执行脚本，进行占锁，默认超时就是我们指定的时间
         * lock.lock(10, TimeUnit.SECONDS);
         *
         * 如果我们不指定锁的超时时间，就是用30s（lockWatchDogTimeOut 看门狗默认超时时间）
         * lock.lock();
         *
         * 只要占锁成功，就会启动一个定时任务（重新给锁设置过期时间，新的过期时间就是看门狗的默认时间）
         * internalLockLeaseTime / 3  三分之一看门狗时间后续一次期
         */

        lock.lock();
        try {
            System.out.println("加锁成功，执行业务" + Thread.currentThread().getId());
            Thread.sleep(15000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("释放锁" + Thread.currentThread().getId());
            lock.unlock();
        }
        return "hello";
    }

    /**
     * 加写锁，保证能读到最新的数据，修改期间，写锁就是一个排它锁（互斥锁） 读锁是一个共享锁
     * 写锁没释放读锁就必须等待
     * 读 + 读：相当于无锁状态，并发读，只会在redis中记录好，所有当前的读锁，他们都会同时加锁成功
     * 写 + 读：等待写锁释放
     * 写 + 写：阻塞
     * 读 + 写: 有读锁，写也需要等待
     * 只要有写的存在，都必须等待
     * ps:测试接口，无实际意义
     */
    @GetMapping("/write")
    @ResponseBody
    public String write() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        RLock rLock = lock.writeLock();
        rLock.lock();
        String s = "";
        try {
            s = UUID.randomUUID().toString().substring(0, 8);
            Thread.sleep(30000);
            stringRedisTemplate.opsForValue().set("writeValue", s);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return s;
    }

    /**
     * 分布式锁测试（测试接口，无实际意义）
     */
    @GetMapping("/read")
    @ResponseBody
    public String read() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        RLock rLock = lock.readLock();
        rLock.lock();
        String value = "";
        try {
            value = stringRedisTemplate.opsForValue().get("writeValue");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return value;
    }

    /**
     * 信号量也可以用来做分布式的限流（测试接口，无实际意义）
     */
    @GetMapping("/park")
    @ResponseBody
    public String park() throws InterruptedException {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.acquire();
        return "ok";
    }

    /**
     * 测试接口，无实际意义
     */
    @GetMapping("/go")
    @ResponseBody
    public String go() {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.release();
        return "ok";
    }

    /**
     * 分布式闭锁(测试接口，无实际意义)
     */
    @GetMapping("/lockDoor")
    @ResponseBody
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.trySetCount(5);
        door.await();
        return "全部完成";
    }

    /**
     * 测试接口，无实际意义
     */
    @GetMapping("/finish/{id}")
    @ResponseBody
    public String finish(@PathVariable("id") Long id) {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.countDown();
        return id + "完成了";
    }
}
