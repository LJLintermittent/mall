package com.learn.mall.product.entity.vo;

import com.learn.mall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * date: 2021/4/20 16:42
 * Package: com.learn.mall.product.entity.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class AttrGroupWithAttrsVo {
    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;

}
