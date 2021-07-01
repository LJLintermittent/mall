#                                                  Mall-LJLintermittent

#### 介绍
Mall-LJLintermittent是一套电商系统，包括mall商城系统和mall-admin-vue商城后台管理系统。

主要模块有商品展示，商品分类，商品检索，秒杀商品，购物车，订单结算，订单流程，个人订单查看，库存管理，订单管理，秒杀上架，商品分类管理，商品采购，商品上架，建立商品。

借鉴于尚硅谷电商项目《谷粒商城》，GitHub开源项目：《mall》，《新蜂商城》，《秒杀》，并结合自己的业务需求做额外开发的一套涵盖商城后台管理系统，商城前台系统的个人学习项目。

#### 软件架构
商城后台管理系统：使用人人开源前端代码模板快速搭建，采用Vue-ElementUi编写前端基本组件，基于Springboot，Mybatis-plus，Mysql为主要架构进行后端接口的开发，使用gateway网关做路径转发与路径重写，Nginx反向代理，动静分离

商城前台系统：在后台管理系统技术栈的基础上加了Redis实现缓存，分布式信号量，分布式锁，存放共享Session等功能，使用RabbitMQ实现流量削峰，解耦，使用ElasticSearch做前台商城的商品检索与上架商品的存储，kibana可视化管理。使用SpringCloud及SpringCloudAlibaba服务进行微服务治理，如Sentinel降级，熔断，Nacos服务注册与发现，配置中心，Sleuth+zipkin服务链路追踪，短信验证码服务使用阿里云提供的短信服务，图片文件上传使用阿里云Oss对象存储，订单支付业务使用支付宝沙箱支付，支付宝支付成功的回调使用哲西云的内网穿透服务。

系统架构图：

![image](https://note.youdao.com/yws/api/personal/file/WEBe6432858caeb0ddb75433a1f6b2c1037?method=download&shareKey=a66cfb247580976bee9355f94039fcb1)

服务架构图：

![image](https://note.youdao.com/yws/api/personal/file/WEBfdcf1a98ca94078c456718355a5d9d8e?method=download&shareKey=f935cb41d80c6b2e417d3fe1f35bb61f)

服务组织图：

![image](https://note.youdao.com/yws/api/personal/file/WEB839873a3b074cd53bf8a8d770113b53f?method=download&shareKey=391696f3439fa07040410acd1f17b5d6)

#### 项目演示

由于项目并未在云服务器部署上线，所以暂无法提供在线访问，开发完毕的展示图如下: (**GitHub访问可能会由于网络原因导致图片加载不完整，建议使用Gitee查看  https://gitee.com/LJLintermittent/mall**)

Kubernetes集群整合kubeSphere可视化管理：

![image](https://note.youdao.com/yws/api/personal/file/WEBbf7593d7459fa775dfd2affcf4b90f73?method=download&shareKey=476e359045d9c470df76d13f5da8d8b9)

![image](https://note.youdao.com/yws/api/personal/file/WEBe894b5bc73812399678bb0827ffe8d10?method=download&shareKey=bd5074dd484b91b0edcc84f6ddbde6d4)

![image](https://note.youdao.com/yws/api/personal/file/WEB247be7fc38e621ea421da101d05634f8?method=download&shareKey=cb9c209e46625775a68ff06bf90f54e9)

![image](https://note.youdao.com/yws/api/personal/file/WEB2e151079731eb72754d8b195c81caeab?method=download&shareKey=b76a1c1f54627182c8d7dc61bc94b998)

![image](https://note.youdao.com/yws/api/personal/file/WEB3b51d51c2b410882f25d627f3791401b?method=download&shareKey=b38a4f64a4f46a5bd75e656c2cd896f7)

![image](https://note.youdao.com/yws/api/personal/file/WEBa97db931858ab77aba83f7d634ff486a?method=download&shareKey=7a600e66286f41fb2921096351f2f313)

![image](https://note.youdao.com/yws/api/personal/file/WEB82de1d891ce4190fe44ccd5bdd68ac51?method=download&shareKey=f0999fdf438237a721b952f8c90de58a)

后台管理系统：

![image](https://note.youdao.com/yws/api/personal/file/WEBd99b4e3cfa32133787c03de2fe39e5c5?method=download&shareKey=2afa20e682cc807410dbe3ac753db0c0)

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E5%90%8E%E5%8F%B0%E7%AE%A1%E7%90%86%E7%B3%BB%E7%BB%9F%E5%95%86%E5%93%81%E7%AE%A1%E7%90%86%E9%A1%B5.png)

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E5%90%8E%E5%8F%B0%E7%AE%A1%E7%90%86%E7%B3%BB%E7%BB%9F%E8%A7%84%E6%A0%BC%E5%8F%82%E6%95%B0%E9%A1%B5.png)

![image](https://note.youdao.com/yws/api/personal/file/WEB4b500524b5e0384176c1a811dc044b35?method=download&shareKey=3a536cc3bd9d260f8ef8ee9fd35e309b)

![image](https://note.youdao.com/yws/api/personal/file/WEBfbff1d912387ef9ae789bb06682cc3da?method=download&shareKey=0d570b3922a6c90608646a56311af787)

前台商城系统首页：

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E5%95%86%E5%9F%8E%E9%A6%96%E9%A1%B5.png)

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E5%95%86%E5%9F%8E%E9%A6%96%E9%A1%B5%E5%AE%8C%E6%95%B4%E5%9B%BE.png)

商品检索页：

![image](https://note.youdao.com/yws/api/personal/file/WEB15985f4584a260ac31da914c102ad6d1?method=download&shareKey=7c8ca378c06abc4e4c0022b0310bc583)

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E5%95%86%E5%93%81%E6%A3%80%E7%B4%A2%E9%A1%B5%28%E4%B8%8B%29.png)

商品详情页:

![image](https://note.youdao.com/yws/api/personal/file/WEBbd5d3a46933d4ce2ddb1e4d0378a7c9f?method=download&shareKey=02741fd5bc0c1322ef28e0479e293db5)

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E5%95%86%E5%93%81%E8%AF%A6%E6%83%85%E9%A1%B5%28%E4%B8%8B%29.png)

购物车：

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E8%B4%AD%E7%89%A9%E8%BD%A6.png)

添加购物车成功页：

<img src="https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E6%B7%BB%E5%8A%A0%E8%B4%AD%E7%89%A9%E8%BD%A6%E6%88%90%E5%8A%9F%E9%A1%B5.png" alt="image" style="zoom:50%;" />

购物车列表页：

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E8%B4%AD%E7%89%A9%E8%BD%A6%E5%88%97%E8%A1%A8%E9%A1%B5.png)

选择收货地址页：

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E9%80%89%E6%8B%A9%E6%94%B6%E8%B4%A7%E5%9C%B0%E5%9D%80%E9%A1%B5.png)

选择支付页：

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E9%80%89%E6%8B%A9%E6%94%AF%E4%BB%98%E5%AE%9D%E6%94%AF%E4%BB%98%E9%A1%B5.png)

支付宝支付页：

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E8%BE%93%E5%85%A5%E5%AF%86%E7%A0%81%E9%A1%B5.png)

支付成功等待跳转：

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E7%AD%89%E5%BE%85%E8%B7%B3%E8%BD%AC%E9%A1%B5.png)

用户订单列表：

![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/%E8%AE%A2%E5%8D%95%E9%A1%B5.png)

秒杀商品详情页(如果此商品为秒杀商品，在价格后面会带上秒杀价，并且加入购物车按钮会变为立即抢购)：

![image](https://note.youdao.com/yws/api/personal/file/WEBd116d62b0d385a25855d583989d68247?method=download&shareKey=ce2d498848f624345671a422ed26a435)

微服务周边治理：

* Nacos

  ![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/nacos.png)

* Sentinel

  ![image](https://note.youdao.com/yws/api/personal/file/WEBe64e01567f138edb024090b52440e122?method=download&shareKey=ab4eb4d53fb5d7e174d2ba5f95275474)

* Zipkin

  ![image](https://mall-ljl.oss-cn-beijing.aliyuncs.com/mall/mallp/zipkin.png)

  

#### 
