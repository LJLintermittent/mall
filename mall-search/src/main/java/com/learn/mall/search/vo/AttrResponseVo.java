package com.learn.mall.search.vo;

import lombok.Data;

/**
 * Description:
 * date: 2021/5/5 23:28
 * Package: com.learn.mall.search.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class AttrResponseVo {
    private Long attrId;
    /**
     * 属性名
     */
    private String attrName;
    /**
     * 是否需要检索[0-不需要，1-需要]
     */
    private Integer searchType;
    /**
     * 值类型 0-为单个值 1-为多个值
     */
    private Integer valueType;
    /**
     * 属性图标
     */
    private String icon;
    /**
     * 可选值列表[用逗号分隔]
     */
    private String valueSelect;
    /**
     * 属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]
     */
    private Integer attrType;
    /**
     * 启用状态[0 - 禁用，1 - 启用]
     */
    private Long enable;
    /**
     * 所属分类
     */
    private Long catelogId;
    /**
     * 快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整
     */
    private Integer showDesc;

    private Long attrGroupId;

    /**
     * 所属分类名字 ：手机/数码/手机 (只有第三级分类)
     */
    private String catelogName;
    /**
     * 所属分组名字  ： 主体
     */
    private String groupName;

    /**
     * 分类级联名称 : 手机/数码/手机  （包含一级二级三级分类）
     */
    private Long[] catelogPath;

}
