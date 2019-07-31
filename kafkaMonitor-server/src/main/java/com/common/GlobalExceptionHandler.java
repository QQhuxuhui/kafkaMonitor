package com.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bean.ResponseBean;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/2 10:32
 * @Description:
 */
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(e);
        responseBean.setCode(500);
        responseBean.setMsg(e.getMessage());
        e.printStackTrace();
        return responseBean;
    }



}
