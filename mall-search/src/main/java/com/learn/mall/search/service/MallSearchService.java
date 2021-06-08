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
     * @param paramVo 检索的所有参数
     * @return 检索的返回结果
     */
    SearchResponseVo search(SearchParamVo paramVo);

}

