package com.learn.mall.ware.entity.vo;

import lombok.Data;

/**
 * Description:
 * date: 2021/4/22 14:40
 * Package: com.learn.mall.ware.entity.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class PurchaseItemsDoneVo {
    private Long itemId;
    private Integer status;
    private String reason;
}
