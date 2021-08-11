package com.learn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.product.entity.AttrEntity;
import com.learn.mall.product.entity.vo.AttrGroupRelationVo;
import com.learn.mall.product.entity.vo.AttrRespVo;
import com.learn.mall.product.entity.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:38:20
 */
@SuppressWarnings("all")
public interface AttrService extends IService<AttrEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存属性的基本数据以及属性&属性分组关联关系表中的数据
     */
    void saveAttr(AttrVo attr);

    /**
     * 查询规格参数
     * 规格参数是基本属性，不是销售属性，注意属性类型
     */
    PageUtils queryPageAttrPage(Map<String, Object> params, Long catelogId, String type);

    /**
     * 查询属性详情，用于前端点击属性修改后的表单数据回显
     */
    AttrRespVo getAttrInfo(Long attrId);

    /**
     * 修改属性信息，在属性的回显表单中，做完修改后点击修改按钮，调用此接口
     */
    void updateAttr(AttrVo attr);

    /**
     * 获取当前分组关联的所有属性（并且这些属性都是基本属性，也就是规格参数）
     * 因为只有基本属性才会有分组进行关联，销售属性是不需要与分组关联的
     */
    List<AttrEntity> getRelationAttr(Long attrgroupId);

    /**
     * 删除属性与分组的关联关系
     */
    void deleteRelation(AttrGroupRelationVo[] vos);

    /**
     * 获取当前分组没有关联的所有属性，在分组中添加关联属性的时候，需要先查询
     * 那么必须获取没有关联的所有属性，才能开始关联
     */
    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    /**
     * 在指定的所有属性里面，挑出可以被检索的属性
     */
    List<Long> selectCanSearchAttrIds(List<Long> attrIds);

}

