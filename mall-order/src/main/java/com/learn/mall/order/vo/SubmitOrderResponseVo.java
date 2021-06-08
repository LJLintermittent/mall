package com.learn.mall.order.vo;

import com.learn.mall.order.entity.OrderEntity;
import lombok.Data;

/**
 * Description:
 * date: 2021/5/17 13:24
 * Package: com.learn.mall.order.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class SubmitOrderResponseVo {

    private OrderEntity order;

    private Integer code;//0成功 非0错误

}
