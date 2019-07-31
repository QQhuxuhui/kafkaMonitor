package com.service.impl;

import kafka.utils.ZkUtils;
import org.apache.kafka.common.security.JaasUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import com.bean.zookeeper.ZooKeeperInfo;
import com.dao.jpa.ZookeeperServerInfoRepository;
import com.service.ZooKeeperServerService;

import java.util.List;

/**
 * @Auther: huxuhui
 * @Date: 2019/5/7 11:22
 * @Description:
 */
@Service
public class ZooKeeperServerServiceImpl implements ZooKeeperServerService {

    @Autowired
    private ZookeeperServerInfoRepository zookeeperServerInfoRepository;

    @Override
    public ZooKeeperInfo save(ZooKeeperInfo zooKeeperInfo) {
        return zookeeperServerInfoRepository.save(zooKeeperInfo);
    }

    @Override
    public List<ZooKeeperInfo> getZooKeeperInfoList() {
        ZooKeeperInfo zooKeeperInfo = new ZooKeeperInfo();
        zooKeeperInfo.setDeleted(false);
        Example<ZooKeeperInfo> example = Example.of(zooKeeperInfo);
        return zookeeperServerInfoRepository.findAll(example);
    }

    @Override
    public Boolean testConn(String address) {
        ZkUtils zkUtils = null;
        try {
            zkUtils = ZkUtils.apply(address, 2000, 2000, JaasUtils.isZkSecurityEnabled());
            zkUtils.getCluster();
        } catch (Exception e) {
            return false;
        } finally {
            if (zkUtils != null) {
                zkUtils.close();
            }
        }
        return true;
    }

}
