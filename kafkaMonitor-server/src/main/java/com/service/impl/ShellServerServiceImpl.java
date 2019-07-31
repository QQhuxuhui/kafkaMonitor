package com.service.impl;

import com.bean.shell.ServerInfoBean;
import com.common.utils.MD5Util;
import com.dao.jpa.ServerInfoRepository;
import com.service.ShellServerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: huxuhui
 * @Date: 2019/5/6 11:12
 * @Description:
 */
@Service
public class ShellServerServiceImpl implements ShellServerService {

    @Autowired
    private ServerInfoRepository serverInfoRepository;

    @Override
    public ServerInfoBean saveServer(ServerInfoBean serverInfoBean) {
        if (StringUtils.isEmpty(serverInfoBean.getId())) {
            serverInfoBean.setId(MD5Util.GetMD5Code(serverInfoBean.getIp() +
                    serverInfoBean.getPort() +
                    serverInfoBean.getUsername()));
        }
        return serverInfoRepository.save(serverInfoBean);
    }

    @Override
    public List<ServerInfoBean> getServerList() {
        ServerInfoBean serverInfoBean = new ServerInfoBean();
        serverInfoBean.setDeleted(false);
        Example<ServerInfoBean> example = Example.of(serverInfoBean);
        return serverInfoRepository.findAll(example);
    }
}
