package com.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bean.kafka.KafkaOffsetMonitorManager;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/8 10:43
 * @Description: kafka监控服务dao
 */
public interface KafkaOffsetMonitorManagerRepository extends JpaRepository<KafkaOffsetMonitorManager, String> {
}
