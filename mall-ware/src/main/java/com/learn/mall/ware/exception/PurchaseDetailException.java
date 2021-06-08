package com.learn.mall.ware.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * date: 2021/4/23 0:14
 * Package: com.learn.mall.ware.exception
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDetailException extends RuntimeException{

    private Integer code;

    private String msg;

}
