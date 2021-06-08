package com.learn.common.to.mq;

import lombok.Data;


/**
 * Description:
 * date: 2021/5/18 22:21
 * Package: com.learn.common.to.mq
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class StockLockedTo {

    private Long id;//库存工作单的ID

    private StockDetailTo detail;//工作单详情


}
