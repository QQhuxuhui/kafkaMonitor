package com.service.impl;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.bean.shell.ServerInfoBean;
import com.bean.shell.ShellResponseBean;
import com.bean.shell.ShellResponseCodeEnum;
import com.common.shell.SessionFactory;
import com.service.ExecService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: huxuhui
 * @Date: 2019/3/28 15:42
 * @Description:
 */
public class ExecServiceImpl implements ExecService {

    Logger logger = LoggerFactory.getLogger(ExecServiceImpl.class);

    @Override
    public ShellResponseBean executeCommand(ServerInfoBean loginInfoBean, String command) {
        ShellResponseBean shellResponseBean = new ShellResponseBean();
        List<String> list = new ArrayList<>();
        try {
            Channel channel = SessionFactory.connect(loginInfoBean).openChannel("exec");
            channel.setInputStream(null);
            ((ChannelExec) channel).setCommand(command);
            InputStream errStream = ((ChannelExec) channel).getErrStream();
            InputStream in = channel.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            BufferedReader errReader = new BufferedReader(new InputStreamReader(errStream));
            channel.connect();
            String str = bufferedReader.readLine();
            String errStr = errReader.readLine();
            if (errStr != null) {
                while (true) {
                    while (errStr != null) {
                        list.add(errStr);
                        errStr = errReader.readLine();
                    }
                    if (channel.isClosed()) {
                        System.out.println("exit-status: " + channel.getExitStatus());
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                        logger.info("sleep 1s");
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
                shellResponseBean.setCode(ShellResponseCodeEnum.FAILE.getCode());
            }else{
                while (true) {
                    while (str != null) {
                        list.add(str);
                        str = bufferedReader.readLine();
                    }
                    if (channel.isClosed()) {
                        System.out.println("exit-status: " + channel.getExitStatus());
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            }
            channel.disconnect();
            SessionFactory.disConnected(loginInfoBean);
        } catch (Exception e) {
            shellResponseBean.setCode(ShellResponseCodeEnum.EXCEPTION.getCode());
            shellResponseBean.setMsg(e.getMessage());
            e.printStackTrace();
        }
        shellResponseBean.setData(list);
        return shellResponseBean;
    }

    public static void main(String[] args) {
        ServerInfoBean loginInfoBean = new ServerInfoBean();
        loginInfoBean.setIp("10.80.0.73");
        ExecService execService = new ExecServiceImpl();
        ShellResponseBean shellResponseBean = execService.executeCommand(loginInfoBean, "/usr/hdp/2.5.0.0-1245/kafka/bin/kafka-consumer-groups.sh --zookeeper xcmg55.xcmg.com:2181 --group xiothub_schwing_rawdata_history_xgit_xcmg_schwing_data --describe");
        for (String s : shellResponseBean.getData()) {
            System.out.println(s);
        }
    }
}
