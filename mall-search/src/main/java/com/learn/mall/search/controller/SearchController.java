package com.learn.mall.search.controller;

import com.learn.mall.search.service.MallSearchService;
import com.learn.mall.search.vo.SearchParamVo;
import com.learn.mall.search.vo.SearchResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 * date: 2021/4/28 21:25
 * Package: com.learn.mall.search.controller
 *
 * @author 李佳乐
 * @version 1.0
 */
@Api(tags = "检索模块")
@Controller
@SuppressWarnings("all")
public class SearchController {

    @Autowired
    private MallSearchService mallSearchService;

    /**
     * 根据页面传递来的参数，去ES检索商品
     * 自动将页面提交过来的所有请求查询参数封装成一个对象
     */
    @ApiOperation(value = "根据页面传递来的参数，去ES检索商品")
    @GetMapping("/list.html")
    public String listPage(SearchParamVo paramVo, Model model, HttpServletRequest request) {
        paramVo.set_queryString(request.getQueryString());
        SearchResponseVo result = mallSearchService.search(paramVo);
        model.addAttribute("result", result);
        return "list";
    }
}
