package com.learn.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.learn.mall.product.entity.vo.front.Catelog2Vo;
import com.learn.mall.product.service.CategoryBrandRelationService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.Query;

import com.learn.mall.product.dao.CategoryDao;
import com.learn.mall.product.entity.CategoryEntity;
import com.learn.mall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@SuppressWarnings("all")
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    /**
     * 本地缓存
     */
    private Map<String, Object> cache = new HashMap<>();

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 查出所有商品分类（三级分类），以树形结构返回
     */
    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);
        List<CategoryEntity> menus = entities.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentCid() == 0;
        }).map((menu) -> {
            menu.setChildren(getChildren(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null) ? 0 : menu1.getSort() - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return menus;
    }

    /**
     * 根据分类id来批量删除分类
     * 使用逻辑删除以后，sql语句会从delete改变为：
     * update 表名 set 列名(逻辑删除字段) = 0 where xxx = xxx and 列名(逻辑删除字段) = 1
     */
    @Override
    public void removeMenusByIds(List<Long> asList) {
        baseMapper.deleteBatchIds(asList);
    }

    /**
     * 找出当前分类ID的分类路径 例如： 【2,34,225】 -> 【父分类，子分类，孙分类。。】
     *
     * @param catelogId 当前分类ID
     * @return 当前分类ID的分类路径（数组）
     */
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new LinkedList<>();
        findParentPath(catelogId, paths);
        Collections.reverse(paths);
        return paths.toArray(new Long[paths.size()]);
    }

    /**
     * 更新分类本身的时候，还要更新在其他表中分类的名字
     *
     * @Caching(evict = {
     * @CacheEvict(value = "category", key = "'Level1Categorys'"),
     * @CacheEvict(value = "category", key = "'CatalogJson'")})
     * @CacheEvict : 失效模式 保证缓存数据一致性 如果发生修改，那么删除对应的namespace中的缓存
     */
    @Transactional
    @Override
    @CacheEvict(value = "category", allEntries = true)
    public void updateDetails(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    /**
     * 获取所有的一级分类
     *
     * @Cacheable： 代表当前方法的结果需要缓存，如果缓存中有，方法不会调用，如果缓存中没有，会调用方法，最后将方法的结果返回
     */
    @Cacheable(value = {"category"}, key = "'Level1Categorys'", sync = true)
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        System.out.println("getLevel1Categorys 查询了数据库");
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>()
                .eq("parent_cid", 0));
        return categoryEntities;
    }

    /**
     * 查询二级分类和三级分类
     */
    @Cacheable(value = {"category"}, key = "'CatalogJson'", sync = true)
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        System.out.println("getCatalogJson 查询了数据库");
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        //查出所有一级分类
        List<CategoryEntity> level1Categorys = getChildrenCategoryByParentId(selectList, 0L);
        //封装数据
        Map<String, List<Catelog2Vo>> catelog2VoList = level1Categorys.stream().
                collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                    //根据这个一级分类，查到每一个一级分类所对应的二级分类
                    List<CategoryEntity> categoryEntities = getChildrenCategoryByParentId(selectList, v.getCatId());
                    List<Catelog2Vo> catelog2Vos = null;
                    if (categoryEntities != null) {
                        catelog2Vos = categoryEntities.stream().map(l2 -> {
                            Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString()
                                    , null
                                    , l2.getCatId().toString()
                                    , l2.getName());
                            //找当前二级分类的三级分类封装成Vo
                            List<CategoryEntity> level3Catalog = getChildrenCategoryByParentId(selectList, l2.getCatId());
                            if (level3Catalog != null) {
                                //一个二级分类下的所有三级分类
                                List<Catelog2Vo.Catelog3Vo> collect = level3Catalog.stream().map(l3 -> {
                                    //每一个二级分类下的每一个三级分类
                                    Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo
                                            .Catelog3Vo(l2.getCatId().toString()
                                            , l3.getCatId().toString()
                                            , l3.getName());
                                    return catelog3Vo;
                                }).collect(Collectors.toList());
                                catelog2Vo.setCatalog3List(collect);
                            }
                            return catelog2Vo;
                        }).collect(Collectors.toList());
                    }
                    return catelog2Vos;
                }));
        return catelog2VoList;
    }

    /**
     * ps:  产生堆外内存溢出：io.netty.util.internal.OutOfDirectMemoryError
     * Redis缓存实现查询三级分类方法
     * springboot2.0以后默认使用lettuce作为操作Redis的客户端，他使用netty进行网络通信
     * lettuce的bug导致netty堆外内存溢出，-Xmx300m netty如果没有指定堆外内存，默认使用-Xmx300m
     * netty堆外内存可以通过-Dio.netty.maxDirectMemory进行设置
     * 解决方案： 不能使用 -Dio.netty.maxDirectMemory进行设置
     * 1.升级luttuce客户端版本
     * 2.切换使用jedis
     */
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDBOrRedisWithWithRedissonLock() {
        /**
         * 1.空结果缓存: 解决缓存穿透（大量请求查找一个或多个不存在的key） （布隆过滤器）
         * 2.设置随机过期时间: 解决缓存雪崩（许多key同时过期，这时有大量请求进来）
         * 3.加锁: 解决缓存击穿（大量请求查找一个热点key 这个热点key在此时刚好失效）
         */
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        //缓存中存放的都是对象的JSON字符串
        if (StringUtils.isEmpty(catalogJSON)) {
            System.out.println("缓存没命中，查询数据库");
            Map<String, List<Catelog2Vo>> catalogJsonFromDB = getCatalogJsonFromDBWithRedissonLock();
            return catalogJsonFromDB;
        }
        System.out.println("缓存命中，查询缓存");
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON,
                new TypeReference<Map<String, List<Catelog2Vo>>>() {
                });
        return result;
    }

    /**
     * 使用Redisson框架实现分布式锁
     * <p>
     * 缓存数据一致性问题（双写模式，失效模式）
     * 使用Canal更新缓存
     * 业务代码 -> 更新DB -> binlog -> canal -> 更新
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDBWithRedissonLock() {
        /*
           调用getlock方法会在redis数据库中存储一个Hash数据结构的键值，
           键的名字就是getlock()方法中指定的名字，由于是hash数据结构，除了本身的名字key以外，
           内部还有一个key和value，key是一串字符串+线程id，值是1。
         */
        Map<String, List<Catelog2Vo>> dataFromDB = null;
        RLock lock = redissonClient.getLock("CatalogJson-lock");
        lock.lock();
        try {
            dataFromDB = getDataFromDB();
        } finally {
            lock.unlock();
        }
        return dataFromDB;
    }

    /**
     * 使用原生Redis实现分布式锁（setnx）
     * 分布式锁演变：
     * 1.setnx占好了位，业务代码异常或者程序宕机，服务器断电，没有执行删除锁的代码，造成了死锁
     * 解决： 设置锁的过期时间，即使没有删除，会自动删除
     * <p>
     * 2.setnx占好了位，在将要设置锁的过期时间时，服务器断电，宕机，没有执行设置锁过期时间代码，造成死锁
     * 解决： 将设置锁的过期时间代码和设置锁的代码作为一个原子操作，锁一出生就带上死亡时间
     * <p>
     * 3.如果业务处理时间超长，已经大于设置的锁的过期时间，那么在业务执行期间，锁已经自己消亡，这时其他请求拿到了锁
     * 这个业务执行完成以后删锁，其实删的是第二个业务设置的锁
     * 解决： 设置一个uuid（唯一标识）让每个请求进来只能删除自己的锁
     * <p>
     * 4.如果锁的过期时间为10s,业务进行了9.5s,向redis发送get获取值的过程用了0.3s,返回途中用了0.3s,在redis向你返回
     * uuid数据的时候，返回的是你设置的uuid，但是第十秒锁过期了，进来个一个新uuid的新锁，但你拿到结果判断发现是我自己的uuid
     * 然后执行删锁代码，还是会把别人的锁删除，如果删除了别人的锁，那么就有更多的请求进来，相当于没锁住
     * 解决： 查锁是不是自己的 和 对比成功以后删除锁 这两个操作必须也是原子的
     * 终极解决： redis + lua 脚本 （解锁脚本）
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDBWithRedisLock() {
        //1.占分布式锁，去redis占坑
        //如果set成功 ，返回true  原子（占坑+过期时间）
        String uuid = UUID.randomUUID().toString();
        Boolean flag = redisTemplate.opsForValue().setIfAbsent("lock", uuid,
                30, TimeUnit.SECONDS);
        if (flag) {
            //加锁成功，执行业务
            Map<String, List<Catelog2Vo>> dataFromDB;
            try {
                dataFromDB = getDataFromDB();
            } finally {
                //原子删锁
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                        "return redis.call('del',KEYS[1]) " +
                        "else return 0 " +
                        "end";
                redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
            }
            /**
             * 删锁之前先看一下这个锁是不是自己设置的（避免自己的锁已经提前过期了，从而删除了别的请求的锁）
             *  String lockValue = redisTemplate.opsForValue().get("lock");
             *          if (lockValue.equals(uuid)) {
             * 执行完业务后，把锁释放掉 （只能删除自己设置的锁）
             *         redisTemplate.delete("lock");
             * }
             */
            return dataFromDB;
        } else {
            //加锁失败，重试
            try {
                Thread.sleep(200);
            } catch (Exception e) {
            }
            //线程休眠一会后再递归调用，相当于自旋操作，如果不休眠，会将栈撑爆
            return getCatalogJsonFromDBWithRedisLock();
        }
    }

    /**
     * 从数据库中查询三级分类方法，已被改进为Redis版本
     * 使用本地锁（已被改为分布式锁）
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDBWithLocalLock() {
        /*
         * 本地缓存伪代码:
         *
         *  Map<String, List<Catelog2Vo>> catalogJson = (Map<String, List<Catelog2Vo>>) cache.get("CatalogJson");
         *  if (catalogJson == null){
         *  业务逻辑
         *  返回数据之前将数据放入缓存
         *  cache.put("CatalogJson",xxx);
         *        return xxx;
         * }
         * return catalogJson;
         */
        /*
           加本地锁(本地锁只能锁住当前进程，需要使用分布式锁)
           在本项目中，使用分布式锁更大的意义仅是一个分布式锁的演示，实际如果一个微服务部署了10台机器，那么就算使用
           本地锁，每个机器微服务机器放进去一个线程去请求数据库，也不会对数据库造成什么压力
           而且分布式锁相比于本地锁的消耗更大，所以注意根据实际场合来使用分布式锁
         */
        synchronized (this) {
            // 线程拿到锁以后，进入方法还要判断缓存中是否有值，如果有值，直接返回缓存中的值
            return getDataFromDB();
        }

    }

    /**
     * 查询三级分类，如果缓存没有，需要在数据库中查询，并将结果放入缓存
     */
    @Override
    public Map<String, List<Catelog2Vo>> getDataFromDB() {

        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (!StringUtils.isEmpty(catalogJSON)) {
            Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON,
                    new TypeReference<Map<String, List<Catelog2Vo>>>() {
                    });
            return result;
        }
        System.out.println("查询了数据库");
        List<CategoryEntity> selectList = baseMapper.selectList(null);

        //查出所有一级分类
        List<CategoryEntity> level1Categorys = getChildrenCategoryByParentId(selectList, 0L);
        //封装数据
        Map<String, List<Catelog2Vo>> catelog2VoList = level1Categorys.stream().
                collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                    //根据这个一级分类，查到每一个一级分类所对应的二级分类
                    List<CategoryEntity> categoryEntities = getChildrenCategoryByParentId(selectList, v.getCatId());
                     List<Catelog2Vo> catelog2Vos= null;
                    if (categoryEntities != null) {
                        catelog2Vos = categoryEntities.stream().map(l2 -> {
                            Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString()
                                    , null
                                    , l2.getCatId().toString()
                                    , l2.getName());
                            //找当前二级分类的三级分类封装成Vo
                            List<CategoryEntity> level3Catalog = getChildrenCategoryByParentId(selectList, l2.getCatId());
                            if (level3Catalog != null) {
                                //一个二级分类下的所有三级分类
                                List<Catelog2Vo.Catelog3Vo> collect = level3Catalog.stream().map(l3 -> {
                                    //每一个二级分类下的每一个三级分类
                                    Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo
                                            .Catelog3Vo(l2.getCatId().toString()
                                            , l3.getCatId().toString()
                                            , l3.getName());
                                    return catelog3Vo;
                                }).collect(Collectors.toList());
                                catelog2Vo.setCatalog3List(collect);
                            }
                            return catelog2Vo;
                        }).collect(Collectors.toList());
                    }
                    return catelog2Vos;
                }));
        /*
            此方法整体被锁住，因此不会出现一个线程释放了锁，正在去添加缓存结果到redis中的这个延迟时，其他线程
            拿到了锁，判断redis没有，去查库，造成没锁住的现象
         */
        String s = JSON.toJSONString(catelog2VoList);
        redisTemplate.opsForValue().set("catalogJSON", s, 1, TimeUnit.DAYS);
        return catelog2VoList;
    }

    /**
     * 在所有的分类集合里找到他们父分类Id为指定Id的分类
     * 需求：
     * 根据每一个一级分类自己的ID，找到这个一级分类下面所有的二级分类
     * 根据每一个二级分类自己的ID，找到这个二级分类下面所有的三级分类
     *
     * @param selectList 所有分类集合
     * @param parent_cid 指定的一个父分类Id
     */
    private List<CategoryEntity> getChildrenCategoryByParentId(List<CategoryEntity> selectList, Long parent_cid) {
        List<CategoryEntity> collect = selectList.stream().filter(
                item -> item.getParentCid() == parent_cid).collect(Collectors.toList());
        return collect;
    }

    /**
     * 找出当前分类ID的分类路径  这个方法完成后 list里面应该是 【225,34,2】
     * 实际需要的是【2,34,225】
     *
     * @param catelogId 当前分类ID
     * @param paths     每一级分类都加入到一个list里面
     */
    private void findParentPath(Long catelogId, List<Long> paths) {
        if (catelogId != 0) {
            paths.add(catelogId);
        }
        CategoryEntity categoryEntity = this.getById(catelogId);
        if (categoryEntity.getParentCid() != 0) {
            findParentPath(categoryEntity.getParentCid(), paths);
        }
    }

    /**
     * 递归查找当前分类的子分类
     *
     * @param root 当前分类（某一个）
     * @param all  所有分类（所有）
     * @return 当前分类在所有分类中查找到的子分类
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentCid().equals(root.getCatId());
        }).map((categoryEntity) -> {
            //此处是一个递归调用，一直查找到没有分类为止，不只是在这里面查找一次
            categoryEntity.setChildren(getChildren(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null) ? 0 : menu1.getSort() - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return children;
    }
}