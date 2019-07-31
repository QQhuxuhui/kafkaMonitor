import request from "../request";

const getZooKeeperServerListUrl = "/zookeeper/getZooKeeperServerList";
const saveZookeeperServerUrl = "/zookeeper/save";
const testConnUrl = "/zookeeper/testConn";
//获取分区数据详情
const getTopicPartitionDetailUrl = "/kafka/operate/getTopicPartitionDetail";

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

