package com.learn.mall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.learn.common.utils.R;
import com.learn.mall.ware.dao.WareSkuDao;
import com.learn.mall.ware.entity.WareSkuEntity;
import com.learn.mall.ware.entity.vo.FareVo;
import com.learn.mall.ware.entity.vo.MemberAddressVo;
import com.learn.mall.ware.feign.MemberFeignService;
import com.learn.mall.ware.service.WareSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.Query;

import com.learn.mall.ware.dao.WareInfoDao;
import com.learn.mall.ware.entity.WareInfoEntity;
import com.learn.mall.ware.service.WareInfoService;
import org.springframework.util.StringUtils;

import javax.management.relation.RelationNotFoundException;


@Service("wareInfoService")
@SuppressWarnings("all")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Autowired
    private WareSkuService wareSkuService;

    @Autowired
    private WareSkuDao wareSkuDao;

    @Autowired
    private MemberFeignService memberFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                new QueryWrapper<WareInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<WareInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.eq("id", key).or()
                    .like("name", key).or()
                    .like("address", key).or()
                    .like("areacode", key);
        }
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);

    }

    /**
     * 更新仓库名字的时候，将其他表中这个仓库名字字段也更新
     *
     * @param wareInfo address: "西安市莲湖区汉城壹号"
     *                 areacode: "1001"
     *                 id: 1
     *                 name: "西安莲湖总仓(测试修改)"
     *                 t: 1619195744765
     */
    @Override
    public void updateRelationTableById(WareInfoEntity wareInfo) {
        this.updateById(wareInfo);
        String wareName = wareInfo.getName();
        Long wareId = wareInfo.getId();
        List<WareSkuEntity> wareSkuEntities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("ware_id", wareId));
        if (wareSkuEntities.size() != 0) {
            for (WareSkuEntity wareSkuEntity : wareSkuEntities) {
                wareSkuEntity.setWareName(wareName);
            }
            wareSkuService.updateBatchById(wareSkuEntities);
        }
    }

    /**
     * 模拟运费的计算
     */
    @Override
    public FareVo getFare(Long addrId) {
        FareVo fareVo = new FareVo();
        R r = memberFeignService.addrInfo(addrId);
        MemberAddressVo data = r.getData("memberReceiveAddress", new TypeReference<MemberAddressVo>() {
        });
        if (data != null) {
            //生成一个随机数（1-30）的运费价格
//            Random random = new Random();
//            int farePrice = random.nextInt(29) + 1;
//            BigDecimal finalFare = new BigDecimal(farePrice);
//            fareVo.setAddress(data);
//            fareVo.setFare(finalFare);
            String phone = data.getPhone();
            String substring = phone.substring(phone.length() - 1, phone.length());
            BigDecimal bigDecimal = new BigDecimal(substring);
            fareVo.setAddress(data);
            fareVo.setFare(bigDecimal);
            return fareVo;
        }
        return null;
    }

}