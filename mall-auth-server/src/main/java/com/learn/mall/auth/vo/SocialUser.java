package com.learn.mall.auth.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Description:
 * date: 2021/5/8 20:09
 * Package: com.learn.mall.auth.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@ApiModel(value = "认证模块-社交登录实体类")
@Data
public class SocialUser {

    private String access_token;

    private String remind_in;

    private Long expires_in;

    private String uid;

    private String isRealName;
}
