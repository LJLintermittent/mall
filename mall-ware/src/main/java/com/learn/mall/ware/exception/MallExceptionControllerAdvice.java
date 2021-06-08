package com.learn.mall.ware.exception;

import com.learn.common.exception.StatusCodeEnum;
import com.learn.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Description:
 * date: 2021/4/22 23:37
 * Package: com.learn.mall.ware.exception
 *
 * @author 李佳乐
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.learn.mall.ware")
public class MallExceptionControllerAdvice {

    @ExceptionHandler(value = Throwable.class)
    public R handlerException(Throwable throwable) {
        log.error(throwable.getMessage());
        return R.error(StatusCodeEnum.UNKNOW_EXCEPTION.getCode(), StatusCodeEnum.UNKNOW_EXCEPTION.getMsg());
    }

    @ExceptionHandler(value = PurchaseDetailException.class)
    public R handlerException(PurchaseDetailException purchaseDetailException) {
        log.error(purchaseDetailException.getMsg());
        purchaseDetailException.printStackTrace();
        return R.error(purchaseDetailException.getCode(), purchaseDetailException.getMsg());
    }
}

