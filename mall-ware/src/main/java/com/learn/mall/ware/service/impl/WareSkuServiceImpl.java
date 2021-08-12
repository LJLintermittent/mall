package com.learn.mall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.learn.common.to.mq.OrderTo;
import com.learn.common.to.mq.StockDetailTo;
import com.learn.common.to.mq.StockLockedTo;
import com.learn.common.utils.R;
import com.learn.mall.ware.entity.WareInfoEntity;
import com.learn.mall.ware.entity.WareOrderTaskDetailEntity;
import com.learn.mall.ware.entity.WareOrderTaskEntity;
import com.learn.mall.ware.entity.vo.*;
import com.learn.mall.ware.exception.NoStockException;
import com.learn.mall.ware.feign.OrderFeignService;
import com.learn.mall.ware.feign.ProductFeignService;
import com.learn.mall.ware.service.WareInfoService;
import com.learn.mall.ware.service.WareOrderTaskDetailService;
import com.learn.mall.ware.service.WareOrderTaskService;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

import com.learn.mall.ware.dao.WareSkuDao;
import com.learn.mall.ware.entity.WareSkuEntity;
import com.learn.mall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@SuppressWarnings("all")
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WareOrderTaskService wareOrderTaskService;

    @Autowired
    private WareOrderTaskDetailService wareOrderTaskDetailService;

    @Autowired
    private WareSkuDao wareSkuDao;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private WareInfoService wareInfoService;

    @Autowired
    private OrderFeignService orderFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * SKU库存的带条件分页查询
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((queryWrapper) -> {
                queryWrapper.eq("sku_id", key).or().eq("ware_id", key)
                        .or().like("sku_name", key).or().like("ware_name", key);
            });
        }
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            wrapper.eq("ware_id", wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    /**
     * 采购成功以后要将商品入库（入的是指定的库）
     */
    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        List<WareSkuEntity> wareSkuEntities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>()
                .eq("sku_id", skuId).eq("ware_id", wareId));
        if (wareSkuEntities == null || wareSkuEntities.size() == 0) {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            WareInfoEntity infoEntity = wareInfoService.getById(wareId);
            wareSkuEntity.setWareName(infoEntity.getName());
            //远程查询sku的名字 如果失败 整个事务无需回滚
            try {
                R result = productFeignService.info(skuId);
                Map<String, Object> data = (Map<String, Object>) result.get("skuInfo");
                if (result.getCode() == 0) {
                    wareSkuEntity.setSkuName((String) data.get("skuName"));
                }
            } catch (Exception e) {

            }
            wareSkuDao.insert(wareSkuEntity);
        } else {
            wareSkuDao.updateStock(skuId, wareId, skuNum);
        }
    }

    /**
     * 修改仓库的时候不只是修改表中的仓库ID字段，还要将仓库对应的名字改正
     *
     * @param wareSku id: 11
     *                skuId: 8
     *                wareId: 2
     *                (ware_name) : 西安莲湖总仓
     *                stock: 3
     *                skuName: "Apple iPhone 12 红色 256GB"
     *                stockLocked: 0
     *                t: 1619194644128
     */
    @Override
    public void updateAllById(WareSkuEntity wareSku) {
        Long wareId = wareSku.getWareId();
        WareInfoEntity wareInfoEntity = wareInfoService.getById(wareId);
        wareSku.setWareName(wareInfoEntity.getName());
        this.updateById(wareSku);
    }

    /**
     * 检查每一个商品Sku是否还有库存
     *
     * @param skuIds 一个要上架的商品对应的所有SkuId
     * @return
     */
    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {
        List<SkuHasStockVo> collect = skuIds.stream().map((skuId) -> {
            SkuHasStockVo skuHasStockVo = new SkuHasStockVo();
            Long count = baseMapper.getSkuStock(skuId);
            skuHasStockVo.setSkuId(skuId);
            skuHasStockVo.setHasStock(count != null && count > 0);
            return skuHasStockVo;
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 为某个订单锁定库存
     *
     * @Transactional(rollbackFor = NoStockException.class)
     * 默认只要是运行时异常，都会回滚
     * <p>
     * 库存解锁的场景：
     * 1.下订单成功，订单过期没有支付被系统自动取消
     * 2.下订单成功，用户又手动取消了订单
     * 3，下订单成功，库存锁定成功，接下来的业务调用出现异常，导致订单服务回滚
     * 这时候库存需要被解锁（库存服务需要回滚）
     */
    @Transactional
    @Override
    public Boolean orderLockStock(WareSkuLockVo vo) {
        /**
         * 保存库存工作单的详情
         * 用于追溯哪个仓库当时锁了多少库存
         */
        WareOrderTaskEntity wareOrderTaskEntity = new WareOrderTaskEntity();
        wareOrderTaskEntity.setOrderSn(vo.getOrderSn());
        wareOrderTaskService.save(wareOrderTaskEntity);
        //找到每个商品在哪个仓库都有库存
        List<OrderItemVo> locks = vo.getLocks();
        List<SkuWareHasStock> collect = locks.stream().map(item -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            stock.setSkuId(skuId);
            stock.setNum(item.getCount());
            //查询这个商品在哪里有库存
            List<Long> wareIds = baseMapper.listHasStockWareIdBySkuId(skuId);
            stock.setWareId(wareIds);
            return stock;
        }).collect(Collectors.toList());
        //锁定库存
        for (SkuWareHasStock hasStock : collect) {
            Boolean skuStocked = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
            if (wareIds == null || wareIds.size() == 0) {
                //没有任务仓库有这个商品的库存
                throw new NoStockException(skuId);
            }
            for (Long wareId : wareIds) {
                //一行受到了影响，所以成功更新就返回1 否则返回0
                Long count = wareSkuDao.lockSkuStock(skuId, wareId, hasStock.getNum());
                if (count == 1) {
                    //当前仓库锁定成功，应该给MQ发送消息
                    skuStocked = true;
                    //1.如果每一个商品都锁定成功，将当前商品锁定了几件的工作单记录发送给MQ
                    //2.锁定失败，前面保存的工作单就回滚。
                    WareOrderTaskDetailEntity wareOrderTaskDetailEntity = new WareOrderTaskDetailEntity(
                            null
                            , skuId
                            , ""
                            , hasStock.getNum()
                            , wareOrderTaskEntity.getId()
                            , wareId
                            , 1);
                    wareOrderTaskDetailService.save(wareOrderTaskDetailEntity);
                    StockLockedTo stockLockedTo = new StockLockedTo();
                    stockLockedTo.setId(wareOrderTaskEntity.getId());
                    StockDetailTo stockDetailTo = new StockDetailTo();
                    BeanUtils.copyProperties(wareOrderTaskDetailEntity, stockDetailTo);
                    //只发送id不行，防止回滚以后找不到数据
                    stockLockedTo.setDetail(stockDetailTo);
                    rabbitTemplate.convertAndSend("stock-event-exchange",
                            "stock.locked", stockLockedTo);
                    break;
                } else {
                    //当前仓库锁定失败，继续寻找下一个仓库
                }
            }
            if (skuStocked == false) {
                //当前商品在所有仓库都没有锁住
                throw new NoStockException(skuId);
            }
        }
        //能走到这一步，没有抛异常，代表全部锁定成功
        return true;
    }

    /**
     * rabbitMQ 监听的解锁库存功能，在listener中调用此service方法
     * <p>
     * 库存自动解锁方法
     * 下订单成功，库存锁定成功，接下来的业务调用出现异常，导致订单服务回滚
     * 这时候库存需要被解锁（库存服务需要回滚）
     * <p>
     * 手动ACK机制！！
     * 如果解锁库存的消息运作失败，一定要通知服务器，这个消息不要删除，应当重试
     */
    @Override
    public void unlockStockRelease(StockLockedTo to) {
        StockDetailTo detail = to.getDetail();
        Long detailId = detail.getId();
        /**
         * 解锁：
         * 1.查询数据库关于这个订单的锁定库存信息
         * 如果有：证明库存锁定成功了
         * 解锁的时候还需要分情况进行讨论：
         * 1。没有这个订单，那么必须解锁
         * 2。有这个订单，根据订单状态来判断，如果订单已取消，那么解锁库存，如果订单没取消，不能解锁库存
         * 如果没有：库存锁定失败了，库存回滚了，这种情况无需解锁
         */
        WareOrderTaskDetailEntity byId = wareOrderTaskDetailService.getById(detailId);
        //有工作单信息
        if (byId != null) {
            //解锁
            Long id = to.getId();//库存工作单的ID
            WareOrderTaskEntity taskEntity = wareOrderTaskService.getById(id);
            String orderSn = taskEntity.getOrderSn();//根据订单号查询订单的状态
            R orderFeignResult = orderFeignService.getOrderStatusByOrderSn(orderSn);
            if (orderFeignResult.getCode() == 0) {
                OrderVo data = orderFeignResult.getData(new TypeReference<OrderVo>() {
                });
                if (data == null || data.getStatus() == 4) {
                    //订单不存在或者订单已经被取消了,接下来需要进行解锁操作
                    if (byId.getLockStatus() == 1) {
                        //当前库存工作单的锁定状态为1 表示已锁定但是未解锁状态，才可以进行解锁
                        unLockStock(detail.getSkuId(), detail.getWareId(), detail.getSkuNum(), detailId);
                    }
                }
            } else {
                //requeue: 消息拒绝后我让你重新返回队列
                throw new RuntimeException("远程服务失败");
            }
        } else {
            //无需解锁
        }
    }

    /**
     * 此方法 是防止订单服务卡顿，导致订单消息一直改不了，库存消息优先到期，查询到订单状态一直为新建状态，于是什么都不做
     * 导致卡顿的订单永远不能解锁库存
     */
    @Transactional
    @Override
    public void unlockStockRelease(OrderTo orderTo) {
        String orderSn = orderTo.getOrderSn();
        //查一下最新的库存状态，防止重复解锁库存
        WareOrderTaskEntity entity = wareOrderTaskService.getOrderTaskByOrderSn(orderSn);
        Long id = entity.getId();
        //按照工作单找到所有没有解锁的库存进行解锁
        List<WareOrderTaskDetailEntity> list = wareOrderTaskDetailService.list(
                new QueryWrapper<WareOrderTaskDetailEntity>()
                        .eq("task_id", id)
                        .eq("lock_status", 1));
        for (WareOrderTaskDetailEntity taskDetailEntity : list) {
            unLockStock(taskDetailEntity.getSkuId(), taskDetailEntity.getWareId()
                    , taskDetailEntity.getSkuNum(), taskDetailEntity.getId());
        }
    }

    /**
     * 解锁库存方法
     */
    private void unLockStock(Long skuId, Long wareId, Integer num, Long taskDetailId) {
        //库存解锁
        wareSkuDao.unlockStock(skuId, wareId, num);
        //库存解锁后更新工作单的锁定状态
        WareOrderTaskDetailEntity taskDetailEntity = new WareOrderTaskDetailEntity();
        taskDetailEntity.setId(taskDetailId);
        taskDetailEntity.setLockStatus(2);
        wareOrderTaskDetailService.updateById(taskDetailEntity);

    }
}
