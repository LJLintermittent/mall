package com.learn.mall.search.service;

import com.learn.mall.search.vo.SearchParamVo;
import com.learn.mall.search.vo.SearchResponseVo;

/**
 * Description:
 * date: 2021/4/29 14:16
 * Package: com.learn.mall.search.service
 *
 * @author 李佳乐
 * @version 1.0
 */
public interface MallSearchService {

    /**
     * 商品检索，传入已经封装好的所有检索参数，调用API来构建成ES需要的DSL语句
     */
    SearchResponseVo search(SearchParamVo paramVo);

}

