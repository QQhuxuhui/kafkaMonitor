package com.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.bean.IdAndEmail;
import com.bean.kafka.Group;
import com.bean.kafka.KafkaOffsetMonitor;
import com.bean.kafka.Offsets;
import com.common.email.SpringMail;
import com.service.IdAndEmailService;
import com.service.KafkaOffsetMonitorService;

import java.util.*;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/2 10:22
 * @Description: kafka偏移量监控
 */
@Component
public class KafkaOffsetAlter {

    @Autowired
    private KafkaOffsetMonitorService kafkaOffsetMonitorService;

    @Autowired
    private SpringMail springMail;

    @Autowired
    private IdAndEmailService idAndEmailService;


    //记录已经提醒过得数据和时间
    static Map<String, Long> alterMap = new HashMap<>();

    /**
     * 2分钟执行一次
     */
    @Scheduled(fixedDelay = 1000 * 60 * 2)
    public void kafkaOffset() {
        List<KafkaOffsetMonitor> KafkaOffsetMonitorList = kafkaOffsetMonitorService.getKafkaMonitorList();
        for (KafkaOffsetMonitor kafkaOffsetMonitor : KafkaOffsetMonitorList) {
            if (!kafkaOffsetMonitor.getEmailAlter() || kafkaOffsetMonitor.getDeleted()) {
                continue;
            }
            try {
                Group group = kafkaOffsetMonitorService.getGroup(kafkaOffsetMonitor.getAddress(), kafkaOffsetMonitor.getGroupId());
                long lag = 0;
                for (Offsets offsets : group.getOffsets()) {
                    if (offsets.getGroup().equals(kafkaOffsetMonitor.getGroupId())) {
                        //计算偏移量
                        lag = lag + (offsets.getLogSize() - offsets.getOffset());
                    }
                }
                if (lag > kafkaOffsetMonitor.getLag()) {
                    /**
                     * 监控条件：
                     * 1、内存中的记录存在
                     * 2、发送时间差在30分钟以内的，不再重复发送
                     */
                    if (alterMap.containsKey(kafkaOffsetMonitor.getId()) &&
                            System.currentTimeMillis() - alterMap.get(kafkaOffsetMonitor.getId()) < 1000 * 60 * 30) {
                        //不满足监控条件
                        continue;
                    }
                    //超过阈值，开始报警
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("address", kafkaOffsetMonitor.getAddress());
                    params.put("topic", kafkaOffsetMonitor.getTopic());
                    params.put("groupId", kafkaOffsetMonitor.getGroupId());
                    params.put("lag", kafkaOffsetMonitor.getLag());
                    params.put("current", lag);
                    Set<String> emails = new HashSet<>();
                    List<IdAndEmail> idAndEmailList = idAndEmailService.getIdAndEmailListByIds(kafkaOffsetMonitor.getId());
                    for (IdAndEmail idAndEmail : idAndEmailList) {
                        emails.add(idAndEmail.getEmail());
                    }
                    String[] array = new String[emails.size()];
                    emails.toArray(array);
                    if (array.length == 0) {
                        continue;
                    }
                    springMail.doSend("kafka数据堵塞通知", "kafkaOffsetInfo.ftl", params, array);
                    alterMap.put(kafkaOffsetMonitor.getId(), System.currentTimeMillis());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
