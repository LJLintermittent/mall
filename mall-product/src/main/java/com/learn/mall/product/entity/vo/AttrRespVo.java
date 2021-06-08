package com.learn.mall.product.entity.vo;

import lombok.Data;

/**
 * Description:
 * date: 2021/4/19 16:01
 * Package: com.learn.mall.product.entity.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class AttrRespVo extends AttrVo {
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
