package com.learn.mall.search.vo;

/**
 * Description:
 * date: 2021/4/29 14:14
 * Package: com.learn.mall.search.vo
 *
 * @author 李佳乐
 * @version 1.0
 */

import lombok.Data;

import java.util.List;

/**
 * 封装页面所有可能传过来的关键字
 */
@Data
public class SearchParamVo {

    private String keyword;//页面传递进来的全文匹配关键字
    private Long catalog3Id;//三级分类ID

    /**
     * sort = saleCount_asc/desc
     * sort = skuPrice_asc/desc
     * sort = hotScore_asc/desc
     */
    private String sort;//排序条件
    /**
     * 过滤条件
     */
    private Integer hasStock;//是否只显示有货商品  0 无库存，1 有库存
    private String skuPrice;//sku价格区间查询
    private List<Long> brandId;//按照品牌进行查询，可以多选
    private List<String> attrs;//按照属性进行筛选
    private Integer pageNum = 1;//页码

    private String _queryString;//原生的所有查询条件

}
