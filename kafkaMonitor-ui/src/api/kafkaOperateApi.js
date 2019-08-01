import request from "../request";

const getTopicListUrl = "/api/kafka/operate/getTopicList";
const createTopicUrl = "/api/kafka/operate/createTopic";
const deleteTopicUrl = "/api/kafka/operate/deleteTopic";
const topicExistsUrl = "/api/kafka/operate/topicExists";
//获取分区数据详情
const getTopicPartitionDetailUrl = "/api/kafka/operate/getTopicPartitionDetail";

export function getTopicList(zookeeper) {
  return request.get(getTopicListUrl, {zookeeper: zookeeper});
}

export function createTopic(zookeeper, topic, partitions, replication) {
  return request.get(createTopicUrl, {
    zookeeper: zookeeper,
    topic: topic,
    partitions: partitions,
    replication: replication
  });
}

export function deleteTopic(zookeeper, topic) {
  return request.get(deleteTopicUrl, {zookeeper: zookeeper, topic: topic});
}

export function topicExists(zookeeper, topic) {
  return request.get(topicExistsUrl, {zookeeper: zookeeper, topic: topic});
}

export function getTopicPartitionDetail(zookeeper, topic) {
  return request.get(getTopicPartitionDetailUrl, {zookeeper: zookeeper, topic: topic});
}
