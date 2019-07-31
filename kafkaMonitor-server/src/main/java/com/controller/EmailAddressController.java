package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bean.EmailAddress;
import com.bean.ResponseBean;
import com.service.EmailAddressService;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/8 17:48
 * @Description:
 */
@RestController
@RequestMapping("/email")
public class EmailAddressController {

    @Autowired
    private EmailAddressService emailAddressService;

    @RequestMapping("/getEmailAddressList")
    public ResponseBean getEmailAddressList() {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(emailAddressService.getEmailAddressList());
        return responseBean;
    }

    @RequestMapping("/saveEmailAddress")
    public ResponseBean saveEmailAddress(EmailAddress emailAddress) {
        ResponseBean responseBean = new ResponseBean();
        EmailAddress emailAddressReturn = emailAddressService.saveEmailAddress(emailAddress);
        if (emailAddressReturn == null) {
            responseBean.setCode(201);
        } else {
            responseBean.setData(emailAddressReturn);
        }
        return responseBean;
    }

}
