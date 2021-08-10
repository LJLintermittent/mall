package com.learn.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.learn.common.valid.AddGroup;
import com.learn.common.valid.ListValue;
import com.learn.common.valid.UpdateGroup;
import com.learn.common.valid.UpdateStatusGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@Data
@TableName("pms_brand")
@SuppressWarnings("all")
public class BrandEntity implements Serializable {

    /**
     * TODO:JSR303校验
     * 1.给Bean添加校验注解,并自定义自己的message显示
     * 2.开启校验功能：在controller方法中需要校验的Bean参数上添加@valid注解
     * 效果是：校验错误以后会有默认的错误响应
     * 3.在参数后面紧跟 BindingResult，就可以获取到校验的结果
     * 但是上述方法的校验存在一些问题，比如每次都要在业务代码中编写收集校验的逻辑，非常麻烦(案例代码见:BrandController)
     * 解决： 统一全局异常处理
     * 使用方法：
     * 1.编写异常处理类，使用@RestControllerAdvice
     * 2.使用@ExceptionHandler标注在方法上，指定要拦截的异常类
     * <p>
     * 分组校验：
     * 1.使用@Validated注解标注在controller方法的参数前面（代替了@valid，因为这个是规范，功能有限）
     * 2.使用@Validated注解中的group属性指定校验分组，校验分组里面传的是空接口类型
     * 3.在JavaBean中的校验注解上指定groups属性的，填入空接口类型
     */

    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @NotNull(message = "修改的时候必须指定品牌id", groups = {UpdateGroup.class})
    @Null(message = "新增的时候不能指定品牌id", groups = AddGroup.class)
    @TableId
    private Long brandId;

    /**
     * 品牌名(字段不能为空且至少包含一个非空格字符)
     */
    @NotBlank(message = "品牌名必须提交", groups = {AddGroup.class, UpdateGroup.class})
    private String name;

    /**
     * 品牌logo地址
     */
    @NotBlank(groups = {AddGroup.class})
    @URL(message = "logo必须是一个合法的url地址", groups = {AddGroup.class, UpdateGroup.class})
    private String logo;

    /**
     * 介绍
     */
    private String descript;

    /**
     * 显示状态[0-不显示；1-显示]
     */
    @NotNull(groups = {AddGroup.class, UpdateStatusGroup.class})
    @ListValue(values = {0, 1}, groups = {AddGroup.class, UpdateStatusGroup.class})
    private Integer showStatus;

    /**
     * 检索首字母
     */
    @NotEmpty(groups = {AddGroup.class})
    @Pattern(regexp = "^[a-zA-Z]$", message = "检索首字符必须是一个a-z或者A-Z的一个字母"
            , groups = {AddGroup.class, UpdateGroup.class})
    private String firstLetter;

    /**
     * 排序
     */
    @NotNull(groups = {AddGroup.class})
    @Min(value = 0, message = "输入的排序字段必须是一个大于等于0的整数"
            , groups = {AddGroup.class, UpdateGroup.class})
    private Integer sort;

}
