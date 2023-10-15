### RocketMq+Springboot 实现p2p 和广播订阅模式
1. 安装NameServer

 搜索/拉取镜像
```
 docker pull rocketmqinc/rocketmq
```
 创建一个数据目录
```
mkdir -p /docker/rocketmq/nameserver/logs /docker/rocketmq/nameserver/store
```
 运行
```
docker run -d --restart=always --name rmqnamesrv --privileged=true -p 9876:9876  -v /docker/rocketmq/nameserver/logs:/root/logs -v /docker/rocketmq/nameserver/store:/root/store -e "MAX_POSSIBLE_HEAP=100000000" rocketmqinc/rocketmq sh mqnamesrv
```
2. 安装broker

安装broker创建broker.conf配置文件，我的目录是/opt/docker/rocketmq/broker.conf，文件内容如下
```
brokerClusterName = DefaultCluster
brokerName = broker-a
brokerId = 0
deleteWhen = 04
fileReservedTime = 48
brokerRole = ASYNC_MASTER
flushDiskType = ASYNC_FLUSH
brokerIP1 = 主机的IP
```
启动broker
```
docker run -d --restart=always --name rmqbroker --link rmqnamesrv:namesrv -p 10911:10911 -p 10909:10909 --priv
```
3. 安装控制台


   拉取镜像
```
   docker pull pangliang/rocketmq-console-ng
```
控制台启动
```
docker run -d --restart=always --name rmqadmin -e "JAVA_OPTS=-Drocketmq.namesrv.addr=122.112.145.138:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false" -p 8080:8080 pangliang/rocketmq-console-ng
```
4. 下载文件
修改application.yaml文件
```
 name-server: 8.142.81.64:9876 为rocketmq主机IP
```
5. 运行服务

### 有添加异步服务 还写了一个基于springboot类似MQ的test的测试

