# kafkaMonitor

#### 介绍
Kafka可视化监控与告警系统，项目基于开源软件[kafkaOffsetMonitor](https://github.com/quantifind/KafkaOffsetMonitor)做的二次开发。

#### 项目背景
- 在实时数据处理中，经常遇到数据延迟问题，对kafka数据堆积量的监控能很及时地知道哪个节点数据处理能力不足，及时发现并解决线上问题。
- 当前版本的项目依赖开源软件[kafkaOffsetMonitor](https://github.com/quantifind/KafkaOffsetMonitor)，原因：
    - 笔者在开发项目时生产环境的kafka版本还比较低，没有很好的java api可以进行数据监控
    - kafkaOffsetMonitor易于安装，受用人群比较普遍，但是没有数据报警，生产环境的数据堆积问题不能及时发现，笔者直接就在此基础上进行了二次开发。
- 项目提供了较完备的说明文档，希望也能对大家的工作有所帮助\^_^。
- 项目支持监控多个[kafkaOffsetMonitor](https://github.com/quantifind/KafkaOffsetMonitor)

#### 系统部分截图
- 实时面板
![](https://i.loli.net/2019/08/02/5d43f0779595145922.jpg)
- Topic管理
![](https://i.loli.net/2019/08/02/5d43efa7de7be28935.jpg)
- kafkaOffsetMonitor管理
![](https://i.loli.net/2019/08/02/5d43efa7cc83d52167.jpg)

#### 安装使用

1. xxxx
2. xxxx
3. xxxx