package com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bean.ResponseBean;
import com.bean.user.UserInfo;
import com.common.constants.LoginStatusEnum;
import com.common.constants.RegisterStatusEnum;
import com.common.email.SpringMail;
import com.common.utils.UUIDUtil;
import com.dao.jpa.UserRepository;
import com.service.IpAndPortService;
import com.service.UserService;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/15 17:40
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpringMail springMail;

    @Autowired
    private IpAndPortService ipAndPortService;

    @Autowired
    private TokenServiceImpl tokenService;

    @Override
    public UserInfo login(UserInfo userInfo) {
        return userRepository.findUserInfoByUsername(userInfo.getUsername());
    }

    @Override
    public UserInfo getUserInfoByUserName(String username) {
        return userRepository.findUserInfoByUsername(username);
    }

    @Override
    public ResponseBean register(UserInfo userInfo) {
        ResponseBean responseBean = new ResponseBean();
        //首先验证用户是否存在
        UserInfo userInfoDb = userRepository.findUserInfoByUsername(userInfo.getUsername());
        if (userInfoDb != null) {
            responseBean.setCode(RegisterStatusEnum.USERNAME_EXIST.getCode());
            responseBean.setMsg(RegisterStatusEnum.USERNAME_EXIST.getMessage());
            return responseBean;
        }
        userInfoDb = userRepository.findUserInfoByName(userInfo.getName());
        if (userInfoDb != null) {
            responseBean.setCode(RegisterStatusEnum.NAME_EXIST.getCode());
            responseBean.setMsg(RegisterStatusEnum.NAME_EXIST.getMessage());
            return responseBean;
        }
        userInfoDb = userRepository.findUserInfoByEmail(userInfo.getEmail());
        if (userInfoDb != null) {
            responseBean.setCode(RegisterStatusEnum.EMAIL_EXIST.getCode());
            responseBean.setMsg(RegisterStatusEnum.EMAIL_EXIST.getMessage());
            return responseBean;
        }
        //注册完不予激活，需要邮箱验证激活
        userInfo.setState(LoginStatusEnum.USER_NOT_ACTIVE.getCode());
        //随机生成密码盐
        userInfo.setSalt(UUIDUtil.getUUID32());
        userInfo = userRepository.save(userInfo);
        responseBean.setCode(200);
        responseBean.setMsg("注册成功");
        responseBean.setData(userInfo);
        return responseBean;
    }

    @Override
    public ResponseBean active(UserInfo userInfo) {
        ResponseBean responseBean = new ResponseBean();
        //发送激活邮件
        userInfo = userRepository.findUserInfoByUsernameAndPassword(userInfo.getUsername(), userInfo.getPassword());
        if (userInfo == null) {
            responseBean.setCode(202);
            responseBean.setMsg("激活邮件发送失败,用户名或密码错误");
            return responseBean;
        }
        Map<String, Object> params = new HashMap<>();
        //activeUrl
        String url = "http://" + ipAndPortService.getIp() + ":" + ipAndPortService.port() + "/api/user/activeEmail?token=" + tokenService.sign(userInfo);
        params.put("activeUrl", url);
        params.put("username", userInfo.getUsername());
        try {
            springMail.doSendException("账号激活", "userActvie.ftl", params, userInfo.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            responseBean.setCode(201);
            responseBean.setMsg("激活邮件发送失败");
            responseBean.setData(e.getMessage());
        }
        return responseBean;
    }


}
