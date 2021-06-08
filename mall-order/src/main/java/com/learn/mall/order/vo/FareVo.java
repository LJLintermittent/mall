package com.learn.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * date: 2021/5/17 14:20
 * Package: com.learn.mall.order.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class FareVo {

    private MemberAddressVo address;

    private BigDecimal fare;
}
