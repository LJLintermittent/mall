package com.learn.mall.product.config;


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * date: 2021/9/23 23:07
 * Package: com.learn.mall.product.config
 *
 * @author 李佳乐
 * @email 18066550996@163.com
 */
@SuppressWarnings("all")
public class NamingThreadFactory implements ThreadFactory {

    /**
     * 线程命名工厂，只是为了更改业务线程池创建出来的线程的名字
     */
    private final AtomicInteger threadNum = new AtomicInteger();

    private final ThreadFactory proxy;

    private final String name;

    public NamingThreadFactory(ThreadFactory proxy, String name) {
        this.proxy = proxy;
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = proxy.newThread(r);
        t.setName(name + "[#" + threadNum.incrementAndGet() + "]");
        return t;
    }
}
