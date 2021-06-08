package com.learn.mall.product.entity.vo.front;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 * date: 2021/4/25 21:04
 * Package: com.learn.mall.product.entity.vo
 *
 * @author 李佳乐
 * @version 1.0
 */

/**
 * 二级分类VO
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Catelog2Vo {
    private String catalog1Id;//一级父分类Id
    private List<Catelog3Vo> catalog3List;//三级子分类Id
    private String id;
    private String name;

    /**
     * 三级分类VO
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Catelog3Vo {
        private String catalog2Id;
        private String id;
        private String name;
    }
}
