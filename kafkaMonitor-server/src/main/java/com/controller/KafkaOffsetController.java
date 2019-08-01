package com.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.bean.ResponseBean;
import com.bean.kafka.KafkaOffsetMonitorManager;
import com.bean.kafka.KafkaOffsetMonitorVo;
import com.bean.user.UserInfo;
import com.service.KafkaOffsetMonitorService;
import com.service.TokenService;
import com.service.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/1 19:44
 * @Description:
 */
@RequestMapping("/api/kafka")
@RestController
public class KafkaOffsetController {

    @Autowired
    private KafkaOffsetMonitorService kafkaOffsetMonitorService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @RequestMapping("/getTopicList")
    public Object getTopicList(String address) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(kafkaOffsetMonitorService.getTopicList(address));
        return responseBean;
    }

    @RequestMapping("/getGroup")
    public Object getGroup(String address, String group) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(kafkaOffsetMonitorService.getGroup(address, group));
        return responseBean;
    }

    /**
     * 获取所有的在监控的group列表
     *
     * @return
     */
    @RequestMapping("/getGroupList")
    public Object getGroupList() {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(kafkaOffsetMonitorService.getGroupList());
        return responseBean;
    }

    /**
     * 获取监控组的偏移量数据和阈值数据
     *
     * @return
     */
    @RequestMapping("/getGroupOffsetAndLagList")
    public Object getGroupOffsetAndLagList() {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(kafkaOffsetMonitorService.getGroupOffsetAndLagList());
        return responseBean;
    }

    /**
     * 获取监控和偏移量信息
     * @param address
     * @param topic
     * @param group
     * @return
     * @throws UnirestException
     */
    @RequestMapping("/getGroupAndKafkaMonitor")
    public Object getGroupAndKafkaMonitor(String address, String topic, String group) throws UnirestException {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(kafkaOffsetMonitorService.getGroupAndKafkaMonitor(address, topic, group));
        return responseBean;

    }

    @RequestMapping("/getTopicDetails")
    public Object getTopicDetails(String address, String topic) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(kafkaOffsetMonitorService.getTopicDetails(address, topic));
        return responseBean;
    }

    @RequestMapping("/saveKafkaMonitorManager")
    public ResponseBean saveKafkaMonitorManager(@RequestBody KafkaOffsetMonitorManager kafkaOffsetMonitorManager) {
        ResponseBean responseBean = new ResponseBean();
        KafkaOffsetMonitorManager kafkaOffsetMonitorManagerReturn = kafkaOffsetMonitorService.saveKafkaMonitorManager(kafkaOffsetMonitorManager);
        responseBean.setData(kafkaOffsetMonitorManagerReturn);
        if (kafkaOffsetMonitorManagerReturn == null) {
            //添加失败
            responseBean.setCode(201);
        }
        return responseBean;
    }


    @RequestMapping("/getKafkaMonitorManagerList")
    public ResponseBean getKafkaMonitorManagerList() {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(kafkaOffsetMonitorService.getKafkaMonitorManagerList());
        return responseBean;
    }

    @RequestMapping(value = "/saveKafkaMonitor", method = {RequestMethod.POST,RequestMethod.GET})
    public ResponseBean saveKafkaMonitor(@RequestBody KafkaOffsetMonitorVo kafkaOffsetMonitorVo,HttpServletRequest httpServletRequest) {
        ResponseBean responseBean = new ResponseBean();
        UserInfo userInfo = userService.getUserInfoByUserName(getUserName(httpServletRequest));
        kafkaOffsetMonitorVo.getKafkaOffsetMonitor().setCreateBy(userInfo.getName());
        kafkaOffsetMonitorService.saveKafkaMonitor(kafkaOffsetMonitorVo);
        return responseBean;
    }

    @RequestMapping("/getKafkaMonitorList")
    public ResponseBean getKafkaMonitorList() {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(kafkaOffsetMonitorService.getKafkaMonitorList());
        return responseBean;
    }

    @RequestMapping("/getKafkaMonitorById")
    public ResponseBean getKafkaMonitorById(String id) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(kafkaOffsetMonitorService.getKafkaMonitorById(id));
        return responseBean;
    }

    /**
     * 通过监控数据的ID获取监控提醒的邮箱列表
     * @return
     */
    @RequestMapping("/getKafkaMonitorEmails")
    public ResponseBean getKafkaMonitorEmails(String id) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(kafkaOffsetMonitorService.getKafkaMonitorEmails(id));
        return responseBean;
    }


    public String getUserName(HttpServletRequest request) {
        return tokenService.getUserName(request.getHeader("authorization"));
    }

}
