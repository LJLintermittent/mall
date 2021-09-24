### Elasticsearch 

Elasticsearch在7版本之前，关于数据的存储涉及到三个概念，分别是index，type，document，索引类型和文档

可以简单的理解为对应mysql的库，表，行

但是7开始类型属性被标记过时状态，并且在8中将会彻底删除

主要原因是：对于关系型数据库来说，两个数据表之间是一定独立的，即使它们具有相同的列名，但是es不是这样，es中不同的type下名称相同的filed最终在Lucene中的处理方式是一样的

也就是说两个不同type下的两个user_name，在es同一个索引下会被认为是一个filed，所以必须在不同的type中的定义相同的filed映射，否则不同type的相同字段名称就会出现处理冲突，导致Lucene处理速率下降

所以删除type的目的就是提升es的处理效率

使用DSL做检索最主要的就是编写DSL语句，领域特定语言，这是一种可以执行查询的json风格的语言，被称为query DSL，es可以在query中组合非常多的查询类型来完成复杂的查询

还有排序，分页，高亮显示等功能

##### query/match匹配查询：

如果是非字符串，会进行精确匹配，如果是字符串的话，会进行全文检索

如果是对字符串进行全文检索，最终会按照评分进行排序，会对检索条件进行分词匹配

##### `query/match_phrase` [不拆分匹配]

将需要匹配的值当成一整个单词，不分词进行匹配，只有全部匹配上才算检索成功

~~~json
GET bank/_search
{
  "query": {
    "match_phrase": {
      "address": "mill road"   #  就是说不要匹配只有mill或只有road的，要匹配mill road一整个子串
    }
  }
}
~~~

如果match匹配在匹配字段后面加了keyword，那么表示对于字符串要精确匹配，也就是只能有这个字符串，不能再有其他东西

~~~json
GET bank/_search
{
  "query": {
    "match": {
      "address.keyword": "990 Mill Road"  # 正好有这条文档，所以能匹配到
    }
  }
}
~~~

##### query/multi_math【多字段匹配】

~~~json
GET bank/_search
{
  "query": {
    "multi_match": {  # 前面的match仅指定了一个字段。
      "query": "mill",
      "fields": [ # state和address有mill子串  不要求都有
        "state",
        "address"
      ]
    }
  }
}
~~~

**state或者address中包含mill**，并且在查询过程中，会对于查询条件进行分词。

##### query/bool/xxxx复合查询

bool查询是一种复合查询，复合语句之间可以互相嵌套，可以组合出非常复杂的查询语句

```wiki
Bool查询对应Lucene中的BooleanQuery，它由一个或多个子句组成，每个子句都有特定的类型
must：返回的文档必须满足must子句的条件，并且参与计算分值
filter：返回的文档必须满足filter子句的条件。但是不会像Must一样，参与计算分值
should：返回的文档可能满足should子句的条件。在一个Bool查询中，如果没有must或者filter，
有一个或者多个should子句，那么只要满足一个就可以返回。minimum_should_match参数定义了至少满足几个子句。
must_not：返回的文档必须不满足must_not中的定义
```

##### query/term

全文检索用match，精确匹配用term，非text字段用term，es默认存储text值时会分词分析，所以要搜索text值，要使用match

至此前面的都是检索存储，还没有做分析

es的聚合提供了从数据中分组和提取数据的能力，最简单的聚合大致等同于mysql的group by

~~~json
"aggs":{ # 聚合
    "aggs_name":{ # 这次聚合的名字，方便展示在结果集中
        "AGG_TYPE":{} # 聚合的类型(avg,term,terms)
     }
}
~~~

### Mapping字段映射

在存储数据之前，首先应该定义数据模型的映射，映射是定义文档如何被存储和检索的

1.字符串：

text，用于全文检索的，搜索时会自动使用分词器进行分词再匹配，如果定义某一个属性的type为text，那么最好指定一个分词器，比如ik_smart分词器，

keyword，不分词，搜索时需要匹配完整的值

一般最主要的区分也是这两个，其他的类型根据数据是什么样的去定义就好了，比如是否有库存这个属性，hasstock，那么他就是一个boolean类型，比如skuid，他就是一个long型

### 项目中使用ES

需求：

1.上架的商品才可以展示在商城中

2.上架的商品可以被检索

检索的时候输入skutitle，进行全文检索（首页检索输入框）

进入商品总览页后，可以根据属性规格进行进一步检索，属性规格是属于spu的，以spu为单位进行一个划分，所以最终在es中存储的数据结构应该是

~~~json
{
    skuId:1
    spuId:11
    skyTitile:华为xx
    price:999
    saleCount:99
    attr:[
        {尺寸:5},
        {CPU:高通945},
        {分辨率:全高清}
	]
}
~~~

类似于这样的数据模型

业务1 商品上架

首先商品服务要把后台管理系统中需要上架的商品信息收集起来，建立一个专门的vo，数据模型，传给search服务，做一个bulkrequest，然后使用rest-high-level-client将请求发送给es，做一个数据的保存。

商品服务要根据spuid为基准，也就是说上架一个商品，是以标准产品单元为单位进行上架的，那么上架前要查询当前spuid所对应的所有sku信息，同时还要查询每个sku是否有库存，然后会根据每件商品对应的库存往skuEsModel数据模型中设置，其实即使没有库存也会发上去的

检索的话主要是根据以下四种需求进行添加，哪部分不为空就把哪部分组装进DSL语句中

- 全文检索：skuTitle-》text
- 排序：saleCount（销量）、hotScore（热度分）、skuPrice（价格）
- 过滤：hasStock、skuPrice区间、brandId、catalog3Id、attrs
- 聚合：attrs

### ES倒排索引

倒排索引直白来说就是根据value来找key

Term index，Term Dictionary，Posting list

假设有一个文档

docid  年龄  性别

1        18       男

2        20       女

3        18        男

那么每一行就是一个文档，每个文档有一个docid，那么可以根据每一个属性在哪个文档中出现过来建立倒排索引

所以倒排索引的第一个关键因素是每一个属性都要建立一个倒排索引

比如 18   [1,3] 代表属性值为18的数据，在文档1和文档3出现过

像18,20这些属性具体值，就叫做term，而[1,3]叫做posting list，也就是一个int数组，存储了所有符合某个term的文档id

假设我们有很多个term，比如：Carla,Sara,Elin,Ada,Patty,Kate,Selena

那么按照这样的顺序来排列的话，找出某个特定的term会非常的慢，因为这些term没有排序，需要全部遍历一遍才能找出特定的term，如果排序了，就可以用二分查找很快地找出特定的term，这个就是term dictionary，有了term dictionary之后，就可以用logN次磁盘查找得到目标，而磁盘的随机访问是非常慢的，放到内存里又会出现放不下的情况，于是就有了term index，term index是一个前缀树，前缀树不会包含所有的term，它包含的是term的一些前缀，通过term index可以很快的定位到term dictionary的某个offset，然后再从这个位置开始查找，再加上Lucene的一些压缩技术，term index 的尺寸是非常小的，使用内存完全可以存下整个term index。

所以倒排索引的整体流程就是通过term index 找到term dictionary的某个offset，然后具体的offset就是一个一个的term，每一个term会建立一个倒排索引，也就是value指向key，key是posting list，也就是文档的id数组

所以如果要回答为什么Elasticsearch/Lucene检索可以比mysql快。答案是mysql 只有term dictionary这一层，是以B+树的方式存储在磁盘上，检索一个term需要若干次的磁盘随机访问，而es将term index放在了内存中，可以很快的找出term dictionary的位置，然后在磁盘上直接找到对应的posting list，大大缩短了磁盘io的时间，并且前缀树还是在内存中，速度非常快

