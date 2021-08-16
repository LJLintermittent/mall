package com.learn.mall.search.vo;

import com.learn.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * date: 2021/4/29 14:47
 * Package: com.learn.mall.search.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
//封装从ES中查询到的所有数据，构建成一个对象给前端页面返回，前端可以拿到经过ResponseBody注解转换成的JSON格式的数据进行展示
public class SearchResponseVo {

    private List<SkuEsModel> products;//查询到的所有商品信息

    /**
     * 分页信息
     */
    private Integer pageNum;//当前页码
    private Long total;//总记录数
    private Integer totalPages;//总页码
    private List<Integer> pageNavs;

    /**
     * 当前查询到的结果，所有涉及到的所有品牌
     */
    private List<brandVo> brands;

    /**
     * 当前查询到的结果，所有涉及到的所有属性
     */
    private List<attrVo> attrs;

    /**
     * 当前查询到的结果，所有涉及到的分类信息，封装成了分类Vo，有分类id和分类名称
     */
    private List<CatalogVo> catalogs;

    /**
     * 面包屑导航数据
     */
    private List<NavVo> navs = new ArrayList<>();
    private List<Long> attrIds = new ArrayList<>();

    @Data
    public static class NavVo {
        private String navName;
        private String navValue;
        private String link;
    }

    @Data
    public static class brandVo {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    public static class attrVo {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }

    @Data
    public static class CatalogVo {
        private Long catalogId;
        private String catalogName;
    }


}
