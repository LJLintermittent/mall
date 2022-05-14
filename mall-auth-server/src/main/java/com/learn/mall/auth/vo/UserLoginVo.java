package com.learn.mall.auth.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * date: 2021/5/8 15:37
 * Package: com.learn.mall.auth.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@ApiModel(value = "认证模块-普通登陆实体类")
@Data
public class UserLoginVo {

    @ApiModelProperty(value = "登录帐户")
    private String loginAccount;

    @ApiModelProperty(value = "登录密码")
    private String password;
}
