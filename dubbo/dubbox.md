## Dubbox - Dubbo Extension

**dubbox新增特性**

	1、 支持REST风格远程调用（HTTP + JSON/XML) - 基于非常成熟的JBoss RestEasy框架

	2、 支持基于Kryo和FST的Java高效序列化实现（为Dubbo的RPC协议添加新的序列化实现）

	3、 支持基于嵌入式Tomcat的HTTP remoting体系
	
	4、 支持完全基于Java代码的Dubbo配置, 基于Spring的Java Config
	   --->实现纯Java代码方式来配置dubbo

	5、 支持基于Jackson的JSON序列化（为dubbo增加新的JSON序列化实现）
	
	6、 第三方依赖框架的升级: Spring, ZooKeeper客户端


**dubbox使用最佳实践**

	1、服务提供方：使用注解配置服务；
	2、服务消费方：使用xml配置引用的服务；

