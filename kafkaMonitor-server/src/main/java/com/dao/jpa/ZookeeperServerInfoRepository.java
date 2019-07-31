package com.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bean.zookeeper.ZooKeeperInfo;

/**
 * @Auther: huxuhui
 * @Date: 2019/5/7 11:20
 * @Description:
 */
public interface ZookeeperServerInfoRepository extends JpaRepository<ZooKeeperInfo,String> {

}
