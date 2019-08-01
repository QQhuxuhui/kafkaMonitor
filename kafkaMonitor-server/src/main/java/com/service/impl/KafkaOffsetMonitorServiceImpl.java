package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bean.BeanFactory;
import com.common.utils.MD5Util;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bean.EmailAddress;
import com.bean.IdAndEmail;
import com.bean.kafka.*;
import com.dao.jpa.EmailAddresssRepository;
import com.dao.jpa.IdAndEmailRepository;
import com.dao.jpa.KafkaOffsetMonitorManagerRepository;
import com.dao.jpa.KafkaOffsetMonitorRepository;
import com.service.KafkaOffsetMonitorService;

import java.util.*;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/1 17:30
 * @Description: 数据监控服务
 */
@Service
@Transactional
public class KafkaOffsetMonitorServiceImpl implements KafkaOffsetMonitorService {

    static KafkaOffsetMonitorUrl kafkaOffsetMonitorUrl = BeanFactory.getBean(KafkaOffsetMonitorUrl.class);


    @Autowired
    private IdAndEmailRepository idAndEmailRepository;

    @Autowired
    private EmailAddresssRepository emailAddresssRepository;

    @Autowired
    private KafkaOffsetMonitorRepository kafkaOffsetMonitorRepository;

    @Autowired
    private KafkaOffsetMonitorManagerRepository kafkaOffsetMonitorManagerRepository;


    @Override
    public TopicList getTopicList(String address) throws UnirestException {
        TopicList topicList = new TopicList();
        String body = Unirest.get(address + kafkaOffsetMonitorUrl.getTopiclist()).asString().getBody();
        if (StringUtils.isNotEmpty(body)) {
            List<String> list = Arrays.asList(body.replaceAll("\\[|\\]", "").split(","));
            topicList.setTopicList(list);
        }
        return topicList;
    }

    @Override
    public TopicDetails getTopicDetails(String address, String topic) throws UnirestException {
        TopicDetails topicDetails = new TopicDetails();
        String body = Unirest.get(address + kafkaOffsetMonitorUrl.getTopicdetails() + topic).asString().getBody();
        if (StringUtils.isNotEmpty(body)) {
            topicDetails = JSONObject.parseObject(body, TopicDetails.class);
        }
        return topicDetails;
    }

    @Override
    public Group getGroup(String address, String groupId) throws UnirestException {
        Group group = new Group();
        String body = Unirest.get(address + kafkaOffsetMonitorUrl.getGroup() + groupId).asString().getBody();
        if (StringUtils.isNotEmpty(body)) {
            group = JSONObject.parseObject(body, Group.class);
        }
        return group;
    }

    @Override
    public List<Group> getGroupList() {
        List<KafkaOffsetMonitor> KafkaOffsetMonitorList = getKafkaMonitorList();
        List<Group> groupList = new ArrayList<>();
        for (KafkaOffsetMonitor kafkaOffsetMonitor : KafkaOffsetMonitorList) {
            try {
                Group group = getGroup(kafkaOffsetMonitor.getAddress(), kafkaOffsetMonitor.getGroupId());
                groupList.add(group);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
        return groupList;
    }

    @Override
    public void saveKafkaMonitor(KafkaOffsetMonitorVo kafkaOffsetMonitorVo) {
        KafkaOffsetMonitor kafkaOffsetMonitor = kafkaOffsetMonitorVo.getKafkaOffsetMonitor();
        //address、topic、groupId
        String id = MD5Util.GetMD5Code(kafkaOffsetMonitor.getAddress() + kafkaOffsetMonitor.getTopic() + kafkaOffsetMonitor.getGroupId());
        if (StringUtils.isEmpty(kafkaOffsetMonitor.getId())) {
            kafkaOffsetMonitor.setId(id);
        }
        if (kafkaOffsetMonitor.getEmailAlter() == null) {
            kafkaOffsetMonitor.setEmailAlter(false);
        }
        //如果记录存在，保留创建人
        KafkaOffsetMonitor kafkaOffsetMonitorDb = kafkaOffsetMonitorRepository.findById(id);
        if (kafkaOffsetMonitorDb != null) {
            kafkaOffsetMonitor.setCreateBy(kafkaOffsetMonitorDb.getCreateBy());
        }
        kafkaOffsetMonitor.setUpdateDate(new Date());
        kafkaOffsetMonitorRepository.save(kafkaOffsetMonitor);
        //更新邮件提醒列表
        idAndEmailRepository.deleteByIds(id);
        if (kafkaOffsetMonitorVo.getEmails() != null) {
            List<IdAndEmail> idAndEmails = new ArrayList<>();
            for (String email : kafkaOffsetMonitorVo.getEmails()) {
                IdAndEmail idAndEmail = new IdAndEmail();
                String id1 = MD5Util.GetMD5Code(id + email);
                idAndEmail.setId(id1);
                idAndEmail.setIds(id);
                idAndEmail.setEmail(email);
                idAndEmails.add(idAndEmail);
            }
            idAndEmailRepository.save(idAndEmails);
        }
    }

    @Override
    public List<KafkaOffsetMonitor> getKafkaMonitorList() {
        KafkaOffsetMonitor kafkaOffsetMonitor = new KafkaOffsetMonitor();
        kafkaOffsetMonitor.setDeleted(false);
        Example<KafkaOffsetMonitor> example = Example.of(kafkaOffsetMonitor);
        return kafkaOffsetMonitorRepository.findAll(example);
    }

    @Override
    public KafkaOffsetMonitorManager saveKafkaMonitorManager(KafkaOffsetMonitorManager kafkaOffsetMonitorManager) {
        if (StringUtils.isEmpty(kafkaOffsetMonitorManager.getId())) {
            kafkaOffsetMonitorManager.setId(MD5Util.GetMD5Code("kafka_offset_monitor_manager" + kafkaOffsetMonitorManager.getAddress()));
        }
        if (kafkaOffsetMonitorManager.getDeleted() == null) {
            kafkaOffsetMonitorManager.setDeleted(false);
        }
        kafkaOffsetMonitorManager.setUpdateDate(new Date());
        return kafkaOffsetMonitorManagerRepository.save(kafkaOffsetMonitorManager);
    }

    @Override
    public List<KafkaOffsetMonitorManager> getKafkaMonitorManagerList() {
        KafkaOffsetMonitorManager kafkaOffsetMonitorManager = new KafkaOffsetMonitorManager();
        kafkaOffsetMonitorManager.setDeleted(false);
        Example<KafkaOffsetMonitorManager> example = Example.of(kafkaOffsetMonitorManager);
//        return kafkaOffsetMonitorManagerRepository.findAll();
        return kafkaOffsetMonitorManagerRepository.findAll(example);
    }

    @Override
    public List<GroupOffsetAndLagBean> getGroupOffsetAndLagList() {
        List<KafkaOffsetMonitor> KafkaOffsetMonitorList = getKafkaMonitorList();
        List<GroupOffsetAndLagBean> groupOffsetAndLagBeanList = new ArrayList<>();
        for (KafkaOffsetMonitor kafkaOffsetMonitor : KafkaOffsetMonitorList) {
            try {
                Group group = getGroup(kafkaOffsetMonitor.getAddress(), kafkaOffsetMonitor.getGroupId());
                long offset = 0;
                for (Offsets offsets : group.getOffsets()) {
                    if (offsets.getGroup().equals(kafkaOffsetMonitor.getGroupId())) {
                        //计算偏移量
                        offset = offset + (offsets.getLogSize() - offsets.getOffset());
                    }
                }
                GroupOffsetAndLagBean groupOffsetAndLagBean = new GroupOffsetAndLagBean();
                groupOffsetAndLagBean.setTopic(kafkaOffsetMonitor.getTopic());
                groupOffsetAndLagBean.setGroup(kafkaOffsetMonitor.getGroupId());
                groupOffsetAndLagBean.setOffset(offset);
                groupOffsetAndLagBean.setLag(kafkaOffsetMonitor.getLag());
                groupOffsetAndLagBean.setAddress(kafkaOffsetMonitor.getAddress());
                groupOffsetAndLagBeanList.add(groupOffsetAndLagBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return groupOffsetAndLagBeanList;
    }

    @Override
    public Object getGroupAndKafkaMonitor(String address, String topic, String groupId) throws UnirestException {
        GroupAndKafkaMonitorBean groupAndKafkaMonitorBean = new GroupAndKafkaMonitorBean();
        //获取监控对象
        KafkaOffsetMonitor kafkaOffsetMonitor = new KafkaOffsetMonitor();
        kafkaOffsetMonitor.setDeleted(false);
        kafkaOffsetMonitor.setAddress(address);
        kafkaOffsetMonitor.setTopic(topic);
        kafkaOffsetMonitor.setGroupId(groupId);
        Example<KafkaOffsetMonitor> example = Example.of(kafkaOffsetMonitor);
        kafkaOffsetMonitor = kafkaOffsetMonitorRepository.findOne(example);
        if (kafkaOffsetMonitor == null) {
            return groupAndKafkaMonitorBean;
        }
        Group group = getGroup(address, groupId);
        //过滤topic
        updateGroup(group, topic);
        groupAndKafkaMonitorBean.setAddress(address);
        groupAndKafkaMonitorBean.setGroup(group);
        groupAndKafkaMonitorBean.setKafkaOffsetMonitor(kafkaOffsetMonitor);
        //重新整合group
        return groupAndKafkaMonitorBean;
    }

    @Override
    public Object getKafkaMonitorEmails(String id) {
        Set<EmailAddress> emails = new HashSet<>();
        List<IdAndEmail> idAndEmailList = idAndEmailRepository.getIdAndEmailsByIds(id);
        for (IdAndEmail idAndEmail : idAndEmailList) {
            emails.add(emailAddresssRepository.getEmailAddressByEmailAndDeleted(idAndEmail.getEmail(), false));
        }
        return emails;
    }

    @Override
    public KafkaOffsetMonitor getKafkaMonitorById(String id) {
        return kafkaOffsetMonitorRepository.findById(id);
    }

    /**
     * 提取group，只保留当前topic下的数据
     *
     * @param topic
     */
    private void updateGroup(Group group, String topic) {
        List<Offsets> offsets = new ArrayList<>();
        for (Offsets offsets1 : group.getOffsets()) {
            if (offsets1.getTopic().equals(topic)) {
                offsets.add(offsets1);
            }
        }
        group.setOffsets(offsets);

    }

}
