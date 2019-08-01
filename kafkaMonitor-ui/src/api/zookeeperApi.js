import request from "../request";

const getZooKeeperServerListUrl = "/api/zookeeper/getZooKeeperServerList";
const saveZookeeperServerUrl = "/api/zookeeper/save";
const testConnUrl = "/api/zookeeper/testConn";
//获取分区数据详情
const getTopicPartitionDetailUrl = "/api/kafka/operate/getTopicPartitionDetail";

export function getZooKeeperServerList() {
  return request.get(getZooKeeperServerListUrl);
}

export function saveZookeeperServer(address, name, deleted) {
  return request.post(saveZookeeperServerUrl, {
    "address": address,
    "name": name,
    "deleted": deleted
  });
}

export function testConn(address) {
  return request.get(testConnUrl, {address: address});
}

