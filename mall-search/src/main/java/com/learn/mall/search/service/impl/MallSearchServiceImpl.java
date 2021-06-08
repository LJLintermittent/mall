package com.learn.mall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.learn.common.to.es.SkuEsModel;
import com.learn.common.utils.R;
import com.learn.mall.search.config.MallElasticSearchConfig;
import com.learn.mall.search.constant.EsConstant;
import com.learn.mall.search.feign.ProductFeignService;
import com.learn.mall.search.service.MallSearchService;
import com.learn.mall.search.vo.AttrResponseVo;
import com.learn.mall.search.vo.BrandVo;
import com.learn.mall.search.vo.SearchParamVo;
import com.learn.mall.search.vo.SearchResponseVo;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.print.DocFlavor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * date: 2021/4/29 14:17
 * Package: com.learn.mall.search.service.impl
 *
 * @author 李佳乐
 * @version 1.0
 */
@Service
public class MallSearchServiceImpl implements MallSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ProductFeignService productFeignService;

    /**
     * 动态构建出查询需要的DSL语句
     *
     * @param paramVo 检索的所有参数
     */
    @Override
    public SearchResponseVo search(SearchParamVo paramVo) {
        SearchResponseVo searchResponseVo = null;
        SearchRequest searchRequest = buildSearchRequest(paramVo);
        try {
            //执行检索请求
            SearchResponse response = restHighLevelClient.search(searchRequest, MallElasticSearchConfig.COMMON_OPTIONS);
            //分析响应数据，封装成需要的格式
            searchResponseVo = buildSearchResponseVo(response, paramVo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResponseVo;
    }

    /**
     * 构建检索请求
     * 模糊匹配，过滤（按照属性，分类，品牌，价格区间,库存）
     * 排序，分页，高亮，聚合分析
     */
    private SearchRequest buildSearchRequest(SearchParamVo paramVo) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        /**
         *查询：模糊匹配，过滤（按
         *照属性，分类，品牌，价格区间,库存）
         */
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(paramVo.getKeyword())) {
            //3.高亮
            HighlightBuilder builder = new HighlightBuilder();
            builder.field("skuTitle");
            builder.preTags("<b style='color:red'>");
            builder.postTags("</b>");
            sourceBuilder.highlighter(builder);
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", paramVo.getKeyword()));
        }
        if (paramVo.getCatalog3Id() != null) {
            boolQuery.filter(QueryBuilders.termQuery("catalogId", paramVo.getCatalog3Id()));
        }
        if (paramVo.getBrandId() != null && paramVo.getBrandId().size() > 0) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", paramVo.getBrandId()));
        }
        if (paramVo.getHasStock() != null) {
            boolQuery.filter(QueryBuilders.termQuery("hasStock", paramVo.getHasStock() == 1));
        }
        //按照价格区间
        if (!StringUtils.isEmpty(paramVo.getSkuPrice())) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] s = paramVo.getSkuPrice().split("_");
            //200_500  //  _600  // 1200_
            if (s.length == 2) {
                rangeQuery.gte(s[0]).lte(s[1]);
            } else if (s.length == 1) {
                if (paramVo.getSkuPrice().startsWith("_")) {
                    rangeQuery.lte(s[0]);
                }
                if (paramVo.getSkuPrice().endsWith("_")) {
                    rangeQuery.gte(s[0]);
                }
            }
            boolQuery.filter(rangeQuery);
        }
        //按照所有指定的属性进行查询
        if (paramVo.getAttrs() != null && paramVo.getAttrs().size() > 0) {
            for (String attrStr : paramVo.getAttrs()) {
                //attrs=1_5寸:8寸&attrs=2_8G:16G
                //1_5寸:8寸
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
                String[] s = attrStr.split("_");
                String attrId = s[0];//检索的属性的ID
                String[] attrValue = s[1].split(":");//检索的属性的值
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValue));
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            }
        }
        sourceBuilder.query(boolQuery);

        /**
         *排序，分页，高亮
         */
        //1.排序
        if (!StringUtils.isEmpty(paramVo.getSort())) {
            //sort = hotScore_asc/desc
            String sort = paramVo.getSort();
            String[] s = sort.split("_");
            SortOrder sortOrder = s[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;
            sourceBuilder.sort(s[0], sortOrder);
        }
        //2.分页
        // from = (pageNum - 1) * pageSize
        sourceBuilder.from((paramVo.getPageNum() - 1) * EsConstant.PRODUCT_PAGESIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);
        /**
         * 聚合分析
         */
        //1.品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);
        //1.1品牌聚合的子聚合
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName")).size(50);
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg")).size(50);
        sourceBuilder.aggregation(brand_agg);
        //2.分类聚合
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg");
        catalog_agg.field("catalogId").size(20);
        catalog_agg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        sourceBuilder.aggregation(catalog_agg);
        //3.属性聚合
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        attr_agg.subAggregation(attr_id_agg);
        sourceBuilder.aggregation(attr_agg);

        String s = sourceBuilder.toString();
        System.out.println("构建的DSL语句：" + s);
        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);
        return searchRequest;
    }

    /**
     * 构建结果数据
     */
    private SearchResponseVo buildSearchResponseVo(SearchResponse response, SearchParamVo paramVo) {
        SearchResponseVo searchResponseVo = new SearchResponseVo();
        //获取命中
        SearchHits hits = response.getHits();
        //得到属性的聚合信息
        List<SearchResponseVo.attrVo> attrVos = new ArrayList<>();
        ParsedNested attr_agg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attr_id_agg.getBuckets()) {
            SearchResponseVo.attrVo attrVo = new SearchResponseVo.attrVo();
            //得到属性的ID
            long attrId = bucket.getKeyAsNumber().longValue();
            //得到属性的名字
            String attrName = ((ParsedStringTerms) bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0)
                    .getKeyAsString();
            //得到属性的值
            List<String> attrValues = ((ParsedStringTerms) bucket.getAggregations().get("attr_value_agg")).getBuckets().stream()
                    .map(item -> {
                        String keyAsString = item.getKeyAsString();
                        return keyAsString;
                    }).collect(Collectors.toList());
            attrVo.setAttrId(attrId);
            attrVo.setAttrName(attrName);
            attrVo.setAttrValue(attrValues);
            attrVos.add(attrVo);
        }
        searchResponseVo.setAttrs(attrVos);
        //得到品牌的聚合信息
        ParsedLongTerms brand_agg = response.getAggregations().get("brand_agg");
        List<SearchResponseVo.brandVo> brandVos = new ArrayList<>();
        for (Terms.Bucket bucket : brand_agg.getBuckets()) {
            SearchResponseVo.brandVo brandVo = new SearchResponseVo.brandVo();
            //得到品牌的ID
            long brandId = bucket.getKeyAsNumber().longValue();
            //得到品牌的名字
            String brandName = ((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0)
                    .getKeyAsString();
            //得到品牌的图片
            String brandImg = ((ParsedStringTerms) bucket.getAggregations().get("brand_img_agg")).getBuckets().get(0)
                    .getKeyAsString();
            brandVo.setBrandId(brandId);
            brandVo.setBrandName(brandName);
            brandVo.setBrandImg(brandImg);
            brandVos.add(brandVo);
        }
        searchResponseVo.setBrands(brandVos);
        //得到分类的聚合信息
        ParsedLongTerms catalog_agg = response.getAggregations().get("catalog_agg");
        List<? extends Terms.Bucket> buckets = catalog_agg.getBuckets();
        List<SearchResponseVo.CatalogVo> catalogVos = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            SearchResponseVo.CatalogVo catalogVo = new SearchResponseVo.CatalogVo();
            //得到分类ID
            String keyAsString = bucket.getKeyAsString();
            catalogVo.setCatalogId(Long.parseLong(keyAsString));
            //得到分类名
            ParsedStringTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            String catalog_name = catalog_name_agg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogName(catalog_name);
            catalogVos.add(catalogVo);
        }
        searchResponseVo.setCatalogs(catalogVos);
        //获取源数据的JSON字符串,并转换为SkuEsModel实体类,返回查询到的所有商品
        List<SkuEsModel> esModels = new ArrayList<>();
        if (hits.getHits() != null && hits.getHits().length > 0) {
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel esModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                if (!StringUtils.isEmpty(paramVo.getKeyword())) {
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String string = skuTitle.getFragments()[0].string();
                    esModel.setSkuTitle(string);
                }
                esModels.add(esModel);
            }
        }
        searchResponseVo.setProducts(esModels);
        long total = hits.getTotalHits().value;
        searchResponseVo.setTotal(total);
        int totalPages = (int) total % EsConstant.PRODUCT_PAGESIZE == 0
                ? (int) total / EsConstant.PRODUCT_PAGESIZE : ((int) total / EsConstant.PRODUCT_PAGESIZE + 1);
        searchResponseVo.setTotalPages(totalPages);
        List<Integer> pageNavs = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNavs.add(i);
        }
        searchResponseVo.setPageNavs(pageNavs);
        searchResponseVo.setPageNum(paramVo.getPageNum());
        //属性的面包屑导航
        if (paramVo.getAttrs() != null && paramVo.getAttrs().size() > 0) {
            List<SearchResponseVo.NavVo> collect = paramVo.getAttrs().stream().map(attr -> {
                SearchResponseVo.NavVo navVo = new SearchResponseVo.NavVo();
                String[] s = attr.split("_");
                navVo.setNavValue(s[1]);
                R r = productFeignService.attrInfo(Long.parseLong(s[0]));
                searchResponseVo.getAttrIds().add(Long.parseLong(s[0]));
                if (r.getCode() == 0) {
                    AttrResponseVo data = r.getData("attr", new TypeReference<AttrResponseVo>() {
                    });
                    navVo.setNavName(data.getAttrName());
                } else {
                    navVo.setNavName(s[0]);
                }
                //取消了这个面包屑以后，我们要跳转到正确的地方，将请求地址的url里面的当前条件置为空
                //拿到所有的查询条件，去掉当前条件
                String replace = replaceQueryString(paramVo, attr, "attrs");
                navVo.setLink("http://search.mall.com/list.html?" + replace);
                return navVo;
            }).collect(Collectors.toList());
            searchResponseVo.setNavs(collect);
        }
        //品牌的面包屑导航
        if (paramVo.getBrandId() != null && paramVo.getBrandId().size() > 0) {
            List<SearchResponseVo.NavVo> navs = searchResponseVo.getNavs();
            SearchResponseVo.NavVo navVo = new SearchResponseVo.NavVo();
            navVo.setNavName("品牌");
            R r = productFeignService.brandsInfo(paramVo.getBrandId());
            if (r.getCode() == 0) {
                List<BrandVo> brand = r.getData("brand", new TypeReference<List<BrandVo>>() {
                });
                StringBuffer stringBuffer = new StringBuffer();
                String replace = "";
                for (BrandVo brandVo : brand) {
                    stringBuffer.append(brandVo.getName() + ";");
                    replace = replaceQueryString(paramVo, brandVo.getBrandId() + "", "brandId");
                }
                navVo.setNavValue(stringBuffer.toString());
                navVo.setLink("http://search.mall.com/list.html?" + replace);
            }
            navs.add(navVo);
        }
        return searchResponseVo;
    }


    private String replaceQueryString(SearchParamVo paramVo, String value, String key) {
        String encode = null;
        try {
            encode = URLEncoder.encode(value, "UTF-8");
            encode = encode.replace("+", "%20");//浏览器对空格编码和java的处理不一样
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String replace = paramVo.get_queryString().replace("&" + key + "=" + encode, "");
        return replace;
    }
}
