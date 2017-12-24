# RocketMQ
##### RocketMQ是一款分布式、队列模型的消息中间件，具有以下特点：
	* 强调集群无单点问题，原生就是集群模式
		1、双主模型	 			2master - 大部分项目使用该模式已足够满足需求
		2、多主多从模型 + 异步复制	2master-2slave-async 实时性要求高的场景下使用
		3、多主多从模型 + 同步复制	2master-2slave-sync	最严苛的场景下使用
		主从复制的两种持久化机制：
			异步复制 - 性能好
			同步双写 - 适合对消息可靠性要求极高的场合，比如设计到money的应用

	* 亿级消息堆积能力（缓解消费端压力）
		消息存储是怎么设计的？CommitLog ConsumerQueue Index
		支持上万个队列的高并发读写，并且保证性能稳定

	* 消息都是持久化存储，且无容量限制
		实际底层是通过定期清除历史数据来解决存储容量的问题

	* 完善的消息重试机制，确保消息在流转过程中不丢失
		消息确认机制

	* 丰富的API提供各种场景的解决方案
		基于电商的各种场景需求而定制的产品

	* 提供两种消费端消费模型：
		push -> 智能型broker+哑consumer
			broker负责推送消息，consumer只管消费
		pull -> 哑broker+智能型consumer
			consumer主动从broker拉取消息，并维护已消费消息的offset
			类似kafka的消息消费方式，由消费端通过拉取方式完成消息的消费

	* 集群消费-消费端水平扩展
		多个consumer同属于一个Group
		加入新的consumer到Group即可分摊压力

	* 支持消息的有序消费（保证严格的消息顺序）
		producer-单线程顺序发送，且发送到同一个队列
		consumer-绑定到某一个线程上对相同key进行消费
		严格顺序消息依赖broker的同步双写机制来提供保障（依赖同步双写，主备自动切换）
		但自动切换功能未实现/未开放源码
		因此，对于开源版本来讲，当broker宕机时，无法确保消息的严格顺序的。
		Broker重启，队列总数发生发化，哈希取模后定位的队列会变化，因此产生短暂的消息顺序不一致。

	* 分布式事务的支持
		开源版本不支持
	

##

## RocketMQ 环境搭建


## RocketMQ 实战 - Broker


## RocketMQ 实战 - Producer


## RocketMQ 实战 - Consumer


## RocketMQ 架构设计


## RocketMQ 管理员操作集群