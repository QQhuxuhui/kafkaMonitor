package com.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bean.kafka.TopicInfo;

/**
 * @Auther: huxuhui
 * @Date: 2019/5/6 17:02
 * @Description: topic 信息表
 */
public interface TopicInfoRepository extends JpaRepository<TopicInfo, String> {
    TopicInfo findById(String id);

    TopicInfo findByTopic(String topic);
}
