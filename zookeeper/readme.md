# Zookeeper 集群搭建

#

## 集群规划
	操作系统：Centos7
	Java版本：JDK_1.8.0_144
	zookeeper版本：3.3.6
		3个节点：
		node1 192.168.1.201
		node2 192.168.1.202
		node3 192.168.1.203

## 准备工作（3个节点）
	- 关闭防火墙
		systemctl disable firewalld
	- 安装JDK
		/usr/local/java
	- 配置HOSTNAME
		vi /etc/hosts
			192.168.1.201           node1.clonegod.com      node1
			192.168.1.202           node2.clonegod.com      node2
			192.168.1.203           node3.clonegod.com      node3
		vi /etc/sysconfig/network
			HOSTNAME=node1.clonegod.com
	- 创建用户（可选）
		groupdadd hadoop
		useradd -g hadoop hadoop
		passwd hadoop
	- ssh免密（可选）
		ssh-keygen -t rsa -f ~/.ssh/id_rsa
		ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop@192.168.1.201
		ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop@192.168.1.202
		ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop@192.168.1.203

 
	
## 安装Zookeeper
#### 下载
	cd /usr/local/software
	wget https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/stable/zookeeper-3.4.10.tar.gz
	
	
		