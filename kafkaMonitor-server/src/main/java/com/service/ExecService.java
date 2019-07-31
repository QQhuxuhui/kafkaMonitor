package com.service;

import com.bean.shell.ServerInfoBean;
import com.bean.shell.ShellResponseBean;

/**
 * @Auther: huxuhui
 * @Date: 2019/3/29 09:44
 * @Description:
 */
public interface ExecService {

    ShellResponseBean executeCommand(ServerInfoBean loginInfoBean, String command);
}
