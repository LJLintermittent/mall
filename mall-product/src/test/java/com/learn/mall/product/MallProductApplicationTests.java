package com.learn.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learn.mall.product.dao.AttrGroupDao;
import com.learn.mall.product.dao.SkuSaleAttrValueDao;
import com.learn.mall.product.entity.BrandEntity;
import com.learn.mall.product.entity.vo.front.SkuItemVo;
import com.learn.mall.product.service.BrandService;
import com.learn.mall.product.service.CategoryService;
import com.learn.mall.product.service.SkuSaleAttrValueService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@SuppressWarnings("all")
public class MallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    AttrGroupDao attrgroupdao;

    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Test
    public void testSQL(){
//        List<SkuItemVo.SpuItemAttrGroupVo> data = attrgroupdao.getAttrGroupWithAttrBySpuId(100L, 225L);
//        System.out.println(data);
        List<SkuItemVo.SkuItemSaleAttrVo> data = skuSaleAttrValueDao.getSaleAttrsBySpuId(6L);
        System.out.println(data);
    }

    @Test
    public void testRedisson() {
        System.out.println(redissonClient);

    }

    @Test
    public void testRedis() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        int i = 0;
        for (i = 0; i < 10; i++) {
            ops.set("hello--" + i, "world" + UUID.randomUUID().toString().substring(0, 5));
        }
        for (i = 0; i < 10; i++) {
            String s = ops.get("hello--" + i);
            System.out.println("hello--" + i + "||" + s);
        }
    }

    @Test
    public void testCatelogPath() {
        Long[] paths = categoryService.findCatelogPath(1465L);
        log.info("完整路径 {}" + Arrays.asList(paths));
    }

    @Test
    public void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("华为");
//        boolean save = brandService.save(brandEntity);
//        System.out.println(save);
//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("国产手机");
//        boolean update = brandService.updateById(brandEntity);
//        System.out.println(update);
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("brand_id", 1L);
        List<BrandEntity> list = brandService.list(wrapper);
        list.forEach(System.out::println);

    }

}
