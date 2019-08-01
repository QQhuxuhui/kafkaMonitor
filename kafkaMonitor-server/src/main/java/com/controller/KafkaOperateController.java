package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bean.ResponseBean;
import com.bean.user.UserInfo;
import com.common.constants.LoginStatusEnum;
import com.service.KafkaOperateService;
import com.service.TokenService;
import com.service.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/30 14:08
 * @Description:
 */
@RequestMapping("/api/kafka/operate")
@RestController
public class KafkaOperateController {

    @Autowired
    private KafkaOperateService kafkaOperateService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping("/createTopic")
    public Object createTopic(String zookeeper, String topic, Integer partitions, Integer replication, HttpServletRequest request) {
        ResponseBean responseBean = new ResponseBean();
        if (partitions == null) {
            partitions = 1;
        }
        if (replication == null) {
            replication = 1;
        }
        if (!kafkaOperateService.createTopic(zookeeper, topic, partitions, replication, getUserName(request))) {
            responseBean.setCode(201);
            responseBean.setMsg("create topic failed");
        }
        return responseBean;
    }

    /**
     * 只允许管理员删除topic
     *
     * @param zookeeper
     * @param topic
     * @return
     */
    @RequestMapping("/deleteTopic")
    public Object deleteTopic(String zookeeper, String topic, HttpServletRequest request) {
        ResponseBean responseBean = new ResponseBean();
        UserInfo userInfo = userService.getUserInfoByUserName(getUserName(request));
        if (userInfo.getUsername().equals("admin")) {
            if (!kafkaOperateService.deleteTopic(zookeeper, topic, userInfo.getName())) {
                responseBean.setCode(201);
                responseBean.setMsg("delete topic failed");
            }
        } else {
            responseBean.setCode(LoginStatusEnum.USER_NO_RIGHT_STATUS.getCode());
            responseBean.setMsg(LoginStatusEnum.USER_NO_RIGHT_STATUS.getMessage());
        }
        return responseBean;
    }

    @RequestMapping("/getTopicList")
    public Object getTopicList(String zookeeper, HttpServletRequest request) {
        ResponseBean responseBean = new ResponseBean();
        UserInfo userInfo = userService.getUserInfoByUserName(getUserName(request));
        responseBean.setData(kafkaOperateService.getTopicList(zookeeper, userInfo));
        return responseBean;
    }

    //topicExists
    @RequestMapping("/topicExists")
    public Object topicExists(String zookeeper, String topic) {
        ResponseBean responseBean = new ResponseBean();
        if (!kafkaOperateService.topicExists(zookeeper, topic)) {
            responseBean.setCode(201);
            responseBean.setMsg(topic + " 不存在");
        }
        return responseBean;
    }

    @RequestMapping("/getTopicPartitionDetail")
    public Object getTopicPartitionDetail(String zookeeper, String topic) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(kafkaOperateService.getTopicPartitionDetail(zookeeper, topic));
        return responseBean;
    }


    public String getUserName(HttpServletRequest request) {
        return tokenService.getUserName(request.getHeader("authorization"));
    }
}
