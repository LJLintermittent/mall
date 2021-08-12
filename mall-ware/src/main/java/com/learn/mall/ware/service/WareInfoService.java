package com.learn.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.ware.entity.WareInfoEntity;
import com.learn.mall.ware.entity.vo.FareVo;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 仓库信息
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 21:18:51
 */
@SuppressWarnings("all")
public interface WareInfoService extends IService<WareInfoEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 带条件的分页查询
     */
    PageUtils queryPageByCondition(Map<String, Object> params);

    /**
     * 更新仓库名字的时候，将其他表中这个仓库名字字段也更新
     */
    void updateRelationTableById(WareInfoEntity wareInfo);

    /**
     * 模拟运费的计算
     */
    FareVo getFare(Long addrId);

}

