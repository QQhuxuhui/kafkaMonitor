# kafkaMonitor
============
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

> monitor for kafka

#### 介绍
Kafka可视化监控与告警系统，项目基于开源软件[kafkaOffsetMonitor](https://github.com/Morningstar/kafka-offset-monitor)做的二次开发。

前端项目参考了优秀开源项目[vue-manager-system](https://github.com/lin-xin/vue-manage-system)。

#### 项目背景
- 在实时数据处理中，经常遇到数据延迟问题，对kafka数据堆积量的监控能很及时地知道哪个节点数据处理能力不足，及时发现并解决线上问题。
- 当前版本的项目依赖开源软件[kafkaOffsetMonitor](https://github.com/Morningstar/kafka-offset-monitor)，原因：
    - kafka不同版本的获取监控数据信息的方式有所不同。基于[kafkaOffsetMonitor](https://github.com/Morningstar/kafka-offset-monitor)可以避免不同版本的兼容性问题。
    - kafkaOffsetMonitor易于安装，受用人群比较普遍，[kafkaOffsetMonitor](https://github.com/Morningstar/kafka-offset-monitor)0.4版本增加了对kafka0.9+版本的数据读取支持。但是没有数据报警，数据堆积问题不能及时发现，笔者直接就在此基础上进行了二次开发。
- 项目支持监控多个[kafkaOffsetMonitor](https://github.com/Morningstar/kafka-offset-monitor)
#### 系统部分截图
- 实时面板
![](https://i.loli.net/2019/08/02/5d43f0779595145922.jpg)
- Topic管理
![](https://i.loli.net/2019/08/02/5d43efa7de7be28935.jpg)
- kafkaOffsetMonitor管理
![](https://i.loli.net/2019/08/02/5d43efa7cc83d52167.jpg)

#### 安装使用

1. [使用说明](https://github.com/quantifind/KafkaOffsetMonitor)
2. [后端部署](https://github.com/quantifind/KafkaOffsetMonitor)
3. [前端部署](https://github.com/quantifind/KafkaOffsetMonitor)