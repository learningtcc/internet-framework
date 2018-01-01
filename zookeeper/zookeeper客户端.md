## zookeeper客户端
>zookeeper 原生API
>
>zkClient 开源第三方框架
>
>curator框架 [kjʊˈreɪtə(r)] 更强大的第三方框架


## zookeeper 原生API
	1、创建节点
		节点分4种类型
			持久化节点 PERSISTENT
			持久化顺序节点 PERSISTENT_SEQUENTIAL
			临时性节点 EPHEMERAL
			临时性顺序节点 EPHEMERAL_SEQUENTIAL

	2、修改节点value
		this.zk.setData(path, data.getBytes(), -1) // -1 表示节点的任何版本号

	3、获取节点value
		zk.getData(path, needWatch, null)

	4、删除节点
		zk.delete(path, -1); // -1 表示节点的任何版本号

	5、watcher - 触发1次后就失效性，之后需要重新注册watcher
		5种WatchedEvent:
			None	初始连接成功的事件
			NodeCreated	 节点创建
			NodeDataChanged 简单数据发生修改
			NodeChildrenChanged 仅告知子节点发生变化，但具体何种事件不明确
			NodeDeleted	节点删除


## zkClient - 封装zookeeper原生API，提供更便捷的接口
创建zkClient对象实例的构造参数

	zkServers	zookeeper集群地址，节点间用","分隔
	sessionTimeout	会话超时时间，毫秒，默认30000ms
	connectiontimeout	连接超时时间，毫秒
	IZkConnection		接口实现类
	zkSerializer		自定义序列化实现

简化了原生zookeeper客户端的API

	zkclient简化了节点的创建，用不同的方法创建不同的节点类型
	zkclient提供递归创建目录的功能
	zkclient提供递归删除目录的功能
	zkclient写数据直接write字符串(原生需要传byte[])
	zkclient读取直接返回Object对象(原生返回byte[])
	zkclient简化watch操作，无需反复注册watcher的问题
		- 监听节点数据的变化
		- 监听子节点的变化
	

## Curator - 提供更强大的功能
zookeeper的特性就是在分布式场景下的高可用，但是原生的API实现分布式功能比较麻烦，那么可以采用第三方的客户端来解决, Curator就是一个推荐的框架。

curator框架，它是Apache的顶级项目，非常成熟。


