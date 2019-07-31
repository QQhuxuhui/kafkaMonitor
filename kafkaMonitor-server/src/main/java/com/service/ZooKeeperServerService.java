package com.service;

import com.bean.zookeeper.ZooKeeperInfo;

import java.util.List;

/**
 * @Auther: huxuhui
 * @Date: 2019/5/7 11:21
 * @Description:
 */
public interface ZooKeeperServerService {

    ZooKeeperInfo save(ZooKeeperInfo zooKeeperInfo);

    List<ZooKeeperInfo> getZooKeeperInfoList();

    Boolean testConn(String address);
}
