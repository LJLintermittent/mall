package com.learn.mall.product.exception;

import com.learn.common.exception.StatusCodeEnum;
import com.learn.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * date: 2021/4/14 14:48
 * Package: com.learn.mall.product.exception
 *
 * @author 李佳乐
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.learn.mall.product.controller")
//这里由于异常信息我们都需要以JSON的形式返回出去，所以要加@ResponseBody注解，这里直接使用@RestControllerAdvice
public class MallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现异常{},异常类型{}" + e.getMessage(), e.getClass());
        BindingResult result = e.getBindingResult();
        Map<String, String> map = new HashMap<>(16);
        result.getFieldErrors().forEach((item) -> {
            map.put(item.getField(), item.getDefaultMessage());
        });
        return R.error(StatusCodeEnum.VALID_EXCEPTION.getCode()
                , StatusCodeEnum.VALID_EXCEPTION.getMsg()).put("data", map);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handlerException(Throwable throwable) {
        log.error(throwable.getMessage());
        return R.error(StatusCodeEnum.UNKNOW_EXCEPTION.getCode(), StatusCodeEnum.UNKNOW_EXCEPTION.getMsg());
    }

}
