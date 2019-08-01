package com.common.interceptors;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.bean.ResponseBean;
import com.common.constants.LoginStatusEnum;
import com.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/19 09:49
 * @Description:
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
    private static final String[] IGNORE_URI = {"/login", "/hbase/dataQuery", "/register", "/active", "/activeEmail", "/map","zookeeper"};
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //预检请求
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        boolean flag = false;
        String url = request.getRequestURL().toString();
        logger.info(">>>: {}", url);
        for (String s : IGNORE_URI) {
            if (url.contains(s)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            if (isLoginUser(request)) {//token校验
                flag = true;
            }
        }
        if (!flag) {//没有登录，则返回状态码
            //response.setStatus(LoginStatusEnum.USER_NOT_LOGIN_IN_STATUS.getCode());
            ResponseBean responseBean = new ResponseBean();
            responseBean.setCode(LoginStatusEnum.USER_NOT_LOGIN_IN_STATUS.getCode());
            responseBean.setMsg(LoginStatusEnum.USER_NOT_LOGIN_IN_STATUS.getMessage());
            printJson(response, responseBean);
        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With, token");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, OPTIONS, POST, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
    }

    /**
     * 判断是否在线
     *
     * @param request
     * @return
     */
    private boolean isLoginUser(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        Map<String, Claim> claimMap = tokenService.verify(token);
        if (claimMap == null) {
            return false;
        }
        return true;
    }

    private static void printJson(HttpServletResponse response, ResponseBean responseBean) {
        String content = JSON.toJSONString(responseBean);
        printContent(response, content);
    }

    private static void printContent(HttpServletResponse response, String content) {
        try {
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With, token");
            response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, OPTIONS, POST, PUT, DELETE");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setContentType("application/json");
//            response.setHeader("Cache-Control", "no-store");
            response.setCharacterEncoding("UTF-8");
            PrintWriter pw = response.getWriter();
            pw.write(content);
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
