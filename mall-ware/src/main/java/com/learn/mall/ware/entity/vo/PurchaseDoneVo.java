package com.learn.mall.ware.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Description:
 * date: 2021/4/22 14:40
 * Package: com.learn.mall.ware.entity.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class PurchaseDoneVo {
    @NotNull
    private Long id; //采购单ID

    private List<PurchaseItemsDoneVo> items;
}
