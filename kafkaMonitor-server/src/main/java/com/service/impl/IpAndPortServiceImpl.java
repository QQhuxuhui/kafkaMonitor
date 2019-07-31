package com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.common.PortConfig;
import com.common.utils.IpUtil;
import com.service.IpAndPortService;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/24 17:31
 * @Description:
 */
@Service
public class IpAndPortServiceImpl implements IpAndPortService {

    @Autowired
    private PortConfig portConfig;

    @Override
    public String getIp() {
        return IpUtil.INTERNET_IP;
    }

    @Override
    public int port() {
        return portConfig.getServerPort();
    }
}
