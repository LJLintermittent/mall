package com.learn.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Description:
 * date: 2021/4/14 16:35
 * Package: com.learn.common.valid
 *
 * @author 李佳乐
 * @version 1.0
 */

/**
 * 自定义JSR303校验注解
 * 作用： 判断传进来的值是否是规定的值 （integer类型）
 *
 * @Constraint(validatedBy = {ListValueConstraintValidator.class}) 此处可以指定多个校验器进行传入不同泛型的校验
 */
@Documented
@Constraint(validatedBy = {ListValueConstraintValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListValue {

    String message() default "{com.learn.common.valid.ListValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] values() default {};

}
