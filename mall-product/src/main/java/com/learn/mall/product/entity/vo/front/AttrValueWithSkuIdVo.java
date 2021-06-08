package com.learn.mall.product.entity.vo.front;

import lombok.Data;
import lombok.ToString;

/**
 * Description:
 * date: 2021/5/7 0:09
 * Package: com.learn.mall.product.entity.vo.front
 *
 * @author 李佳乐
 * @version 1.0
 */
@ToString
@Data
public class AttrValueWithSkuIdVo {
    private String attrValue;
    private String skuIds;
}
