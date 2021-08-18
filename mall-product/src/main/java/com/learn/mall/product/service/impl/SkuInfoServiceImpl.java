package com.learn.mall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.learn.common.utils.R;
import com.learn.mall.product.entity.SkuImagesEntity;
import com.learn.mall.product.entity.SpuInfoDescEntity;
import com.learn.mall.product.entity.vo.front.SecKillInfoVo;
import com.learn.mall.product.entity.vo.front.SkuItemVo;
import com.learn.mall.product.feign.SecKillFeignService;
import com.learn.mall.product.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.Query;

import com.learn.mall.product.dao.SkuInfoDao;
import com.learn.mall.product.entity.SkuInfoEntity;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
@SuppressWarnings("all")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private SecKillFeignService secKillFeignService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 保存SKU的基本信息
     */
    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    /**
     * 带条件的分页查询
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((queryWrapper) -> {
                queryWrapper.eq("sku_id", key).or().like("sku_name", key);
            });
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId)) {
            wrapper.eq("catalog_id", catelogId);
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId)) {
            wrapper.eq("brand_id", brandId);
        }
        String min = (String) params.get("min");
        if (!StringUtils.isEmpty(min)) {
            wrapper.ge("price", min);
        }
        String max = (String) params.get("max");
        if (!StringUtils.isEmpty(max)) {
            //假设前端提交有不对的值 转 bigDecimal可能会出错，所以tryCatch
            try {
                BigDecimal bigDecimal = new BigDecimal(max);
                if (bigDecimal.compareTo(new BigDecimal("0")) > 0) {
                    wrapper.le("price", max);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    /**
     * 根据spuID查询出所有的SKU
     */
    @Override
    public List<SkuInfoEntity> getSkuInfoBySpuId(Long spuId) {
        List<SkuInfoEntity> skuInfoEntities = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        return skuInfoEntities;
    }

    /**
     * 根据skuId获取前台需要的Vo实体封装好的数据(非异步版本)
     */
    public SkuItemVo searchSkuVoInfoBySkuIdOld(Long skuId) {
        SkuItemVo skuItemVo = new SkuItemVo();
        //1.获取sku的基本信息 pms_sku_info
        SkuInfoEntity skuInfoEntity = getById(skuId);
        skuItemVo.setInfo(skuInfoEntity);
        //第一步和第二步操作之间没有依赖关系,第三第四第五步需要依赖第一步返回的结果
        Long catalogId = skuInfoEntity.getCatalogId();
        Long spuId = skuInfoEntity.getSpuId();
        //2.获取sku的图片信息 pms_sku_images，skuid在这张表中不是主键，要加普通索引
        List<SkuImagesEntity> images = skuImagesService.getImagesBySkuId(skuId);
        skuItemVo.setImages(images);
        //3.获取spu的销售属性组合
        List<SkuItemVo.SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrsBySpuId(spuId);
        skuItemVo.setSaleAttr(saleAttrVos);
        //4.获取spu的介绍
        SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(spuId);
        skuItemVo.setDescEntity(spuInfoDescEntity);
        //5.获取spu的规格参数介绍
        List<SkuItemVo.SpuItemAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupWithAttrBySpuId(spuId, catalogId);
        skuItemVo.setGroupAttrs(attrGroupVos);
        return skuItemVo;
    }

    /**
     * 根据skuId获取前台需要的Vo实体封装好的数据(异步+线程池版本)
     */
    @Override
    public SkuItemVo searchSkuVoInfoBySkuId(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = new SkuItemVo();

        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            //1.获取sku的基本信息 pms_sku_info,这个结果需要被其他接口引用，所以使用supplyAsync，带返回值的编排
            SkuInfoEntity skuInfoEntity = getById(skuId);
            skuItemVo.setInfo(skuInfoEntity);
            return skuInfoEntity;
        }, threadPoolExecutor);

        CompletableFuture<Void> saleAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            // 获取spu的销售属性组合，thenAcceptAsync接收上一步的结果，但是自己不返回结果
            List<SkuItemVo.SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrsBySpuId(res.getSpuId());
            skuItemVo.setSaleAttr(saleAttrVos);
        }, threadPoolExecutor);

        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((res) -> {
            // 获取spu的介绍，thenAcceptAsync接收上一步的结果，但是自己不返回结果
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(res.getSpuId());
            skuItemVo.setDescEntity(spuInfoDescEntity);
        }, threadPoolExecutor);

        CompletableFuture<Void> baseAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            // 获取spu的规格参数，也就是分组名字和这个分组中对应的属性，这是规格参数属性，跟spu绑定
            // thenAcceptAsync接收上一步的结果，但是自己不返回结果
            List<SkuItemVo.SpuItemAttrGroupVo> attrGroupVos = attrGroupService
                    .getAttrGroupWithAttrBySpuId(res.getSpuId(), res.getCatalogId());
            skuItemVo.setGroupAttrs(attrGroupVos);
        }, threadPoolExecutor);

        CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
            // 获取sku的图片信息 pms_sku_images,runAsync传入runnable，无返回值
            List<SkuImagesEntity> images = skuImagesService.getImagesBySkuId(skuId);
            skuItemVo.setImages(images);
        }, threadPoolExecutor);

        CompletableFuture<Void> secKillFuture = CompletableFuture.runAsync(() -> {
            // 查询当前sku是否参与秒杀，runAsync传入runnable，无返回值
            R result = secKillFeignService.getSecKillSkuInfo(skuId);
            if (result.getCode() == 0) {
                SecKillInfoVo data = result.getData(new TypeReference<SecKillInfoVo>() {
                });
                skuItemVo.setSecKillInfoVo(data);
            }
        }, threadPoolExecutor);

        // 阻塞等待所有异步任务完成以后，统一返回最终结果
        CompletableFuture.allOf(saleAttrFuture, descFuture, baseAttrFuture, imageFuture, secKillFuture).get();
        return skuItemVo;
    }

}