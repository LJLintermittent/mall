package com.learn.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * date: 2021/4/20 21:46
 * Package: com.learn.common.to
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class SpuBoundsTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
