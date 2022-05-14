package com.learn.mall.auth.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * Description:
 * date: 2021/5/7 21:46
 * Package: com.learn.mall.auth.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@ApiModel(value = "认证模块-普通用户注册实体类")
@Data
public class UserRegisterVo {

    @ApiModelProperty(value = "用户名")
    @NotEmpty(message = "用户名必须提交")
    @Length(min = 6, max = 18, message = "用户名必须是6-18位")
    private String username;

    @ApiModelProperty(value = "密码")
    @NotEmpty(message = "密码必须提交")
    @Length(min = 6, max = 18, message = "密码必须是6-18位")
    private String password;

    @ApiModelProperty(value = "手机号")
    @NotEmpty(message = "手机号必须填写")
    @Pattern(regexp = "^[1]([3-9])[0-9]{9}$", message = "手机号格式不正确")
    private String phone;

    @ApiModelProperty(value = "验证码")
    @NotEmpty(message = "验证码必须填写")
    private String code;

}
