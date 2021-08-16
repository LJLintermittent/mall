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

~~~java
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

```java
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

