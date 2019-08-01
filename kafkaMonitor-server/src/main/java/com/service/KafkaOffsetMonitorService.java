package com.service;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.bean.kafka.*;

import java.util.List;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/1 17:18
 * @Description:
 */
public interface KafkaOffsetMonitorService {

    /**
     * 获取所有topic
     *
     * @param address
     * @return
     * @throws Exception
     */
    TopicList getTopicList(String address) throws Exception;

    /**
     * 查看topic详细信息
     *
     * @param address
     * @param topic
     * @return
     * @throws Exception
     */
    TopicDetails getTopicDetails(String address, String topic) throws Exception;

    /**
     * 查看group的消费信息
     *
     * @param address
     * @param group
     * @return
     * @throws Exception
     */
    Group getGroup(String address, String group) throws Exception;

    /**
     * 获取所有在监控的group列表
     * @return
     */
    List<Group> getGroupList();

    /**
     * 添加监控
     * @param kafkaOffsetMonitorVo
     */
    void saveKafkaMonitor(KafkaOffsetMonitorVo kafkaOffsetMonitorVo);

    /**
     * 获取所有的监控列表
     * @return
     */
    List<KafkaOffsetMonitor> getKafkaMonitorList();

    /**
     * 添加监控服务
     * @param kafkaOffsetMonitorManager
     * @return
     */
    KafkaOffsetMonitorManager saveKafkaMonitorManager(KafkaOffsetMonitorManager kafkaOffsetMonitorManager);

    /**
     * 获取所有的kafkaManager
     *
     * @return
     */
    List<KafkaOffsetMonitorManager> getKafkaMonitorManagerList();

    /**
     * 获取监控的数据偏移量信息
     * @return
     */
    List<GroupOffsetAndLagBean> getGroupOffsetAndLagList();

    /**
     * 获取指定消费组的偏移量和阈值
     * @param address
     * @param topic
     * @param group
     * @return
     */
    Object getGroupAndKafkaMonitor(String address, String topic, String group) throws UnirestException;

    /**
     * 通过监控数据的ID获取监控的邮箱列表
     * @param id
     * @return
     */
    Object getKafkaMonitorEmails(String id);

    /**
     * 通过id获取kafkamonitor信息
     * @param id
     * @return
     */
    KafkaOffsetMonitor getKafkaMonitorById(String id);
}
