package com.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bean.kafka.KafkaOffsetMonitor;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/8 10:36
 * @Description: kafka监控条目dao
 */
public interface KafkaOffsetMonitorRepository extends JpaRepository<KafkaOffsetMonitor, String> {

    KafkaOffsetMonitor findById(String id);

}
