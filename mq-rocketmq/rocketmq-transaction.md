# rocketmq - 分布式事务
使用消息中间件来实现，解决高并发时的消息处理效率问题。

**注意：开源版本3.2.6把broker对"未确认prepared消息的回查机制"砍掉/闭源了!**

## rocketmq分布式事务底层实现原理
### 第一部分：producer <-> MQ
1、producer发送prepared预处理消息到broker上，此时消息对consumer端不可见；

2、broker收到消息后，回调producer的callback方法，producer在callback方法中执行本地事务；

本地事务执行完成后，向broker返回确认消息：

	a、producer本地事务执行成功，则返回MESSAGE_COMMIT
	b、producer本地事务执行失败，则返回MESSAGE_ROLLBACK

如果producer发送的确认消息发送失败（网络原因或producer宕机）：
则由“未决事务，服务器回查客户端”机制进行处理：

	在broker上通过定时job检查那些未收到确认的prepared消息，
	然后，broker向producer确认这些未决消息的本地事务执行结果是成功还是失败。

这样保证了producer的本地事务和broker上的prepared消息处理逻辑的一致/正确性。
	
producer端本地事务的执行结果与broker上的事务消息在整体上保持逻辑一致：

	A事务在producer上执行成功，则消息必须对consumer端可见；
	A事务在producer上执行失败，则消息必须对consumer端不可见；

只要能够保证producer的事务执行和broker上的事务消息的一致，就已经完成分布式事务的第一步，也是最重要的一步。

接下来，就是由broker将事务消息发送给相关的consumer来消费，

### 第二部分：MQ <-> consumer
在consumer端的消费就是分布式事务的第二部分。

	如果consumer处理事务成功，则整个分布式事务正确执行并结束。
	如果consumer处理事务失败，则通过“人工介入”或者其它补偿方式来处理。

从MQ到consumer，可能发生以下几种情况：

	consumer发生异常：broker会重发消息给consumer
	consumer宕机：broker会重发消息给同组的consumer
	consumer处理失败/无效消息导致处理失败：consumer通知producer消息有问题

### 总结
	分布式事务第一部分，由producer和MQ结合起来保障本地事务与事务消息的一致性
	分布式事务第二部分，由MQ的消息重试机制来保证消息被consumer消费到。
	分布式事务的第一部分执行不需要考虑第二部分事务的结果。

A事务不关心B事务的结果，只需要保证prepared消息在broker上被正确处理

由MQ来保证将事务消息推送给相关的consumer消费。