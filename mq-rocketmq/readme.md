# RocketMQ
##### RocketMQ是一款分布式、队列模型的消息中间件，具有以下特点：
	* >>> 强调集群无单点问题，原生就是集群模式
		1、双主模型 2master 
			- 大部分项目使用该模式已足够满足需求
			- Master宕机后，其上的未被消费的消息不能被consumer端消费，缺失消息的实时消费
		
		2、多主多从模型 + 异步复制	2master-2slave-async 
			-实时性要求高的场景下使用
			-Master宕机后，由Slave节点推送消息到consumer，实现消息的实时消费
			-在Master恢复前，Slave仅提供消息给consumer消费，Slave只读不可写
			-在Master恢复后，会同步slave上的消息消费状态，保持主从消息状态一致
			-主从间消息复制使用异步方式，性能好，但Master宕机可能导致少量数据丢失
		
		3、多主多从模型 + 同步双写	2master-2slave-sync
			- 最严苛的场景下使用
			- 主从间消息复制使用同步方式，最安全，可确保主从间消息是完全一致的
		
		主从复制的两种持久化机制：
			异步复制 - 性能好（消息从Master以异步方式复制到Slave）
			同步双写 - 适合对消息可靠性要求极高的场合，比如设计到money的应用

	* >>> 完善的消息重试机制，确保消息在流转过程中不丢失
		消息确认机制
		消息方向：producer -> broker
			producer端的消息重试 - 确保消息发送到broker 
			producer.setRetryTimesWhenSendFailed(10);
		
		broker对所有消息进行持久化 - 确保消息在broker上不丢失

		消息方向：broker -> consumer
			consumer端的消息重试 - 确保消息从broker推送到消费端
			网络异常导致broker推送消息到consumer失败的重试-消费者宕机
			消费端程序异常导致的重试
		说明：
			先启动consumer订阅，再启动producer的情况下,
			consumer只要不宕机，broker对其上的消息处理没有时间要求，
			broker不会因为consumer长时间未返回消息确认而将消息发送到其它consumer。

	* >>> 支持消息的有序消费（保证严格的消息顺序）
		producer-单线程顺序发送，且发送到同一个队列
		consumer-绑定到某一个线程上对相同key进行消费
		顺序消息：
		1、正常情况下，双Master模式在Master不宕机的情况下可以保证消息的顺序消费；
		2、如果Master宕机，则无法提供消息顺序消费的保障。
			Broker重启，队列总数发生发化，哈希取模后定位的队列会变化，因此产生短暂的消息顺序不一致。
		3、如果要求某个Master宕机时也需要保障消息的顺序消费：
			3个条件: a.部署集群为多主多从模式; b.同步双写机制；c.能自动完成主备切换
			只有满足上面3个条件，才能保证在高可用的情况下也支持严格的消息顺序消费。
			注意：主备自动切换的功能未开源。
			因此，对于开源版本来讲，当broker宕机时，无法保证严格的顺序消息。	

	* >>> 分布式事务的支持
		开源版本不支持

	* > 提供两种消费端消费模型：
		consumer集群消费-消费端水平扩展
			同组的consumer分摊所订阅topic上的消息，每条消息仅被1个consumer消费
		consumer广播消费-不同系统对同一个消息的处理方式不同
			同一条消息会推送给同组的每一个consumer

	* > 提供两种消费端消费拉取方式：
		push -> 智能型broker+哑consumer
			broker负责推送消息，consumer只管消费
			Consumer使用：DefaultMQPushConsumer
		pull -> 哑broker+智能型consumer
			consumer主动从broker拉取消息，并维护已消费消息的offset
			Consumer使用：DefaultMQPullConsumer
			类似kafka的消息消费方式，由消费端通过拉取方式完成消息的消费？

	* > 亿级消息堆积能力（缓解消费端压力）
		消息存储是怎么设计的？CommitLog ConsumerQueue Index
		支持上万个队列的高并发读写，并且保证性能稳定

	* > 消息都是持久化存储，且无容量限制
		实际底层是通过定期清除历史数据来解决存储容量的问题
		broker上存储消息的三个相关文件:
			commitlog 原始消息数据
			consumerqueue	记录消息偏移量，便于快速定位消息在文件中的位置
			index	索引文件

	* > 丰富的API提供各种场景的解决方案
		基于电商的各种场景需求而定制的产品
	
	* > RocketMQ无法避免消息重复	
		消息可能会重复推送给consumer，如果业务对重复消息敏感，则需要在业务层面进行去重。
		需要消费端/业务端进行业务逻辑上的去重处理，保证消息的幂等性！！！
		引起消息重复的因素：
			网络不可达/不稳定等因素，需要依靠消息重发机制来确保消息不丢失。
		幂等性+去重的建议：
			1、为消息设置唯一键，比如使用消息内容中具有唯一性的字段来去重，比如订单号。
			2、去重表，设置字段的唯一性约束来防止重复数据。
		

##

## RocketMQ 环境搭建


## RocketMQ 实战 - Broker


## RocketMQ 实战 - Producer


## RocketMQ 实战 - Consumer


## RocketMQ 架构设计


## RocketMQ 管理员操作集群