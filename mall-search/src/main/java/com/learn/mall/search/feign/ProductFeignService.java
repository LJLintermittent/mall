package com.learn.mall.search.feign;

import com.learn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Description:
 * date: 2021/5/5 23:20
 * Package: com.learn.mall.search.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@FeignClient("mall-product")
@SuppressWarnings("all")
public interface ProductFeignService {

    /**
     * Feign的执行流程：
     * 1.构造请求数据，将对象转为JSON，最终构造出一个模板请求
     * 在SynchronousMethodHandler.invoke()中调用：
     * RequestTemplate template = buildTemplateFromArgs.create(argv);
     * 2.发送请求进行执行(执行并解码响应数据)
     *     while (true) {
     *       try {
     *         return executeAndDecode(template);
     *       } catch (RetryableException e) {
     *         try {
     *           retryer.continueOrPropagate(e);
     * 可以看到执行失败会使用重试机制
     * 如果是default默认实现：
     *       if (attempt++ >= maxAttempts) {
     *         throw e;
     *       }
     * 会有一个最大重试次数，超了这个次数再抛出异常
     * public Default() {
     *       this(100, SECONDS.toMillis(1), 5);
     *     }
     * 可以看到在构造器中默认重试最大次数为5
     *
     * public class NeverRetry implements Retryer {
     *
     *     public static final NeverRetry INSTANCE = new NeverRetry();
     *
     *     public NeverRetry() {
     *
     *     }
     *
     *     public void continueOrPropagate(RetryableException e) {
     *         throw e;
     *     }
     * 如果使用NeverRetry的重试实现，那么它一次都不重试，直接抛异常
     */

    /**
     * 根据属性ID查询部分要求的属性内容(具体查哪些内容要根据业务构造)
     */
    @GetMapping("/product/attr/info/{attrId}")
    R attrInfo(@PathVariable("attrId") Long attrId);

    /**
     * 返回多个品牌信息
     */
    @GetMapping("/product/brand/infos")
    R brandsInfo(@RequestParam("brandIds") List<Long> brandIds);

}
