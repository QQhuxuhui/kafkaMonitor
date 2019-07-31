package com.service;

import com.bean.shell.ServerInfoBean;

import java.util.List;

/**
 * @Auther: huxuhui
 * @Date: 2019/5/6 11:12
 * @Description:
 */
public interface ShellServerService {

    ServerInfoBean saveServer(ServerInfoBean serverInfoBean);

    List<ServerInfoBean> getServerList();
}
