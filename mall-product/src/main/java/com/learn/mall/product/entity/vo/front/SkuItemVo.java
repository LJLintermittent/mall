package com.learn.mall.product.entity.vo.front;

/**
 * Description:
 * date: 2021/5/6 18:46
 * Package: com.learn.mall.product.entity.vo.front
 *
 * @author 李佳乐
 * @version 1.0
 */

import com.learn.mall.product.entity.SkuImagesEntity;
import com.learn.mall.product.entity.SkuInfoEntity;
import com.learn.mall.product.entity.SpuInfoDescEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 1.获取sku的基本信息 pms_sku_info
 * 2.获取sku的图片信息 pms_sku_images
 * 3.获取spu的销售属性组合
 * 4.获取spu的介绍
 * 5.获取spu的规格参数介绍
 */
@Data
public class SkuItemVo {

    SkuInfoEntity info;

    boolean hasStock = true;

    List<SkuImagesEntity> images;

    List<SkuItemSaleAttrVo> saleAttr;

    SpuInfoDescEntity descEntity;

    List<SpuItemAttrGroupVo> groupAttrs;

    SecKillInfoVo secKillInfoVo;

    @ToString
    @Data
    public static class SkuItemSaleAttrVo {
        private Long attrId;
        private String attrName;
        private List<AttrValueWithSkuIdVo> attrValues;
    }

    @ToString
    @Data
    public static class SpuItemAttrGroupVo {
        private String groupName;
        private List<SpuBaseAttrVo> attrs;
    }

    @ToString
    @Data
    public static class SpuBaseAttrVo {
        private String attrName;
        private String attrValue;
    }

}
