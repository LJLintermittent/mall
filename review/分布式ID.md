## 分布式唯一ID

数据分库分表后需要有一个唯一ID来标识一条数据或消息，数据库的自增ID显然不能满足需求。

特别一点的如订单、骑手、优惠券也都需要有唯一ID做标识（以美团外卖业务举例），此时就需要用到分布式唯一ID

分布式唯一ID需要的特征：

1.全局唯一性：不能出现重复的ID

2.趋势递增：在MySQL InnoDB引擎中使用的是聚集索引，由于多数RDBMS使用B-tree的数据结构来存储索引数据，在主键的选择上面我们应该尽量使用有序的主键保证写入性能

3.单调递增：保证下一个ID一定大于上一个ID，例如事务版本号、IM增量消息、排序等特殊需求

4.信息安全：如果ID是连续的，恶意用户的扒取工作就非常容易做了，直接按照顺序下载指定URL即可；如果是订单号就更危险了，竞对可以直接知道我们一天的单量。所以在一些应用场景下，会需要ID无规则、不规则。

3和4需求是互斥的，无法使用同一个方案满足

同时除了对ID号码自身的要求，业务还对ID号生成系统的可用性要求极高，想象一下，如果ID生成系统瘫痪，整个美团点评支付、优惠券发券、骑手派单等关键动作都无法执行，这就会带来一场灾难。

由此总结下一个ID生成系统应该做到如下几点：

1. 平均延迟和TP999延迟都要尽可能低；
2. 可用性5个9；
3. 高QPS。

### 常用方法介绍：

#### UUID

UUID(Universally Unique Identifier)的标准型式包含32个16进制数字，以连字号分为五段，形式为8-4-4-4-12的36个字符，示例：`550e8400-e29b-41d4-a716-446655440000`，一个16进制数字占半个字节，也就是4位，那么32个16进制数字就是16个字节128位。

优点：

- 性能非常高：本地生成，没有网络消耗。

缺点：

- 不易于存储：UUID太长，16字节128位，通常以36长度的字符串表示，很多场景不适用。
- 信息不安全：基于MAC地址生成UUID的算法可能会造成MAC地址泄露，这个漏洞曾被用于寻找梅丽莎病毒的制作者位置。

- ID作为主键时在特定的环境会存在一些问题，比如做DB主键的场景下，UUID就非常不适用：

  ① MySQL官方有明确的建议主键要尽量越短越好，36个字符长度的UUID不符合要求。(聚集索引，主键放内存中)

~~~txt
All indexes other than the clustered index are known as secondary indexes. In InnoDB, each record in a secondary index contains the primary key columns for the row, as well as the columns specified for the secondary index. InnoDB uses this primary key value to search for the row in the clustered index. If the primary key is long, the secondary indexes use more space, so it is advantageous to have a short primary key.
~~~

​      ② 对MySQL索引不利：如果作为数据库主键，在InnoDB引擎下，UUID的无序性可能会引起数据位置频繁变动，严重影响性能。

### 类snowflake方案

这种方案大致来说是一种以划分命名空间（UUID也算，由于比较常见，所以单独分析）来生成ID的一种算法，这种方案把64-bit分别划分成多段，分开来标示机器、时间等，比如在snowflake中的64-bit分别表示如下图（图片来自网络）所示：

![image](https://pic.imgdb.cn/item/621b9f5d2ab3f51d915c8e9d.png)

41-bit的时间可以表示（1L<<41）/(1000L*3600*24*365)=69年的时间，10-bit机器可以分别表示1024台机器。如果我们对IDC划分有需求，还可以将10-bit分5-bit给IDC，分5-bit给工作机器。这样就可以表示32个IDC，每个IDC下可以有32台机器，可以根据自身需求定义。12个自增序列号可以表示2^12个ID，理论上snowflake方案的QPS约为409.6w/s，这种分配方式可以保证在任何一个IDC的任何一台机器在任意毫秒内生成的ID都是不同的。

这 64 个 bit 中，其中 1 个 bit 是不用的，然后用其中的 41 bit 作为毫秒数，用 10 bit 作为工作机器 id，12 bit 作为[序列号](https://so.csdn.net/so/search?q=序列号&spm=1001.2101.3001.7020)。

第一个部分，是 1 个 bit：0，这个是无意义的。

第二个部分是 41 个 bit：表示的是时间戳。

第三个部分是 5 个 bit：表示的是机房 id，10001。

第四个部分是 5 个 bit：表示的是机器 id，1 1001。

第五个部分是 12 个 bit：表示的序号，就是某个机房某台机器上这一毫秒内同时生成的 id 的序号，0000 00000000。

~~~txt
① 1 bit：是不用的，为啥呢？
因为二进制里第一个 bit位如果是 1，那么都是负数，但是我们生成的 id 都是正数，所以第一个 bit 统一都是 0。

② 41 bit：表示的是时间戳，单位是毫秒。
41 bit 可以表示的数字多达 2^41 - 1，也就是可以标识 2 ^ 41 - 1 个毫秒值，换算成年就是表示 69 年的时间。

③ 10 bit：记录工作机器 id，代表的是这个服务最多可以部署在 2^10 台机器上，也就是 1024 台机器。
但是 10 bit 里 5 个 bit 代表机房 id，5 个 bit 代表机器 id。意思就是最多代表 2 ^ 5 个机房（32 个机房），每个机房里可以代表 2 ^ 5 个机器（32 台机器），也可以根据自己公司的实际情况确定。

④ 12 bit：这个是用来记录同一个毫秒内产生的不同 id。
12 bit 可以代表的最大正整数是 2 ^ 12 - 1 = 4096，也就是说可以用这个 12 bit 代表的数字来区分同一个毫秒内的 4096 个不同的 id。

简单来说，你的某个服务假设要生成一个全局唯一 id，那么就可以发送一个请求给部署了 SnowFlake 算法的系统，由这个 SnowFlake 算法系统来生成唯一 id。这个 SnowFlake 算法系统首先肯定是知道自己所在的机房和机器的，比如机房 id = 17，机器 id = 12。
接着 SnowFlake 算法系统接收到这个请求之后，首先就会用二进制位运算的方式生成一个 64 bit 的 long 型 id，64 个 bit 中的第一个 bit 是无意义的。接着 41 个 bit，就可以用当前时间戳（单位到毫秒），然后接着 5 个 bit 设置上这个机房 id，还有 5 个 bit 设置上机器 id。最后再判断一下，当前这台机房的这台机器上这一毫秒内，这是第几个请求，给这次生成 id 的请求累加一个序号，作为最后的 12 个 bit。
~~~

#### snowflake算法实现

~~~java
 public class IdWorker {
 
	//因为二进制里第一个 bit 为如果是 1，那么都是负数，但是我们生成的 id 都是正数，所以第一个 bit 统一都是 0。
 
	//机器ID  2进制5位  32位减掉1位 31个
	private long workerId;
	//机房ID 2进制5位  32位减掉1位 31个
	private long datacenterId;
	//代表一毫秒内生成的多个id的最新序号  12位 4096 -1 = 4095 个
	private long sequence;
	//设置一个时间初始值    2^41 - 1   差不多可以用69年
	private long twepoch = 1585644268888L;
	//5位的机器id
	private long workerIdBits = 5L;
	//5位的机房id
	private long datacenterIdBits = 5L;
	//每毫秒内产生的id数 2 的 12次方
	private long sequenceBits = 12L;
	// 这个是二进制运算，就是5 bit最多只能有31个数字，也就是说机器id最多只能是32以内
	private long maxWorkerId = -1L ^ (-1L << workerIdBits);
	// 这个是一个意思，就是5 bit最多只能有31个数字，机房id最多只能是32以内
	private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
 
	private long workerIdShift = sequenceBits;
	private long datacenterIdShift = sequenceBits + workerIdBits;
	private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	private long sequenceMask = -1L ^ (-1L << sequenceBits);
	//记录产生时间毫秒数，判断是否是同1毫秒
	private long lastTimestamp = -1L;
	public long getWorkerId(){
		return workerId;
	}
	public long getDatacenterId() {
		return datacenterId;
	}
	public long getTimestamp() {
		return System.currentTimeMillis();
	}
 
 
 
	public IdWorker(long workerId, long datacenterId, long sequence) {
 
		// 检查机房id和机器id是否超过31 不能小于0
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(
					String.format("worker Id can't be greater than %d or less than 0",maxWorkerId));
		}
 
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
 
			throw new IllegalArgumentException(
					String.format("datacenter Id can't be greater than %d or less than 0",maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
		this.sequence = sequence;
	}
 
	// 这个是核心方法，通过调用nextId()方法，让当前这台机器上的snowflake算法程序生成一个全局唯一的id
	public synchronized long nextId() {
		// 这儿就是获取当前时间戳，单位是毫秒
		long timestamp = timeGen();
		if (timestamp < lastTimestamp) {
 
			System.err.printf(
					"clock is moving backwards. Rejecting requests until %d.", lastTimestamp);
			throw new RuntimeException(
					String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
							lastTimestamp - timestamp));
		}
 
		// 下面是说假设在同一个毫秒内，又发送了一个请求生成一个id
		// 这个时候就得把seqence序号给递增1，最多就是4096
		if (lastTimestamp == timestamp) {
 
			// 这个意思是说一个毫秒内最多只能有4096个数字，无论你传递多少进来，
			//这个位运算保证始终就是在4096这个范围内，避免你自己传递个sequence超过了4096这个范围
			sequence = (sequence + 1) & sequenceMask;
			//当某一毫秒的时间，产生的id数 超过4095，系统会进入等待，直到下一毫秒，系统继续产生ID
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
 
		} else {
			sequence = 0;
		}
		// 这儿记录一下最近一次生成id的时间戳，单位是毫秒
		lastTimestamp = timestamp;
		// 这儿就是最核心的二进制位运算操作，生成一个64bit的id
		// 先将当前时间戳左移，放到41 bit那儿；将机房id左移放到5 bit那儿；将机器id左移放到5 bit那儿；将序号放最后12 bit
		// 最后拼接起来成一个64 bit的二进制数字，转换成10进制就是个long型
		return ((timestamp - twepoch) << timestampLeftShift) |
				(datacenterId << datacenterIdShift) |
				(workerId << workerIdShift) | sequence;
	}
 
	/**
	 * 当某一毫秒的时间，产生的id数 超过4095，系统会进入等待，直到下一毫秒，系统继续产生ID
	 * @param lastTimestamp
	 * @return
	 */
	private long tilNextMillis(long lastTimestamp) {
 
		long timestamp = timeGen();
 
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}
	//获取当前时间戳
	private long timeGen(){
		return System.currentTimeMillis();
	}
 
	/**
	 *  main 测试类
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(1&4596);
		System.out.println(2&4596);
		System.out.println(6&4596);
		System.out.println(6&4596);
		System.out.println(6&4596);
		System.out.println(6&4596);
//		IdWorker worker = new IdWorker(1,1,1);
//		for (int i = 0; i < 22; i++) {
//			System.out.println(worker.nextId());
//		}
	}
}
~~~

这种方式的优缺点是：

优点：

- 毫秒数在高位，自增序列在低位，整个ID都是趋势递增的。
- 不依赖数据库等第三方系统，以服务的方式部署，稳定性更高，生成ID的性能也是非常高的。
- 可以根据自身业务特性分配bit位，非常灵活。

缺点：

- 强依赖机器时钟，如果机器上时钟回拨，会导致发号重复或者服务会处于不可用状态。

#### 应用举例Mongdb objectID

[MongoDB官方文档 ObjectID](https://docs.mongodb.com/manual/reference/method/ObjectId/#description)可以算作是和snowflake类似方法，通过“时间+机器码+pid+inc”共12个字节，通过4+3+2+3的方式最终标识成一个24长度的十六进制字符。

### 数据库生成

以MySQL举例，利用给字段设置`auto_increment_increment`和`auto_increment_offset`来保证ID自增，每次业务使用下列SQL读写MySQL得到ID号。

~~~sql
begin;REPLACE INTO Tickets64 (stub) VALUES ('a');SELECT LAST_INSERT_ID();commit;
~~~

![image](https://pic.imgdb.cn/item/621ba2d72ab3f51d916402e3.png)

这种方案的优缺点如下：

优点：

- 非常简单，利用现有数据库系统的功能实现，成本小，有DBA专业维护。
- ID号单调自增，可以实现一些对ID有特殊要求的业务。

缺点：

- 强依赖DB，当DB异常时整个系统不可用，属于致命问题。配置主从复制可以尽可能的增加可用性，但是数据一致性在特殊情况下难以保证。主从切换时的不一致可能会导致重复发号。
- ID发号性能瓶颈限制在单台MySQL的读写性能。

对于MySQL性能问题，可用如下方案解决：在分布式系统中我们可以多部署几台机器，每台机器设置不同的初始值，且步长和机器数相等。比如有两台机器。设置步长step为2，TicketServer1的初始值为1（1，3，5，7，9，11...）、TicketServer2的初始值为2（2，4，6，8，10...）。这是Flickr团队在2010年撰文介绍的一种主键生成策略（[Ticket Servers: Distributed Unique Primary Keys on the Cheap ](http://code.flickr.net/2010/02/08/ticket-servers-distributed-unique-primary-keys-on-the-cheap/)）。如下所示，为了实现上述方案分别设置两台机器对应的参数，TicketServer1从1开始发号，TicketServer2从2开始发号，两台机器每次发号之后都递增2。

~~~sql
TicketServer1:
auto-increment-increment = 2
auto-increment-offset = 1

TicketServer2:
auto-increment-increment = 2
auto-increment-offset = 2
~~~

假设我们要部署N台机器，步长需设置为N，每台的初始值依次为0,1,2...N-1那么整个架构就变成了如下图所示：

![image](https://pic.imgdb.cn/item/621ba3322ab3f51d9164c4d9.png)

这种架构貌似能够满足性能的需求，但有以下几个缺点：

- 系统水平扩展比较困难，比如定义好了步长和机器台数之后，如果要添加机器该怎么做？假设现在只有一台机器发号是1,2,3,4,5（步长是1），这个时候需要扩容机器一台。可以这样做：把第二台机器的初始值设置得比第一台超过很多，比如14（假设在扩容时间之内第一台不可能发到14），同时设置步长为2，那么这台机器下发的号码都是14以后的偶数。然后摘掉第一台，把ID值保留为奇数，比如7，然后修改第一台的步长为2。让它符合我们定义的号段标准，对于这个例子来说就是让第一台以后只能产生奇数。扩容方案看起来复杂吗？貌似还好，现在想象一下如果我们线上有100台机器，这个时候要扩容该怎么做？简直是噩梦。所以系统水平扩展方案复杂难以实现。
- ID没有了单调递增的特性，只能趋势递增，这个缺点对于一般业务需求不是很重要，可以容忍。
- 数据库压力还是很大，每次获取ID都得读写一次数据库，只能靠堆机器来提高性能。

### Leaf方案实现

综合对比上述几种方案，每种方案都不完全符合我们的要求。所以Leaf分别在上述第二种和第三种方案上做了相应的优化，实现了Leaf-segment和Leaf-snowflake方案。

