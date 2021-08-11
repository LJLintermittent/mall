/**
 * Copyright 2021 bejson.com
 */
package com.learn.mall.product.entity.vo;

import lombok.Data;

/**
 * Auto-generated: 2021-04-20 18:45:42
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class Attr {

    /**
     * VO:值对象或者视图对象
     * 作用是接收页面传递来的数据，进行处理，或者将业务处理完成的数据，封装成页面要使用的数据
     */
    private Long attrId;
    private String attrName;
    private String attrValue;

}