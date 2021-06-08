package com.learn.mall.ware.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * date: 2021/4/22 11:31
 * Package: com.learn.mall.ware.entity.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class MergeVo {
    private Long purchaseId;//采购单ID

    private List<Long> items;//采购需求项
}
