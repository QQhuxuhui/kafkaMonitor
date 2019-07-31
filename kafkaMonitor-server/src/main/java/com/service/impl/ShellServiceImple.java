package com.service.impl;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.bean.shell.MyUserInfo;

/**
 * @Auther: huxuhui
 * @Date: 2019/3/28 15:38
 * @Description:
 */
public class ShellServiceImple {


    public static void main(String[] arg) {
        try {
            JSch jsch = new JSch();
            String user = "root";
            String host = "10.80.0.76";
            Session session = jsch.getSession(user, host, 22);
            String passwd = "XreaServer_2014";
            session.setPassword(passwd);
            UserInfo userInfo = new MyUserInfo();
            session.setUserInfo(userInfo);
            session.connect(30000); // making a connection with timeout.
            Channel channel = session.openChannel("shell");
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.connect(3 * 1000);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
