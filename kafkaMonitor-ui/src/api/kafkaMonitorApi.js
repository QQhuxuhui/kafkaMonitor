import request from '../request.js'

export function get(api, params) {
  return request.get(api, params);
}

export function post(api, params) {
  return request.post(api, params);
}


//获取所有的kafkaManager
const getKafkaMonitorManagerListUrl = "/kafka/getKafkaMonitorManagerList";
//更新kafkaManager
const saveKafkaMonitorManagerUrl = "/kafka/saveKafkaMonitorManager";
//getTopicList
const getTopicListUrl = "/api/kafka/getTopicList";
//获取topic详细
const getTopicDetailsUrl = "/api/kafka/getTopicDetails";
//获取group列表
const getGroupListUrl = "/api/kafka/getGroupList";
const getGroupOffsetAndLagListUrl = "/api/kafka/getGroupOffsetAndLagList";
//获取监控和偏移量信息
const getGroupAndKafkaMonitorUrl = "/api/kafka/getGroupAndKafkaMonitor";
//添加监控
const saveKafkaMonitorUrl = "/api/kafka/saveKafkaMonitor";
const getKafkaMonitorListUrl = "/api/kafka/getKafkaMonitorList";
const getKafkaMonitorByIdUrl = "/api/kafka/getKafkaMonitorById";
const getKafkaMonitorEmailsUrl = "/api/kafka/getKafkaMonitorEmails";
//添加邮箱地址
const saveEmailAddressUrl = "/api/email/saveEmailAddress";
//获取所有邮箱地址
const getEmailAddressListUrl = "/api/email/getEmailAddressList";


export function getKafkaMonitorManagerList() {
  return request.get(getKafkaMonitorManagerListUrl);
}

export function getKafkaMonitorEmails(id){
  return request.get(getKafkaMonitorEmailsUrl,{id: id});
}

export function saveKafkaMonitorManager(id, address, name, isDeleted, tag) {
  return request.post(saveKafkaMonitorManagerUrl, {
    "id": id,
    "address": address,
    "name": name,
    "deleted": isDeleted,
    "tag": tag
  });
  // return request.get(saveKafkaMonitorManagerUrl, {address: address, name: name, isDeleted: isDeleted});
}

export function getGroupList() {
  return request.get(getGroupListUrl);
}

export function getGroupOffsetAndLagList() {
  return request.get(getGroupOffsetAndLagListUrl);
}

/**
 * 查询topic的偏移量和监控信息
 * @param address
 * @param topic
 * @param group
 * @returns {*}
 */
export function getGroupAndKafkaMonitor(address, topic, group) {
  return request.get(getGroupAndKafkaMonitorUrl, {address: address, topic: topic, group: group});
}


export function getTopicList(address) {
  return request.get(getTopicListUrl, {address: address});
}

export function getTopicDetails(address, topic) {
  return request.get(getTopicDetailsUrl, {address: address, topic: topic});
}

/**
 * 添加监控
 * @param address 服务地址
 * @param topic topic
 * @param group group
 * @param lag 阈值
 * @param emails 邮件列表
 * @param isEmail 是否开启通知
 * @param isDeleted 是否删除记录
 * @param desc 备注
 * @returns {*}
 */
export function saveKafkaMonitor(id, address, topic, group, lag, emails, isEmail, isDeleted, desc) {
  return request.post(saveKafkaMonitorUrl, {
    "emails": emails,
    "kafkaOffsetMonitor": {
      "id": id,
      "address": address,
      "topic": topic,
      "groupId": group,
      "lag": lag,
      "emailAlter": isEmail,
      "deleted": isDeleted,
      "description": desc,
    }
  });
}

export function getKafkaMonitorList() {
  return request.get(getKafkaMonitorListUrl);
}

export function saveEmailAddress(email, name) {
  return request.get(saveEmailAddressUrl, {email: email, name: name});
}

export function getKafkaMonitorById(id){
  return request.get(getKafkaMonitorByIdUrl, {id: id});
}

export function getEmailAddressList() {
  return request.get(getEmailAddressListUrl);
}
