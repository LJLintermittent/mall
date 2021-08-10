package com.learn.mall.product.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.mall.product.entity.CategoryEntity;
import com.learn.mall.product.service.CategoryService;
import com.learn.common.utils.R;


/**
 * 商品三级分类
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("product/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查出所有商品分类（三级分类），以树形结构显示
     * RequestMapping注解:
     * 如果不指定method参数，它的源码里也没有给method参数默认值，那么get，post等等restfulAPI都可以实现
     */
    @RequestMapping("/list/tree")
    public R list() {
        List<CategoryEntity> entities = categoryService.listWithTree();
        return R.ok().put("data", entities);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);
        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.save(category);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category) {
        categoryService.updateDetails(category);
        return R.ok();
    }

    /**
     * 批量修改拖拽后的分类的排序，层级，父节点id
     */
    @RequestMapping("/update/batch")
    public R updateBatch(@RequestBody CategoryEntity[] category) {
        categoryService.updateBatchById(Arrays.asList(category));
        return R.ok();
    }

    /**
     * 删除
     * TODO:@RequestBody与@RequestParam注解作用
     * 我们的接口如果使用Json字符串来传参，而不是 x-www-form-urlencoded 类型，因为key-value有局限性
     * key-value无法测试批量操作数据的接口
     * 这时候就需要使用@RequestBody，这个注解表示接收到的参数来自requestBody中，也就是请求体中，一般用来处理
     * content-type不是application/x-www-form-urlencoded编码格式的数据，比如application/json、application/xml等类型的数据
     * 对于application/json类型的数据而言，使用注解@RequestBody可以将body里面所有的json数据传到后端，后端再进行解析
     * 常用于POST请求，springmvc会将请求体中的json数据转换为对应的对象
     * 而@RequestParam注解：接收的参数来自requestHeader中，也就是请求头中。常用于GET请求
     * 比如说以下这种url，在接口中需要对参数String author，String type加上@RequestParam注解，表示来自url中的key-value
     * http://localhost:8081/spring-boot-study/novel/findByAuthorAndType?author=唐家三少&type=已完结
     * <p>
     * 如果使用的是x-www-form-urlencoded格式，在浏览器控制台的network中显示有form data部分
     * 而如果使用的是application/json格式，在浏览器控制台的network中显示有request payload部分
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds) {
        categoryService.removeMenusByIds(Arrays.asList(catIds));
        return R.ok();
    }

}
