package com.learn.mall.ware.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * date: 2021/5/16 20:22
 * Package: com.learn.mall.ware.entity.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class FareVo {

    private MemberAddressVo address;

    private BigDecimal fare;

}
