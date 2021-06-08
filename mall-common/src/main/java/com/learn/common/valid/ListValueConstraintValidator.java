package com.learn.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * Description:
 * date: 2021/4/14 16:44
 * Package: com.learn.common.valid
 *
 * @author 李佳乐
 * @version 1.0
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {

    private final Set<Integer> set = new HashSet<>();

    //初始化方法
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] values = constraintAnnotation.values();
        if (values.length > 0) {
            for (int value : values) {
                set.add(value);
            }
        }
    }

    /**
     * @param integer                    需要校验的值
     * @param constraintValidatorContext 校验的上下文环境信息
     * @return 判断是否校验成功
     */
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }
}
