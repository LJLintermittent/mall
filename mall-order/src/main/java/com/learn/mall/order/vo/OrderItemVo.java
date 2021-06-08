package com.learn.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 * date: 2021/5/12 20:37
 * Package: com.learn.mall.order.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class OrderItemVo {
    private Long skuId;
    private String title;
    private String image;
    private List<String> skuAttr;
    private BigDecimal price;
    private Integer count;
    private BigDecimal totalPrice;
    //查询库存状态
    private BigDecimal weight;
}
