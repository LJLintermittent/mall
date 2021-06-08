package com.learn.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 * date: 2021/4/20 22:07
 * Package: com.learn.common.to
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;

}
