# 电商项目重难点回顾文档

### 分布式微服务概念

### 微服务

微服务架构的风格就是将一个大型的单体应用，拆分成了多个小服务，每个小服务运行在自己的进程中，或者在不同的机器中运行，使用轻量级通信机制，通常是HTTP API。这些微服务围绕业务能力来构建划分，并通过完全自动化的部署机制来独立部署。微服务可以使用不同的编程语言编写，每个微服务涉及的数据存储可以使用不同的存储技术，并保持最低限度的集中式管理。

简而言之，拒绝大型单体应用，基于业务边界将服务拆分，各个服务独立部署运行。

### 集群&分布式&节点

集群是一种物理形态，分布式是一种工作方式

也就是说集群就是把多个机器集中在一起，就叫集群，这一堆机器到底是各做各的事情，还是都做一件事情，根据具体情况而定。

分布式是若干计算机组成的服务的集合，这些计算机组成的服务对于用户来说是感知不到的，用户使用一个大型分布式系统，就感觉像是使用一个服务系统一样。

例如：京东是一个大型分布式系统，众多业务运行在不同的机器上，所有业务构成一个大型的业务集群，每个小的业务，由于访问的并发量大的原因，一台机器可能承受不住，那么每一个服务都可以使用多台机器构成一个集群，而这一块集群中的一台机器在分布式系统中就对应一个节点。也就是每一个业务都可以做到集群化。

综上所述，分布式是将不同的业务分布到不同的地方。集群是指将多个机器集中起来，处理一种业务，达到高并发，高可用的特性（集群通常可以用来做高可用，当一台机器挂了，其他机器可以立马顶上，不影响系统的可用性）

节点是集群中的一台机器

### 分布式微服务架构衍生出来的问题

### 远程调用

在分布式系统中，各个服务可能处于不同的主机中，服务之间不可避免的会存在互相调用的情况，这叫远程调用

通常使用HTTP+JSON的方式来完成远程调用

### 负载均衡

比如订单服务查询商品服务获得商品信息，商品服务部署在了多个服务器上，调用哪一个机器都可以完成功能。

常见的负载均衡算法有轮询算法，最小连接算法，散列算法等

轮询就是每一个服务器挨个轮流访问，可以使访问压力均衡。

最小连接是哪个服务器的压力小，就派发到哪个服务器上去。

散列是将特定的IP源发送到特定的机器，可以一定程度上保证特定用户能连接到相同的服务器。如果你的应用需要处理状态而要求用户能连接到和之前相同的服务器，可以使用这种算法。

### 服务的注册发现和注册中心

当商品服务和订单服务都有好几台服务器部署的时候，订单要调用商品，这时候你不知道商品服务的哪些机器上线了，哪些机器挂掉了，那么商品和订单的微服务在上线的时候需要把自己的状态（也就是已上线来通知到注册中心，这就是服务注册的过程），这时候再调用，你就会知道哪些可以调用，然后再通过特定的负载均衡算法来实现调用。

### 配置中心

当一个微服务部署在多个机器上时，而且每一个复制出去的微服务都有大量的配置信息，那么当需要修改配置时，就要同步修改所有机器上这个微服务的配置信息，我们可以通过配置中心来修改配置文件，然后让注册在配置中心的服务主动去获取配置中心中更新后的自己的配置，省去了以前需要手动停掉每个机器，来修改配置再上线的繁杂过程。

### 熔断&限流&降级

当一个服务A调用服务B，服务B又需要调用服务C，这时候如果C因为网络故障等原因迟迟无法响应，那么会造成B的阻塞，B的阻塞又会造成A的阻塞，当高并发进来时，会造成调用链挤压，如果耗尽了服务器资源，那么就会发生服务雪崩，造成系统的不可用。

服务熔断，开启短路保护机制，让后面来的请求直接不会请求到这个熔断的服务，不会去调用你这个服务，本地直接返回默认的数据。

服务降级，在业务高峰期，停掉或者降级某些非核心业务，将资源让给核心业务，降级后的业务处理方式变得简单，比如直接返回NULL或者调用fallback处理逻辑。

### API网关

网关作为所有流量的统一入口，抽象了所有微服务中都需要的功能，提供了客户端负载均衡，服务自动熔断，灰度发布，统一认证，限流流控，日志统计等丰富的功能。作为所有微服务统一的入口，解决API的管理问题。

### @Mapper与@MapperScan注解

@Mapper：
作用：在接口类上添加了@Mapper，并不是在编译期间生成实现类，而是在运行时通过动态代理生成这个接口的实现类

如果想要每个接口都要变成实现类，那么需要在每个接口类上加上@Mapper注解，比较麻烦，解决这个问题用@MapperScan

@MapperScan：
MapperScan注解被MapperScannerRegistrar的registerBeanDefinitions方法所引用，目的是将basePackages定义的所有包下的所有接口生成一个org.apache.ibatis.binding.MapperProxy代理bean，,无论这个接口你是用来干嘛的，他都会生成一个Bean，这样就可以用@Autowired注解进行装配使用了。
添加位置：是在Springboot启动类上面添加

~~~wiki
 public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
    AnnotationAttributes mapperScanAttrs = AnnotationAttributes
        .fromMap(importingClassMetadata.getAnnotationAttributes(MapperScan.class.getName()));
    if (mapperScanAttrs != null) {
      registerBeanDefinitions(mapperScanAttrs, registry, generateBaseBeanName(importingClassMetadata, 0));
    }
  }


 void registerBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry, String beanName) {

    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
    builder.addPropertyValue("processPropertyPlaceHolders", true);

    Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
    if (!Annotation.class.equals(annotationClass)) {
      builder.addPropertyValue("annotationClass", annotationClass);
    }

    Class<?> markerInterface = annoAttrs.getClass("markerInterface");
    if (!Class.class.equals(markerInterface)) {
      builder.addPropertyValue("markerInterface", markerInterface);
    }

    Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
    if (!BeanNameGenerator.class.equals(generatorClass)) {
      builder.addPropertyValue("nameGenerator", BeanUtils.instantiateClass(generatorClass));
    }

    Class<? extends MapperFactoryBean> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
    if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass)) {
      builder.addPropertyValue("mapperFactoryBeanClass", mapperFactoryBeanClass);
    }

    String sqlSessionTemplateRef = annoAttrs.getString("sqlSessionTemplateRef");
    if (StringUtils.hasText(sqlSessionTemplateRef)) {
      builder.addPropertyValue("sqlSessionTemplateBeanName", annoAttrs.getString("sqlSessionTemplateRef"));
    }

    String sqlSessionFactoryRef = annoAttrs.getString("sqlSessionFactoryRef");
    if (StringUtils.hasText(sqlSessionFactoryRef)) {
      builder.addPropertyValue("sqlSessionFactoryBeanName", annoAttrs.getString("sqlSessionFactoryRef"));
    }

    List<String> basePackages = new ArrayList<>();
    basePackages.addAll(
        Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));

    basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText)
        .collect(Collectors.toList()));

     							     basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName)
        .collect(Collectors.toList()));

    String lazyInitialization = annoAttrs.getString("lazyInitialization");
    if (StringUtils.hasText(lazyInitialization)) {
      builder.addPropertyValue("lazyInitialization", lazyInitialization);
    }

    builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));

    registry.registerBeanDefinition(beanName, builder.getBeanDefinition());

  }

~~~

注意：

MapperScan用来扫描定义包下的所有的接口，无论这个接口你的设计目的是用来干嘛的，他都会生成一个bean（经测试，@Service实现的接口和@FeignClient注解的接口，即使他已经都相关的程序注册了一个bean，MapperScan还是会将这些接口再注册一个bean，导致出错）

@Mapper注解源码里面空空如也，什么也没有(只有一行注释)

那么它的作用是什么？

```wiki
@MapperScan(value = "com.learn.mall.product.dao", annotationClass = Mapper.class)
```

指定扫描这个包下带Mapper注解的接口，然后生成代理Bean，从而不会由于多次注册一个接口的Bean而报错

Mapper注解就相当于是一个标志，可能在mybatis中会有拦截器来处理带有这个注解的接口，为他们生成动态代理类。

## **classpath**

在springboot项目中，classpath路径指的是resources文件夹下的目录

classpath 和 classpath* 区别： 
classpath：只会到你的class路径中查找找文件; 
classpath*：不仅包含class路径，还包括jar文件中(class路径)进行查找. 

### 缓存穿透&缓存击穿&缓存雪崩&布隆过滤器

高并发下的缓存失效问题：分为三类缓存穿透&缓存击穿&缓存雪崩

缓存穿透：同时查询一个一定不存在的数据（经常是一种攻击手段），由于缓存不命中，判断从缓存中取出来的为空，那么就会同时都去查数据库，当然，这种情况只在高并发下有讨论的意义，因为如果并发量不大的话，那么即使在一瞬间去查库了，也不会造成什么影响， 而且，只要有一个用户查了库，那么业务代码里面肯定会将这个数据重新添加到缓存中，那么后来的请求就不会去查数据库了。

缓存穿透的解决方法：1.缓存空结果到redis中，并设置一个较短的过期时间，因为空结果是一个无意义的结果，所以我们不希望它长时间占据内存空间，同时如果后面这个不存在的key被加上了，那么就不能一直还返回null。2.布隆过滤器 3.接口校验，一般正常的业务请求都不会去查询一个不存在的数据，如果有，那么很可能是恶意攻击导致，那么可以做网关做用户鉴权，在业务代码里做好数据合法性校验。

布隆过滤器：布隆过滤器的特点是判断一个不存在的东西，以及判断一个东西是否存在(有误判概率)。它是由一个bit数组和数个hash函数组成，主要用来判断一个元素是否在集合中存在。在初始化的时候bit数组中的每一位都被初始化为0，同时会定义K个哈希函数。

写入流程：当我们要写入一个值时，首先会把这个值经过K个哈希函数计算出下标位置，然后将bit数组中对应位置的值从0置为1，其他的值的添加都要经过这个流程。

查询流程：只要是哈希计算，就会出现不同值经过计算得到相同下标位置的情况，这也是布隆过滤器在判断元素是否存在时会发生误判的原因。因为如果一个值哈希后的计算出来的下标是1,3,7这三个位置，而这三个位置都不为0，那么只能说这个值可能存在，因为这个下标位置可能被其他值提前占用了，但是如果一个值经过哈希计算后，对应的数组下标有一个为0，就可以证明这个数肯定不存在，因为如果这个值存在，那么经过相同的数个哈希函数计算得到了下标值，并且改为了1，那么有0就代表一定不存在。判断存在时候，如果想降低误判率，可以通过加大bit数组的长度，或者提升hash函数的个数来解决。

缓存雪崩：大面积key同时过期，所有请求发送到了数据库，造成数据库压力骤增。

解决方法：分散缓存的过期时间设置

缓存击穿：某一个热点key失效，大量请求进来瞬间进来全去查库。

解决方法：1.加锁，让大量请求并发进来的时候阻塞，只让第一个抢到锁的线程执行查库代码，其他线程阻塞，第一个线程查到了数据以后，更新缓存，其他线程阻塞完毕后，在查库之前再次判断缓存中是否有值，如果有值，直接走缓存。2.热点key设置永不过期

### 分布式锁

使用redis实现原生分布式锁的原理就是使用redis的set命令加上nx参数，表示如果不存在就设置进去，那么假如让所有的商品服务都来到一个独立的中间件，比如说redis，在查询缓存的时候如果不存在，执行setnx命令，如果不存在就设置一个key，比如lock，就相当于这个线程拿到了锁。

Redis分布式锁的演进过程：

1.所有线程发送setnx命令，去占锁，占成功了，相当于获取到了锁，去执行查询数据库的业务，没占成功，阻塞等待，对应代码里redistemplate的setIfAbsent()方法，加锁成功执行完业务代码以后还要把锁删了。

2.第一个版本的问题是如果执行完业务代码，这时候出现了异常，那么没法执行删除代码的逻辑，就会造成死锁，那么很容易想到trycatchfinally，将删锁代码放到finally里面，即使异常，也要执行，但是如果是服务器断电的异常，虚拟机直接关闭，finally代码块中的代码也不会执行，依然会死锁。那么又能想到，设置锁的过期时间。

3.如果设置锁的过期时间和加锁代码不是原子性的，那么依然会出现加锁成功后没有执行设置过期时间的代码，出现死锁。所以加锁和设置过期时间必须保证原子性。

4.保证了过期时间和加锁的原子性后，依然有问题，就是说如果你的业务执行时间超长，业务还没执行完，过期时间到了，这时候其他线程拿到了锁，也进来执行代码，甚至当第三个线程进来后，第一个线程才执行完代码，然后执行删除锁的代码，这时候由于你的锁早已被过期时间机制删除，那么这时候你删除的想当于是别人的锁。

5.解决上一步的方式是使用分布式全局唯一ID，比如雪花算法，来保证你每个人上的锁的value是不同的，根据这个value来判断是不是你的锁，锁的key当然必须还是相同的，比如叫lock，key都不相同了，那就不是同一把锁了。

6.上一步依然存在问题，假如说redis给你半路传数据的时候(传的是根据value来判断这是不是你自己的锁)，这个时间段内，如果锁自动过期，其他线程抢到了锁，锁的value变了，但是redis已经给你传回来了你想要的那个value值，你一判断，这个value就是我加的那个uuid，然后执行删锁代码，删的还是别人的锁。

7.上一步的问题主要在于删锁的判断是两步，一步是获取值来对比，第二步是对比成功以后删除。这两步不是原子操作。使用redis的lua脚本来原子执行脚本内容，将比较value和删锁的逻辑做成一个原子脚本。

8.综上所述，redis分布式锁的原理就是加锁保证原子性和解锁保证原子性。

Redisson框架实现分布式锁的原理与上面描述的基本一致，它更优秀的一个点在于它的自动续期机制，避免死锁问题，并且因为自动续期机制，可以在业务超长的情况下，不会出现过期机制的删锁操作，如果它检测到你程序代码还没执行完，过期时间到了，它会自动续期，这就是看门狗机制。并且redisson在加锁的时候会默认加上一个30秒的自动过期时间。业务超时会自动续期

在redisson源码中的config类中有一个成员变量private long lockWatchdogTimeout = 30 * 1000;并且在创建redisclient这个bean的时候，我们会new一个config来配置进去，那么这个值已经被初始化默认好了是30s

```wiki
<T> RFuture<T> tryLockInnerAsync(long leaseTime, TimeUnit unit, long threadId, RedisStrictCommand<T> command) {
    internalLockLeaseTime = unit.toMillis(leaseTime);

    return commandExecutor.evalWriteAsync(getName(), LongCodec.INSTANCE, command,
              "if (redis.call('exists', KEYS[1]) == 0) then " +
                  "redis.call('hset', KEYS[1], ARGV[2], 1); " +
                  "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                  "return nil; " +
              "end; " +
              "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
                  "redis.call('hincrby', KEYS[1], ARGV[2], 1); " +
                  "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                  "return nil; " +
              "end; " +
              "return redis.call('pttl', KEYS[1]);",
                Collections.<Object>singletonList(getName()), internalLockLeaseTime, getLockName(threadId));
```

注意，如果使用redisson的lock方法中添加参数指定key的过期时间，那么会走到上面的方法中，然后执行lua脚本。那么在这个lua脚本中可以看到直接将我们传入的过期时间加了进去，并没有自动续期的代码实现，所以如果用了lock的带参方法，那么看门狗机制不生效。

上述代码从tryAcquireAsync方法中进入tryLockInnerAsync

如果lock方法用空参实现，那么看门狗机制的实现在scheduleExpirationRenewal中

```wiki
private void scheduleExpirationRenewal(long threadId) {
    ExpirationEntry entry = new ExpirationEntry();
    ExpirationEntry oldEntry = EXPIRATION_RENEWAL_MAP.putIfAbsent(getEntryName(), entry);
    if (oldEntry != null) {
        oldEntry.addThreadId(threadId);
    } else {
        entry.addThreadId(threadId);
        renewExpiration();
    }
}
```

renewExpiration方法重新设置过期时间：

```wiki
Timeout task = commandExecutor.getConnectionManager().newTimeout(new TimerTask() {
```

会启动一个定时任务

~~~wiki
Timeout task = commandExecutor.getConnectionManager().newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                ExpirationEntry ent = EXPIRATION_RENEWAL_MAP.get(getEntryName());
                if (ent == null) {
                    return;
                }
                Long threadId = ent.getFirstThreadId();
                if (threadId == null) {
                    return;
                }
                
                RFuture<Boolean> future = renewExpirationAsync(threadId);
                future.onComplete((res, e) -> {
                    if (e != null) {
                        log.error("Can't update lock " + getName() + " expiration", e);
                        return;
                    }
                    
                    if (res) {
                        // reschedule itself
                        // 续期成功以后，再次调用自己，相当于每十秒调用一次定时任务，进行续期
                        renewExpiration();
                    }
                });
            }
        //	internalLockLeaseTime：看门狗时间除以3，发送定时任务，任务里面包含一个lua脚本带expira的设置过期时间操作
        }, internalLockLeaseTime / 3, TimeUnit.MILLISECONDS);
~~~

其实严谨来说，不叫续期，叫重置过期时间，每十秒一次定时任务，发送lua脚本，重新设置过期时间，新的过期时间还是默认看门狗时间，也就是三十秒。

调用getlock方法会在redis数据库中存储一个Hash数据结构的键值，键的名字就是getlock()方法中指定的名字，由于是hash数据结构，除了本身的名字key以外，内部还有一个key和value，key是一串字符串+线程id，值是1。

### 缓存与数据库的数据一致性问题

双写模式：数据库改完以后，直接修改缓存中的数据

失效模式：数据库改完以后，直接删除缓存中的数据，下次请求来了以后，直接查库，获取最新数据，然后在添加到缓存中

### 接口幂等性

同一个接口，多次发出同一个请求，必须保证操作只执行一次。以及使用Feign远程调用的时候失败了进行重试，会造成系统无法承受的损失。比如说：支付订单，重复支付导致多次扣钱，创建订单接口，用户由于网络原因，点了很多次确认订单，那么会重复创建出好多个订单

接口幂等性的解决方案：

1.唯一索引：使用唯一索引来避免脏数据的添加，比如说订单表中的订单号字段，一般来说都是唯一的，那么就要加一个唯一索引来约束，当插入重复数据的时候数据库会抛异常，保证了数据的唯一性。

2.乐观锁：对于更新的sql语句，可以通过加一个version字段来控制版本，当数据库更新的时候，先看一下此时的version版本号

这里的乐观锁指的是使用乐观锁的思想去解决问题，并没有显示地去加任何锁，有点类似于CAS比较并交换

~~~wiki
select version from tablename where xxx

update tablename set count=count+1,version=version+1 where version=#{version}
~~~

3.悲观锁，在获取数据的时候进行加锁，当同时有多个重复请求的时候，阻塞，不允许执行

4.分布式锁：在分布式环境下，锁定一个全局唯一资源，使请求串行化，实际表现为互斥锁，防止重复，解决接口幂等

5.token机制：token机制的核心思想是为每一次操作生成一个唯一性凭证，一个token在一个操作中只有一次执行权，拿到token后比对成功后需要立即删除，而不是等业务执行完再删除，同时也要注意获取token，比对token，删除token这三个操作一定是一个原子性的操作

### 分布式事务

本地事务在分布式微服务中出现的问题：

1.远程服务假失败，远程服务自己其实执行成功了，但是由于是远程调用，如果出现网络抖动导致feign远程调用的时候数据回传出现了问题，那么订单服务感知到了异常，所以订单服务会回滚，但是库存服务不回滚

2.远程服务返回成功，接着执行下面的逻辑，下面逻辑出现异常，导致回滚，远程服务依然不会回滚，导致服务逻辑出现异常

### 分布式理论

CAP代表的分别是一致性可用性和分区容错性

一致性：所有节点访问同一份最新的数据副本

可用性：非故障的节点在合理的时间内返回合理的相应（不是错误或者超时的响应）

分区容错性：分布式系统出现网络分区的时候，仍然能够对外提供服务

至于经常说的三选二，有些地方会说一致性，可用性，分区容错性只能三个达到其中的两个，不可能三个同时达到。

这是不准确的，其实应该是当发生网络分区时，如果要继续对外提供服务，那么强一致性和可用性只能二选一，所以这个P是一定得保证的，那么只能是AP或者CP。

也就是说CAP理论认为网络分区是一定会出现的，那么出现了网络分区这种问题，我们不能让整个系统都停掉，所以分区容错必须满足

原因是，若系统出现了分区，系统中的某个节点在进行写操作，为了强一致性，那么就需要禁止其他节点的读写操作，那就无法保证其他节点的可用性，如果为了保证其他节点正常，而且分区还容错，也就是出现网络分区时，系统仍然能够对外提供服务，那么就会造成分区断连的节点与那边正在处理写操作的网络分区中的节点造成数据不一致的情况

对于nacos注册中心，既支持CP也支持AP

CA不存在的原因是，如果发生了分区容错，为了保证一致性，系统必须禁止写入，违背了可用性，如果为了保证可用性，那么就会出现正常的分区可以写入数据，有故障的分区不能写入数据，那么就违背了一致性

BASE理论是基本可以，软状态和最终一致性。

base理论的核心思想是即使无法做到强一致性，但每个应用都可以根据自身的业务特点，采用适当的方式来使系统达到最终一致性，也就是牺牲数据的一致性来满足系统的高可用性，系统中的部分数据不可用或者出现不一致时，仍然要保证系统的整体主要可用

base可以说是cap的一个延伸，或者说是对ap方案的一个补充

ap方案是在发生网络分区时，放弃一致性，仍然提供系统的可用性，但是不是永远的放弃一致性，在分区故障恢复以后，系统应该达到最终一致性，这一点就是base延伸的地方

1.基本可用：就是当系统中的部分节点出现故障时，允许损失部分可用性，但整体系统要处于可用状态

2.软状态：允许系统中的数据存在中间状态，也就是cap理论中的数据不一致。并认为这种中间状态不会 影响系统的整体可用性，即允许系统在不同节点的数据副本之间进行数据同步的过程存在延时

3.最终一致性就是系统中的所有数据副本，在经过一段时间后，最终能够达到一种一致性状态，而不需要保证数据的强一致性

### docker

对于docker，我们经常跟容器联系在一起，那么对于容器来说，一句话总结就是：容器就是将软件打包成一个标准化单元，用于开发交付和部署

容器镜像是轻量的，可执行的独立软件包，既然可以独立运行，那么他就应该包含运行所需的所有内容：代码，运行环境，系统工具，系统库和设置等

那么这种容器化软件应该适用于linux和windows的应用，在任何环境中都能做到始终如一的运行

容器赋予了软件独立性，使其免受外在环境差异，从而减少团队间在相同基础设施上运行不同软件时的冲突

~~~wiki
容器虚拟化的是操作系统而不是硬件，容器之间是共享同一套操作系统资源。而虚拟化技术是虚拟出一套硬件后，在其之上运行一个操作系统。
所以容器的隔离级别会低一些
~~~

一个docker容器可以把它看做是一个独立的操作系统，但是容器与容器之间是共享一个大的操作系统，当然也要共享硬件

当通过命令进入docker容器内部以后，其实会发现它就是一个linux操作系统，上面跑着我们的软件

docker使用go语言开发，基于linux内核提供的CGroup和namespace来实现，以及 AUFS 类的 **UnionFS** 等技术，对进程进行一个封装隔离，属于操作系统层面的虚拟化技术，由于隔离的进程独立于宿主和其他隔离的进程，因此也称为容器

docker能够自动执行重复的任务，例如搭建和配置开发环境，解放了开发人员

作为用户也可以很方便的创建和使用容器，把自己的应用放入到容器中，容器还可以进行版本管理，复制，分享和修改，就像普通的代码一样

docker的思想：

1.集装箱

2.标准化

3.隔离

docker的特点：

轻量：在一台机器上可以运行多个docker容器来共享宿主机的操作系统资源，他们能够迅速启动，只需占用少量的计算和内存资源

标准：docker容器基于开放式标准，能够在所有的主流linux，windows以及包括vm，裸机服务器和云服务等任何基础上做一个运行

安全：docker赋予应用的隔离性不仅限于彼此之间的隔离，还独立于底层的基础设施，因此当某一个应用出现问题的时候，也只是单个容器的问题，而不会涉及到整台机器

为什么使用docker？

docker的镜像提供了除内核外完整的运行时环境，确保了应用运行环境的一致性，所谓的“一致的运行环境”指的是：这段代码在我这台机器跑着没问题这种 情况

docker非常轻量级，可以做到秒级的启动时间，大大节约了开发测试部署的时间成本

避免公用的服务器，资源不会受到其他应用的影响，就是隔离性

弹性伸缩：善于处理集中爆发的服务器压力

迁移方便，可以很轻松的将在一个平台上运行的应用，迁移到另一个平台，而不担心运行环境的变化导致应用无法正常运行的情况

docker的概念：

1.镜像：一个特殊的文件系统

操作系统分为内核态和用户态，对于linux而言，内核启动以后会挂载root文件系统为其提供用户空间支持，而一个docker镜像就相当于一个root文件系统

docker镜像是一个特殊的文件系统，除了提供容器运行时所需的程序，库，资源，配置等文件外，还包含了一些为运行时准备的一些配置参数，如匿名卷，环境变量，用户等，镜像里面是不包含任何动态数据的，其内容在构建以后也不会再被改变

docker设计的时候，充分利用了UFS技术，将其设计为分层存储的架构，镜像实际上是由多层文件系统联合组成的

分层存储的特征可以使得镜像的复用，定制变得更为容易，甚至可以用之前构建好的镜像作为基础层，然后进一步添加新的层，定制自己所需要的内容，构建出新的镜像

2.容器：镜像运行时的实体

镜像和容器的关系，就像是面向对象程序设计中的类和实例一样，镜像是静态的定义，容器是镜像运行时的实体，容器是可以被创建，删除，停止，暂停的

容器的实质是进程，但与直接在宿主执行的进程不同，容器进程运行于属于自己的独立的命名空间，容器也跟镜像一样使用的是分层存储

容器存储层的生命周期和容器一样，容器消亡的时候，容器存储层也会随之消亡，因此任何保存于容器存储层的信息都会随容器的删除而删除

所以按照docker最佳实践的要求，容器不应该向其存储层写入数据，容器存储层要保持无状态化，所有的数据写入操作，都应该使用数据卷，或者绑定宿主目录，我在项目里是使用绑定宿主目录的方式来将向容器中写入数据的操作转移到向宿主文件系统写入。直接跳过容器存储层，使用绑定方式后，容器可以随时删除，重新run，数据是不会丢失的

3.仓库：集中存放镜像文件的地方

镜像构建完成以后，可以很容易的在当前宿主上运行，但是如果需要在其他服务器上使用这个镜像，我们就需要一个集中的存储，分发镜像的服务，**Docker Registry 就是这样的服务**

docker hub就是一个默认的docker registry，也可以使用阿里云提供的仓库服务

一个docker registry可以包含多个仓库，每个仓库可以包含多个标签，每个标签对应一个镜像，所以说：镜像仓库是docker用来集中存放镜像文件的地方，类似于github这种代码仓库

docker的常用命令：

docker version：查看版本

docker images：查看已经下载的镜像，可以通过这个来查看自己使用的镜像版本

docker container ls 等价于docker ps：查看所有正在运行的容器

docker search xxx：查看xxx的相关镜像

docker pull xxx:版本 ：拉取指定tag的镜像

docker rmi 镜像id：删除指定的镜像，前提是这个镜像没有被容器引用

补充一下docker的 -p 端口映射命令

docker run -p 8080:8080 tomcat:8.0.5

这两个端口的作用分别是：第一个端口是宿主机端口，第二个端口为容器内部的端口（每一个容器可以看做是一个虚拟化出来的操作系统），外部访问宿主机配置的端口，就可以映射到容器内部的端口

另外当容器后台运行的时候，我们无法知道容器内部的情况，可以使用docker logs -ft 容器id （-ft参数表示显示时间戳并实时打印日志）

### docker的build ship and run

build：构建镜像：镜像就像一个个集装箱一般，包括文件以及运行环境等等资源

ship：运输镜像：主机和仓库间的运输，这里仓库就像是超级码头一样

run：运行镜像：运行的镜像就可以称之为容器了，容器就是运行程序的地方

namespace是linux内核用来隔离内核资源的方式：通过namespace可以让一些进程只能看到与自己相关的一部分资源，而另外一些进程也只能看到与他们自己相关的资源，这两拨进程根本就感觉不到对方的存在，具体的实现方式就是把一个或多个进程的相关资源指定在同一个namespace中，linux namespace是对全局资源的一种封装隔离，使得处于不同的namespace的进程拥有独立的全局系统资源。

### k8s

在docker技术发展火热的时候，使用者会发现一个问题，如果要想将docker应用于具体的业务实现，是存在困难的，比如编排，管理，调度等，所以需要一套管理系统来对docker容器进行更加高级灵活的管理

k8s就是基于容器的集群管理平台

一个k8s系统，通常称之为一个k8s集群，包含一个master节点和若干node节点

master节点主要负责管理和控制，node节点是真正的工作负载节点

Master节点包括API Server、Scheduler、Controller manager、etcd。

API Server是整个系统的对外接口，供客户端和其它组件调用，相当于“营业厅”。

Scheduler负责对集群内部的资源进行调度，相当于“调度室”。

Controller manager负责管理控制器，相当于“大总管”。

Node节点包括Docker、kubelet、kube-proxy、Fluentd、kube-dns（可选），还有就是**Pod**。

pod是k8s最基本的操作单元，一个pod代表着集群中运行的一个进程，它内部封装了一个或多个紧密相关的容器，除了pod以外，k8s还有一个概念叫service，一个service可以看做一组提供着相同服务的pod的对外访问接口

Kubelet，主要负责监视指派到它所在Node上的Pod，包括创建、修改、监控、删除等。

docker就是创建容器的引擎

### 计算机网络

应用层的任务是通过应用进程间的交互来完成特定的网络应用，也就是说应用层的协议定义的是应用进程间的交互规则，对于不同的网络应用来说有不同的应用层协议，比如域名解析DNS协议，支持万维网的http协议，电子邮件smtp协议，文件传输协议FTP协议等

运输层的主要任务是负责向两台主机进程之间的通信提供数据传输服务，应用进程利用该服务传送应用层报文，一台主机可以同时运行多个线程，因此传输层有复用的能力。传输层主要有两种协议：TCP，提供面向连接的，可靠的数据传输服务，UDP，提供无连接的，尽量最大努力的数据传输服务

在计算机网络中进行通信的两个计算机之间可能会经过很多个数据链路，也可能会经过很多通信子网，网络层的主要目的就是选择合适的网间路由和交换节点，确保数据及时传送，在发送数据的时候，网络层把运输层产生的报文段或用户数据报封装成分组和包的形式进行发送，在tcp/ip体系结构中，由于网络层使用的协议是ip协议，因此分组也叫ip数据报

数据链路层通常称为链路层，两台主机间的数据传输，总是在一段一段的链路上进行的，这就需要有专门的链路层协议，在两个相邻节点间传送数据时，数据链路层要将网络层交下来的ip数据包封装成帧，在相邻的两个节点间的链路上传送帧，每一个帧都包括数据和必要的控制信息（如同步信息，地址信息，差错控制等）

物理层的作用是实现相邻计算机节点之间比特流的透明传输，尽可能屏蔽掉传输介质和物理设备的差异，使其上面的数据链路层不必考虑网络的具体介质是什么，透明传送比特流表示经过实际电路传送后的比特流没有发生变化，对传送的比特流来说，这个电路好像是看不见的

### TCP连接

三次握手

在建立连接之前，服务器必须做好连接的准备，通过调用socket，bing，listen，accept四个函数来完成绑定公网ip，监听443或者80端口和接收请求的任务

然后客户端通过socket和connect两个函数来主动打开连接，给服务器发送带有syn标志的分组，随机生成一个初始序列号x，以及附带MSS（最大段的大小）等额外信息，为了避免在网络层被ip协议分片使得使得丢失错误的概率增加，以及为了达到最佳的传输效果，mss的值一般以 以太网MTU（最大传输单元）的值减去ip头部和tcp头部大小，等于1460字节

TCP的单个数据报的最大长度称为MSS，在tcp建立连接的时候会有一个mss协商的过程，双方在发送syn的时候都会在tcp头部写入自己能支持的最大mss

服务端收到客户端发来的分组，服务端会向客户端返回带有syn+ack标志的分组，随机生成一个初始序列号y，并且确认号是x+1，以及附带mss等额外信息，当一端收到另一端的mss值，会根据两者的mss取一个小值来确定随后的tcp最大报文段大小

客户端收到服务端发来的分组以后，再向服务端返回带有ack标志的分组，同时确认号为y+1，至此tcp三次握手完成，客户端与服务端之间建立起了tcp连接

为什么需要三次握手？

三次握手目的是建立可靠的通信信道，通信就是数据发送和接收的过程，那么三次握手可以保证两端都处在正常的收发状态

经过第一次握手，客户端什么都不能确认，server确认了client发送正常，自己接收正常

第二次握手，client知道自己发送正常，接收正常，对方发送接收也都正常，server不变，依然只是能确认client发送正常，自己接收正常

第三次握手，server这边才会知道自己发送正常，对方接收正常，至此两边都能知道自己接发正常，对方接发是否正常

三次握手完毕以后客户端与服务端之间的连接正式建立，对于http1.1来说，默认是长连接，长连接指的是tcp长连接，也就是说一次请求过程中建立的连接在请求响应完成以后不会立即断开，以便来处理不久后到来的新请求，无需为新请求建立连接增加开销，提高了网络的吞吐量，同时这个长连接也是有时间限度的，浏览器会每隔45秒向服务器发送一个tcp keep-alive探测包，如果没有收到ack，那么就会断开连接，避免连接风暴

四次挥手

对于任意一方都可以在数据传送结束后发出连接释放的通知，我以服务端为例：

服务端通过调用close函数来主动关闭连接，向客户端发送带有fin标志位的分组，序列号为m

客户端收到该分组，向服务端发送带有ack标志的分组，确认号为m+1

客户端发送完所有数据以后，向服务器发送带有fin标志的分组，序列号为n

服务器确认收到该分组，向客户端返回一个带有ack的分组，序列号为n+1，（被关闭方）客户端收到确认分组以后，立即进入closed状态，（主动关闭方）服务端会等待2msl，2个最大报文生存时间后，进入closed状态

为什么四次挥手后需要等待2msl（time_wait）才会进入closed状态？

因为主动关闭方在发送最后一个ack的时候，被动方可能没有收到，如果被动方在发送最后一个fin后，等待一段时间没有收到ack应答，那么会重新发送第三次挥手的fin分组，主动方会重试发送最后一次ack

这个time_wait状态就是防止被动方没有收到主动方发送的第四次挥手，ack，从而重新向主动方发送fin，等着主动方回一个ack

MSL：即最大报文段生存时间，即任何tcp报文在网络中存在的最大时长，如果超过这个时长，这个tcp报文就会被丢弃

至于为什么是2msl？

因为主动方不知道被动方到底有没有收到ack，被动方如果没有收到ack，那么会进行重发fin，那么最坏的情况是第四次挥手的ack生存时间+服务端重传fin后fin的最大生存时间，就是2msl

一定注意：被动关闭方应该是最后收到主动发来的ack的，所以被动方一旦收到ack，立马进入closed状态，而主动方不知道它到底有没有收到，所以主动方要等待2msl的time_wait状态，但是被动方如果没有收到ack，那么也不会进入closed，而是重新发送fin。

另外每次收到被动方发来的fin，主动方回一个ack后，都会重置2msl时间

### TCP与UDP

udp在传送数据之前不需要先建立连接，对方在收到udp报文后，也不需要做出任何确认，虽然udp不提供可靠的数据传输，但是在语音通话，视频通话，直播等场景还是会使用，这些场景对数据的一致性要求没那么高

tcp提供面向连接的服务，在传送之前必须先建立连接，也就是三次握手的过程，数据传送结束后需要释放连接，tcp不提供广播或者多播服务，由于tcp要提供可靠的，面向连接的传输服务，tcp的可靠体现在tcp传输数据前需要三次握手来建立连接，而且在数据传输时，有确认，窗口，重传，拥塞控制机制，在数据传送完毕后，还会断开连接来节约系统的资源，这些保证机制难免会造成协议数据单元的首部增大很多，占用处理机资源，tcp一般用于文件传输，发送和接收邮件等场景

DNS使用udp的最大原因应该是不需要建立连接就能发送数据，节省了建立连接的时延，如果dns运行在tcp上，就会慢很多

而http选择与tcp搭配使用，目的是对于文本数据来说，可靠性是非常重要的，至于说建立连接，tcp也有长连接模式，可以尽可能的减少建立连接带来的时延

### TCP协议如何保证可靠传输

1.应用数据会被分割成tcp认为最适合发送的数据块大小，也就是建立连接过程中的mss协商过程

2.tcp会给发送的每一个包进行编号，接收方对数据包进行排序，把有序的数据传送给应用层

3.校验和：tcp将保持它首部和数据的校验和，这是一个端到端的校验和，目的是检测数据在传输过程中的任何变化，如果收到段的检验和有差错，tcp将丢弃这个报文段和不确认收到此报文段

4.流程控制：tcp连接的每一方都有固定大小的缓冲空间，tcp的接收端只允许发送端发送缓冲区能够接收的数据，当接收方来不及处理发送方的数据时，能提示发送方降低发送的速率，防止包丢失，tcp使用的流量控制协议是可变大小的滑动窗口协议，tcp利用滑动窗口来实现流量控制

5.拥塞控制：当网络拥塞时，减少数据的发送

6.超时重传：当tcp发出一个段后，启动一个定时器，等待目的端确认收到这个报文段，如果不能及时收到一个确认，将重发这个报文段

7.ARQ协议：自动重传请求，也是为了可靠传输，它的基本原理就是每发送完一个分组就停止发送，等待对方的确认，在收到确认后再发送下一个分组。他是使用确认和超时这两个机制，如果发送方在发送完以后一段时间没有收到确认帧，它通常会重新发送

### ARQ协议

自动重传请求协议是数据链路层和运输层的错误纠正协议之一，它通过使用确认和超时这两个机制，在不可靠服务的基础上实现可靠的信息传输。

停止等待ARQ：

停止等待协议是为了实现可靠传输的，它的基本原理就是每发完一个分组就停止发送，等待对方确认（回复 ACK）。如果过了一段时间（超时时间后），还是没有收到 ACK 确认，说明没有发送成功，需要重新发送，直到收到确认后再发下一个分组。在停止等待协议中，若接收方收到重复分组，就丢弃该分组，但同时还要发送确认。

优点就是简单

缺点但是信道利用率极低，等待时间长

连续ARQ：（流水线传输）

连续ARQ为了提高信道的利用率，发送方维持一个发送窗口，凡是位于发送窗口内的分组可以连续发送出去，而不需要等待对方的确认，接收方一般采用累计确认，对按序到达的最后一个分组发送确认，表明到这个分组为止的所有分组都已经被正确的收到了

优点：信道利用率高

缺点：不能向发送方反映出接收方已经正确接收到的分组，比如，发送方发送了5条消息，中间第三条丢失了，接收方只能对前两个做出确认，发送方自己不知道另外的三条的下落，而只好把后面的三个进行重传，这也叫go back N，表示需要退回来重新传送N个消息

