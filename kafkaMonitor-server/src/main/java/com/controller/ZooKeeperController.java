package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bean.ResponseBean;
import com.bean.user.UserInfo;
import com.bean.zookeeper.ZooKeeperInfo;
import com.service.TokenService;
import com.service.UserService;
import com.service.ZooKeeperServerService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Auther: huxuhui
 * @Date: 2019/5/7 11:36
 * @Description:
 */
@RestController
@RequestMapping("/api/zookeeper")
public class ZooKeeperController {

    @Autowired
    private ZooKeeperServerService zooKeeperServerService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @RequestMapping("/getZooKeeperServerList")
    public Object getZooKeeperServerList() {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(zooKeeperServerService.getZooKeeperInfoList());
        return responseBean;
    }

    @RequestMapping("/save")
    public Object save(@RequestBody ZooKeeperInfo zooKeeperInfo, HttpServletRequest request) {
        ResponseBean responseBean = new ResponseBean();
        UserInfo userInfo = userService.getUserInfoByUserName(getUserName(request));
        zooKeeperInfo.setCreateBy(userInfo.getName());
        zooKeeperInfo.setCreateDate(new Date());
        zooKeeperInfo.setUpdateDate(new Date());
        responseBean.setData(zooKeeperServerService.save(zooKeeperInfo));
        return responseBean;
    }

    @RequestMapping("/testConn")
    public Object testConn(String address) {
        ResponseBean responseBean = new ResponseBean();
        if (!zooKeeperServerService.testConn(address)) {
            responseBean.setCode(201);
            responseBean.setMsg("连接失败");
        }
        return responseBean;
    }

    private String getUserName(HttpServletRequest request) {
        return tokenService.getUserName(request.getHeader("authorization"));
    }
}
