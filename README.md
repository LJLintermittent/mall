![fFcOyt.png](https://z3.ax1x.com/2021/08/04/fFcOyt.png)

![fFczTS.png](https://z3.ax1x.com/2021/08/04/fFczTS.png)

[![fFgCWj.png](https://z3.ax1x.com/2021/08/04/fFgCWj.png)](https://imgtu.com/i/fFgCWj)

## 介绍

《mall电商》是一套商城系统，包括mall商城前台系统和mall-admin-vue商城后台管理系统。

《mall电商》致力于打造一套集商品系统，检索系统，认证系统，库存系统，订单系统，秒杀系统，第三方服务系统(短信验证码，支付宝沙箱支付等)，网关路由系统为一体的分布式微服务架构的前后端分离的开源商城系统。

**项目部分技术实现的参考来源：**

1.尚硅谷电商教程《谷粒商城》

2.新蜂商城：https://github.com/newbee-ltd/newbee-mall-plus

3.mall：https://github.com/macrozheng/mall

4.miaosha：https://github.com/qiurunze123/miaosha

## 前言

**项目仅为个人学习所做，目前依然有一些TODO和BUG等待完成和修改，仅供参考**

**商城前台系统：**

* 商城前台系统的前端页面直接使用视频教程提供的静态HTML页面，并进行了一定的修改适配，不建议参考
* 商城前台系统的后端代码完成度较高，可以作为参考

**商城后台管理系统：**

* 商城后台管理系统的前端页面基本完成，可以作为参考
* 商城后台管理系统的后端代码完成度较高，可以作为参考

## 技术选型（仅列部分重要组件）

|      技术      |        版本        |                说明                |
| :------------: | :----------------: | :--------------------------------: |
|      Java      |       JDK1.8       |            后端开发语言            |
|     Vue.JS     |        2.x         |            渐进式JS框架            |
|     Maven      |       3.5.4        |            项目构建工具            |
|  Spring Boot   |       2.1.8        |            MVC核心框架             |
|  MyBatis-Plus  |       3.2.0        |              ORM框架               |
| validation-api |    2.0.1.Final     |              验证框架              |
|    Redisson    |       3.12.0       |  对redis进行封装、集成分布式锁等   |
|     Redis      |       6.0.10       |             内存数据库             |
|     Nacos      |       1.1.3        |         服务注册与配置中心         |
|    Gateway     | 同Spring Cloud版本 |              API网关               |
|    Sentinel    | 同Spring Cloud版本 |         服务熔断&限流&降级         |
| Seluth+Zipkin  | 同Spring Cloud版本 |              链路追踪              |
|  SpringCloud   |   Greenwich.SR3    |       微服务治理综合解决方案       |
| SpringSession  | 同Spring Boot版本  |           分布式Session            |
|  SpringCache   | 同Spring Boot版本  |           基于注解的缓存           |
| ElasticSearch  |       7.4.2        |           分布式检索引擎           |
|    RabbitMQ    |     management     |              消息队列              |
|     MySQL      |        5.7         |   OLTP应用数据库(InnoDB存储引擎)   |
|     Nginx      |        1.10        | 反向代理服务器，负载均衡，动静分离 |

## 软件架构

* 系统架构图：

![image](https://note.youdao.com/yws/api/personal/file/WEBe6432858caeb0ddb75433a1f6b2c1037?method=download&shareKey=a66cfb247580976bee9355f94039fcb1)

* 服务架构表：

|    微服务模块    |                 功能                 |
| :--------------: | :----------------------------------: |
| mall-auth-server |             认证中心服务             |
|    mall-cart     |              购物车服务              |
|   mall-common    | 项目公共依赖，公共配置，公共枚举类等 |
|   mall-coupon    |              优惠券服务              |
|   mall-gateway   |             API网关服务              |
|   mall-member    |               用户服务               |
|    mall-order    |               订单服务               |
|   mall-product   |               商品服务               |
|   mall-search    |               检索服务               |
|   mall-seckill   |               秒杀服务               |
| mall-third-party |              第三方服务              |
|    mall-ware     |               库存服务               |
|   renren-fast    | 人人开源后台管理系统（搭建基本结构） |
| renren-generator |         人人开源-代码生成器          |

## 项目演示

由于项目并未在云服务器部署上线，所以暂无法提供在线访问，开发完毕的展示图如下: (**GitHub访问可能会由于网络原因导致图片加载不完整，建议使用Gitee查看  https://gitee.com/LJLintermittent/mall**)

* Kubernetes集群整合kubeSphere可视化管理

![image](https://note.youdao.com/yws/api/personal/file/WEBbf7593d7459fa775dfd2affcf4b90f73?method=download&shareKey=476e359045d9c470df76d13f5da8d8b9)

![image](https://note.youdao.com/yws/api/personal/file/WEBe894b5bc73812399678bb0827ffe8d10?method=download&shareKey=bd5074dd484b91b0edcc84f6ddbde6d4)

![image](https://note.youdao.com/yws/api/personal/file/WEB247be7fc38e621ea421da101d05634f8?method=download&shareKey=cb9c209e46625775a68ff06bf90f54e9)

![image](https://note.youdao.com/yws/api/personal/file/WEB2e151079731eb72754d8b195c81caeab?method=download&shareKey=b76a1c1f54627182c8d7dc61bc94b998)

![image](https://note.youdao.com/yws/api/personal/file/WEB3b51d51c2b410882f25d627f3791401b?method=download&shareKey=b38a4f64a4f46a5bd75e656c2cd896f7)

![image](https://note.youdao.com/yws/api/personal/file/WEBa97db931858ab77aba83f7d634ff486a?method=download&shareKey=7a600e66286f41fb2921096351f2f313)

![image](https://note.youdao.com/yws/api/personal/file/WEB82de1d891ce4190fe44ccd5bdd68ac51?method=download&shareKey=f0999fdf438237a721b952f8c90de58a)

* 后台管理系统

![image](https://note.youdao.com/yws/api/personal/file/WEBd99b4e3cfa32133787c03de2fe39e5c5?method=download&shareKey=2afa20e682cc807410dbe3ac753db0c0)

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E5%90%8E%E5%8F%B0%E7%AE%A1%E7%90%86%E7%B3%BB%E7%BB%9F%E5%95%86%E5%93%81%E7%AE%A1%E7%90%86%E9%A1%B5.png)

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E5%90%8E%E5%8F%B0%E7%AE%A1%E7%90%86%E7%B3%BB%E7%BB%9F%E8%A7%84%E6%A0%BC%E5%8F%82%E6%95%B0%E9%A1%B5.png)

![image](https://note.youdao.com/yws/api/personal/file/WEB4b500524b5e0384176c1a811dc044b35?method=download&shareKey=3a536cc3bd9d260f8ef8ee9fd35e309b)

![image](https://note.youdao.com/yws/api/personal/file/WEBfbff1d912387ef9ae789bb06682cc3da?method=download&shareKey=0d570b3922a6c90608646a56311af787)

* 商城前台系统首页

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E5%95%86%E5%9F%8E%E9%A6%96%E9%A1%B5.png)

![fFgy0f.png](https://z3.ax1x.com/2021/08/04/fFgy0f.png)

* 商品检索页

![image](https://note.youdao.com/yws/api/personal/file/WEB15985f4584a260ac31da914c102ad6d1?method=download&shareKey=7c8ca378c06abc4e4c0022b0310bc583)

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E5%95%86%E5%93%81%E6%A3%80%E7%B4%A2%E9%A1%B5%28%E4%B8%8B%29.png)

* 商品详情页

![image](https://note.youdao.com/yws/api/personal/file/WEBbd5d3a46933d4ce2ddb1e4d0378a7c9f?method=download&shareKey=02741fd5bc0c1322ef28e0479e293db5)

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E5%95%86%E5%93%81%E8%AF%A6%E6%83%85%E9%A1%B5%28%E4%B8%8B%29.png)

* 购物车

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E8%B4%AD%E7%89%A9%E8%BD%A6.png)

* 添加购物车成功后的展示

<img src="https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E6%B7%BB%E5%8A%A0%E8%B4%AD%E7%89%A9%E8%BD%A6%E6%88%90%E5%8A%9F%E9%A1%B5.png" alt="image" style="zoom:50%;" />

* 购物车列表页

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E8%B4%AD%E7%89%A9%E8%BD%A6%E5%88%97%E8%A1%A8%E9%A1%B5.png)

* 确认收货地址页

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E9%80%89%E6%8B%A9%E6%94%B6%E8%B4%A7%E5%9C%B0%E5%9D%80%E9%A1%B5.png)

* 支付方式选择页

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E9%80%89%E6%8B%A9%E6%94%AF%E4%BB%98%E5%AE%9D%E6%94%AF%E4%BB%98%E9%A1%B5.png)

* 支付宝沙箱支付

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E8%BE%93%E5%85%A5%E5%AF%86%E7%A0%81%E9%A1%B5.png)

* 支付成功等待跳转

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E7%AD%89%E5%BE%85%E8%B7%B3%E8%BD%AC%E9%A1%B5.png)

* 用户历史订单列表页

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E8%AE%A2%E5%8D%95%E9%A1%B5.png)

* 秒杀商品详情页(如果此商品为秒杀商品，在价格后面会带上秒杀价，并且加入购物车按钮会变为立即抢购)

![image](https://note.youdao.com/yws/api/personal/file/WEBd116d62b0d385a25855d583989d68247?method=download&shareKey=ce2d498848f624345671a422ed26a435)

**微服务周边治理：**

* Nacos

  ![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/nacos.png)

* Sentinel

  ![image](https://note.youdao.com/yws/api/personal/file/WEBe64e01567f138edb024090b52440e122?method=download&shareKey=ab4eb4d53fb5d7e174d2ba5f95275474)

* Zipkin

  ![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/zipkin.png)

  

#### 
