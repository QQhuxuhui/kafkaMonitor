package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bean.ResponseBean;
import com.bean.shell.ServerInfoBean;
import com.bean.user.UserInfo;
import com.service.ShellServerService;
import com.service.TokenService;
import com.service.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: huxuhui
 * @Date: 2019/5/6 11:11
 * @Description: 服务器
 */
@RestController
@RequestMapping("/api/shellServer")
public class ShellServerController {

    @Autowired
    private ShellServerService shellServerService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @RequestMapping("/saveServer")
    public Object saveServer(@RequestBody ServerInfoBean serverInfoBean, HttpServletRequest httpServletRequest) {
        ResponseBean responseBean = new ResponseBean();
        UserInfo userInfo = userService.getUserInfoByUserName(getUserName(httpServletRequest));
        serverInfoBean.setCreateBy(userInfo.getName());
        shellServerService.saveServer(serverInfoBean);
        return responseBean;
    }

    @RequestMapping("/getServerList")
    public Object getServerList(){
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(shellServerService.getServerList());
        return responseBean;
    }

    public String getUserName(HttpServletRequest request) {
        return tokenService.getUserName(request.getHeader("authorization"));
    }
}
