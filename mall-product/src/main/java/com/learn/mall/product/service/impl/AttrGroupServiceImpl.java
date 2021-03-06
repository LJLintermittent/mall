package com.learn.mall.product.service.impl;

import com.learn.mall.product.entity.AttrEntity;
import com.learn.mall.product.entity.vo.AttrGroupWithAttrsVo;
import com.learn.mall.product.entity.vo.front.SkuItemVo;
import com.learn.mall.product.service.AttrService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.Query;

import com.learn.mall.product.dao.AttrGroupDao;
import com.learn.mall.product.entity.AttrGroupEntity;
import com.learn.mall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
@SuppressWarnings("all")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrService attrService;

    /**
     * 原生分页查询
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据分类ID来进行分页查询
     * SQL语句如下
     * SELECT attr_group_id,icon,catelog_id,sort,descript,attr_group_name FROM pms_attr_group
     * WHERE (( (attr_group_id = ? OR attr_group_name LIKE ?) ) AND catelog_id = ?) LIMIT ?,?
     * 在catelog_id和attr_group_name字段建立普通索引
     * 加索引前后压测分析：
     * 加完索引以后使用1000线程 ramp-up：1s 循环次数永远 最大堆内存512M,样本数30000,吞吐量是781.8/sec
     * 去掉索引以后使用1000线程 ramp-up：1s 循环次数永远 最大堆内存512M 样本数30000,吞吐量是843.5/sec
     * 对于数据量较少的业务，加索引反而降低了吞吐量
     *
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((obj) -> {
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }
        if (catelogId == 0) {
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        } else {
            wrapper.eq("catelog_id", catelogId);
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);
        }
    }

    /**
     * 查出当前三级分类下所有的属性分组和每个属性分组下的所有属性
     *
     * @param catelogId 三级分类ID
     * @return 当前三级分类下所有的属性分组和每个属性分组下的所有属性
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrByCategoryId(Long catelogId) {
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("catelog_id", catelogId);
        List<AttrGroupEntity> attrGroupEntities = this.list(wrapper);
        List<AttrGroupWithAttrsVo> collect = attrGroupEntities.stream().map((item) -> {
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(item, attrGroupWithAttrsVo);
            List<AttrEntity> attrEntities = attrService.getRelationAttr(attrGroupWithAttrsVo.getAttrGroupId());
            attrGroupWithAttrsVo.setAttrs(attrEntities);
            return attrGroupWithAttrsVo;
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 查出当前spu对应的所有属性分组信息以及每一个分组下对应的属性名字和属性值
     */
    @Override
    public List<SkuItemVo.SpuItemAttrGroupVo> getAttrGroupWithAttrBySpuId(Long spuId, Long catalogId) {
        List<SkuItemVo.SpuItemAttrGroupVo> result = baseMapper.getAttrGroupWithAttrBySpuId(spuId, catalogId);
        return result;
    }

}