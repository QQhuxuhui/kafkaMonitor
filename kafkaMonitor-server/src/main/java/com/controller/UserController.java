package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bean.ResponseBean;
import com.bean.user.UserInfo;
import com.common.constants.LoginStatusEnum;
import com.dao.jpa.UserRepository;
import com.service.TokenService;
import com.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/15 17:38
 * @Description:
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("login")
    public ResponseBean login(@RequestBody UserInfo userInfo, HttpServletRequest request, HttpServletResponse response) {
        ResponseBean responseBean = new ResponseBean();
        UserInfo userLogin = userService.login(userInfo);
        if (userLogin == null) {
            //用户不存在
            responseBean.setCode(LoginStatusEnum.USER_LOGIN_NO_USER.getCode());
            responseBean.setMsg(LoginStatusEnum.USER_LOGIN_NO_USER.getMessage());
        } else {
            if (!userLogin.getPassword().equals(userInfo.getPassword())) {
                //密码错误
                responseBean.setCode(LoginStatusEnum.USER_LOGIN_AUTH_FAILE.getCode());
                responseBean.setMsg(LoginStatusEnum.USER_LOGIN_AUTH_FAILE.getMessage());
            } else {
                //登陆成功
                if (userLogin.getState() == LoginStatusEnum.USER_LOGIN_SUCCESS.getCode()) {
                    responseBean.setCode(LoginStatusEnum.USER_LOGIN_SUCCESS.getCode());
                    responseBean.setMsg(LoginStatusEnum.USER_LOGIN_SUCCESS.getMessage());
                    //这里就简单用用用户名和密码加密作为token
                    String token = tokenService.sign(userLogin);
                    UserInfo userInfo1 = new UserInfo();
                    userInfo1.setToken(token);
                    userInfo1.setName(userLogin.getName());
                    userInfo1.setUsername(userLogin.getUsername());
                    responseBean.setData(userInfo1);
                }
                //账号未激活
                if (userLogin.getState() == LoginStatusEnum.USER_NOT_ACTIVE.getCode()) {
                    responseBean.setCode(LoginStatusEnum.USER_NOT_ACTIVE.getCode());
                    responseBean.setMsg(LoginStatusEnum.USER_NOT_ACTIVE.getMessage());
                }
            }
        }
        return responseBean;
    }


    @RequestMapping("register")
    public ResponseBean register(@RequestBody UserInfo userInfo, HttpServletRequest request, HttpServletResponse response) {
        ResponseBean responseBean = userService.register(userInfo);
        return responseBean;
    }

    @RequestMapping("active")
    public ResponseBean active(@RequestBody UserInfo userInfo, HttpServletRequest request, HttpServletResponse response) {
        ResponseBean responseBean = userService.active(userInfo);
        return responseBean;
    }

    /**
     * 激活账号
     *
     * @param token
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("activeEmail")
    public ResponseBean activeEmail(String token, HttpServletRequest request, HttpServletResponse response) {
        ResponseBean responseBean = new ResponseBean();
        String username = tokenService.getUserName(token);
        if (username == null) {
            responseBean.setCode(201);
            responseBean.setMsg("认证信息已过期，请重新激活");
            return responseBean;
        }
        UserInfo userInfo = userService.getUserInfoByUserName(username);
        if (userInfo == null) {
            responseBean.setCode(202);
            responseBean.setMsg("用户信息已被删除，请重新注册");
            return responseBean;
        }
        userInfo.setState(LoginStatusEnum.USER_LOGIN_SUCCESS.getCode());
        userRepository.save(userInfo);
        responseBean.setMsg("账号" + userInfo.getUsername() + "激活成功");
        return responseBean;
    }


    /**
     * 用户退出
     *
     * @param request
     * @return
     */
    @RequestMapping("/loginOut")
    public ResponseBean loginOut(HttpServletRequest request) {
        ResponseBean responseBean = new ResponseBean();
        tokenService.expire(request.getHeader("authorization"));
        responseBean.setCode(LoginStatusEnum.USER_NOT_LOGIN_IN_STATUS.getCode());
        responseBean.setMsg(LoginStatusEnum.USER_NOT_LOGIN_IN_STATUS.getMessage());
        return responseBean;
    }

}
