package com.learn.mall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.learn.common.constant.ProductConstant;
import com.learn.common.to.SkuHasStockVo;
import com.learn.common.to.SkuReductionTo;
import com.learn.common.to.SpuBoundsTo;
import com.learn.common.to.es.SkuEsModel;
import com.learn.common.utils.R;
import com.learn.mall.product.entity.*;
import com.learn.mall.product.entity.vo.*;
import com.learn.mall.product.feign.CouponFeignService;
import com.learn.mall.product.feign.SearchFeignService;
import com.learn.mall.product.feign.WareFeignService;
import com.learn.mall.product.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.Query;

import com.learn.mall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sun.security.tools.KeyStoreUtil;

@SuppressWarnings("all")
@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WareFeignService wareFeignService;

    @Autowired
    private SearchFeignService searchFeignService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 保存提交的SpuInfoVo信息
     * 1.保存SPU的基本信息 涉及表 pms_spu_info
     * 2.保存SPU的描述图片 涉及表 pms_spu_info_desc
     * 3.保存SPU的图片集   涉及表 pms_spu_images
     * 4.保存SPU的规格参数 涉及表 pms_product_attr_value
     * 5.保存SPU的积分信息 涉及表 sms_spu_bounds
     * 6.保存当前SPU对应的所有SKU信息
     * 6.1.SKU的基本信息 涉及表 pms_sku_info
     * 6.2 SKU的图片信息 涉及表 pms_sku_images
     * 6.3 SKU的销售属性信息 涉及表 pms_sku_sale_attr_value
     * 6.4 SKU的优惠，满减信息 涉及表 sms_sku_ladder(满几件打几折信息表)
     * sms_sku_full_reduction(满多少钱减多少信息表)
     * sms_member_price(会员价格表)
     *
     * @param spuSaveVo 前端提交表单的SpuInfoVo的具体内容
     * @GlobalTransactional 后台管理服务，提交一些增删改查数据，不要求超高并发
     * 可以使用 seata AT模式做分布式事务
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {
        //1.保存SPU的基本信息 涉及表 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
//        spuInfoEntity.setCreateTime(new Date());
//        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);
        //2.保存SPU的描述图片 涉及表 pms_spu_info_desc
        List<String> descriptionImages = spuSaveVo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",", descriptionImages));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);
        //3.保存SPU的图片集   涉及表 pms_spu_images
        List<String> images = spuSaveVo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(), images);
        //4.保存SPU的规格参数 涉及表 pms_product_attr_value
        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map((attrs) -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrId(attrs.getAttrId());
            AttrEntity id = attrService.getById(attrs.getAttrId());
            productAttrValueEntity.setAttrName(id.getAttrName());
            productAttrValueEntity.setQuickShow(attrs.getShowDesc());
            productAttrValueEntity.setAttrValue(attrs.getAttrValues());
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttrValue(collect);
        //5.保存SPU的积分信息 涉及表 sms_spu_bounds
        Bounds bounds = spuSaveVo.getBounds();
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(bounds, spuBoundsTo);
        spuBoundsTo.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundsTo);
        if (r.getCode() != 0) {
            log.error("远程服务保存spu积分信息失败");
        }
        //6.保存当前SPU对应的所有SKU信息
        //6.1.SKU的基本信息 涉及表 pms_sku_info
        List<Skus> skus = spuSaveVo.getSkus();
        if (skus != null && skus.size() > 0) {
            skus.forEach(item -> {
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg() == 1) {
                        defaultImg = image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item, skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfoEntity);
                //6.2 SKU的图片信息 涉及表 pms_sku_images
                Long skuId = skuInfoEntity.getSkuId();
                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map((img) -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter((entity) -> {
                    //返回true就是需要这个值，返回false就过滤掉
                    //没有图片路径的无需保存
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                skuImagesService.saveBatch(imagesEntities);
                //6.3 SKU的销售属性信息 涉及表 pms_sku_sale_attr_value
                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map((a) -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);
                //6.4 SKU的优惠，满减信息 涉及表 sms_sku_ladder(满几件打几折信息表)
                //                             sms_sku_full_reduction(满多少钱减多少钱信息表)
                //                             sms_member_price(会员购买此spu对应的会员价格表)
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item, skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount() > 0
                        || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) > 0) {
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (r1.getCode() != 0) {
                        log.error("远程保存sku优惠信息失败");
                    }
                }
            });
        }
    }

    /**
     * 保存基本的SPU信息，涉及表：pms_spu_info
     */
    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    /**
     * 分页条件查询
     *
     * @param params 查询条件和分页参数
     * @return 数据
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((queryWrapper) -> {
                queryWrapper.eq("id", key).or().like("spu_name", key);
            });
        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("publish_status", status);
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId)) {
            wrapper.eq("brand_id", brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId)) {
            wrapper.eq("catalog_id", catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    /**
     * 根据要上架的SpuId查出与之对应的所有的Sku信息
     *
     * @param spuId 要上架的商品ID
     */
    @Transactional
    @Override
    public void up(Long spuId) {
        List<SkuInfoEntity> skus = skuInfoService.getSkuInfoBySpuId(spuId);
        List<Long> skuIdList = skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        //查出当前Sku的所有可以被用来检索的规格属性
        List<ProductAttrValueEntity> baseAttrs = productAttrValueService.queryBaseAttrListForSpu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        List<Long> CanSearchAttrIds = attrService.selectCanSearchAttrIds(attrIds);
        Set<Long> idSet = new HashSet<>(CanSearchAttrIds);
        List<SkuEsModel.Attrs> attrList = baseAttrs.stream().filter((item) -> {
            return idSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs);
            return attrs;
        }).collect(Collectors.toList());
        //远程库存服务，查询是否有库存
        Map<Long, Boolean> stockMap = null;
        try {
            R wareFeignResult = wareFeignService.getSkuHasStock(skuIdList);
            TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<List<SkuHasStockVo>>() {
            };
            stockMap = wareFeignResult.getData(typeReference).stream()
                    .collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));
        } catch (Exception e) {
            log.error("远程库存查询异常,原因: " + e);
        }
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> skuEsModels = skus.stream().map(sku -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, skuEsModel);
            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());
            //设置是否有库存
            if (finalStockMap == null) {
                skuEsModel.setHasStock(false);
            } else {
                skuEsModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }
            //热度评分
            skuEsModel.setHotScore(0L);
            //查询品牌和分类的名字
            BrandEntity brandEntity = brandService.getById(skuEsModel.getBrandId());
            skuEsModel.setBrandName(brandEntity.getName());
            skuEsModel.setBrandImg(brandEntity.getLogo());
            CategoryEntity categoryEntity = categoryService.getById(skuEsModel.getCatalogId());
            skuEsModel.setCatalogName(categoryEntity.getName());
            //设置检索属性
            skuEsModel.setAttrs(attrList);

            return skuEsModel;
        }).collect(Collectors.toList());
        //将这个封装好的数据模型发送给ES进行保存
        R searchFeignResult = searchFeignService.productStatusUp(skuEsModels);
        if (searchFeignResult.getCode() == 0) {
            //远程调用成功,更新这个商品状态为已上架
//            baseMapper.updateSpuUpStatus(spuId, ProductConstant.ProductUpStatusEnum.SPU_UP.getCode());
            this.updateSpuUpStatus(spuId, ProductConstant.ProductUpStatusEnum.SPU_UP.getCode());
        } else {
            //远程调用失败
            //TODO 重复调用问题，接口幂等性 重复机制
            /**
             * feign的调用流程：
             * 1.构造请求数据,将对象转为json
             * 2.发送请求并执行(执行成功会解码响应数据)
             * 3.执行请求会有重试机制
             */
        }
    }

    /**
     * 更新商品上架状态
     */
    @Override
    public void updateSpuUpStatus(Long spuId, int code) {
        UpdateWrapper<SpuInfoEntity> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", spuId);
        wrapper.set("publish_status", code);
        wrapper.set("update_time", new Date());
        this.update(wrapper);
    }

    /**
     * 根据skuId查出spu的信息
     */
    @Override
    public SpuInfoEntity getSpuInfoBySkuId(Long skuId) {
        SkuInfoEntity skuInfoEntity = skuInfoService.getById(skuId);
        Long spuId = skuInfoEntity.getSpuId();
        SpuInfoEntity spuInfoEntity = baseMapper.selectById(spuId);
        return spuInfoEntity;
    }

}