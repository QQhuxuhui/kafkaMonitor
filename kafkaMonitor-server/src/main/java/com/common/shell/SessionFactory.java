package com.common.shell;

import com.bean.BeanFactory;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.bean.shell.ServerInfoBean;
import com.bean.shell.MyUserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: huxuhui
 * @Date: 2019/3/28 16:20
 * @Description: Seesion工厂类
 */
public class SessionFactory {

    private static Map<String, Session> sessionMap = new HashMap<>();

    static {
        Map<String, ServerInfoBean> loginInfoBeanMap = BeanFactory.getBeans(ServerInfoBean.class);
        for (String key : loginInfoBeanMap.keySet()) {
            ServerInfoBean loginInfoBean = loginInfoBeanMap.get(key);
            JSch jsch = new JSch();
            try {
                Session session = jsch.getSession(loginInfoBean.getUsername(), loginInfoBean.getIp(), loginInfoBean.getPort());
                session.setPassword(loginInfoBean.getPassword());
                UserInfo userInfo = new MyUserInfo();
                session.setUserInfo(userInfo);
                sessionMap.put(loginInfoBeanMap.get(key).getIp(), session);
            } catch (JSchException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 增加、修改session
     *
     * @param loginInfoBean
     */
    private static void addSession(ServerInfoBean loginInfoBean) {
        sessionMap.put(loginInfoBean.getIp(), createSession(loginInfoBean));
    }

    public static void removeSession(ServerInfoBean loginInfoBean) {
        sessionMap.remove(loginInfoBean.getIp());
    }

    /**
     * 判断连接是否已建立
     *
     * @param loginInfoBean
     * @return
     */
    private static boolean isConnected(ServerInfoBean loginInfoBean) {
        if (exsitSession(loginInfoBean)) {
            return getSession(loginInfoBean).isConnected();
        }
        return false;
    }

    /**
     * 判断是否存在连接信息
     *
     * @param loginInfoBean
     * @return
     */
    private static boolean exsitSession(ServerInfoBean loginInfoBean) {
        if (sessionMap.containsKey(loginInfoBean.getIp())) {
            return true;
        }
        return false;
    }

    /**
     * 获取session
     *
     * @param loginInfoBean
     * @return
     */
    private static Session getSession(ServerInfoBean loginInfoBean) {
        return sessionMap.get(loginInfoBean.getIp());
    }

    /**
     * 建立连接
     *
     * @param loginInfoBean
     * @return
     */
    public static Session connect(ServerInfoBean loginInfoBean) {
        if (!exsitSession(loginInfoBean)) {
            addSession(loginInfoBean);
        }
        if (!isConnected(loginInfoBean)) {
            try {
                getSession(loginInfoBean).connect();
            } catch (JSchException e) {
                e.printStackTrace();
            }
        }
        return getSession(loginInfoBean);
    }

    public static void disConnected(ServerInfoBean loginInfoBean){
        if (exsitSession(loginInfoBean)) {
            getSession(loginInfoBean).disconnect();
        }
    }

    /**
     * 创建session
     *
     * @param loginInfoBean
     * @return
     */
    private static Session createSession(ServerInfoBean loginInfoBean) {
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(loginInfoBean.getUsername(), loginInfoBean.getIp(), loginInfoBean.getPort());
            session.setPassword(loginInfoBean.getPassword());
            UserInfo userInfo = new MyUserInfo();
            session.setUserInfo(userInfo);
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return session;
    }
}
