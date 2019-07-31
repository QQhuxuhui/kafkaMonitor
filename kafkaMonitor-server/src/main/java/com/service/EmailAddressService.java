package com.service;

import com.bean.EmailAddress;

import java.util.List;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/8 17:22
 * @Description:
 */
public interface EmailAddressService {

    /**
     * 获取所有符合条件的邮件数据
     * @return
     */
    List<EmailAddress> getEmailAddressList();

    /**
     * 保存邮箱数据
     * @param emailAddress
     * @return
     */
    EmailAddress saveEmailAddress(EmailAddress emailAddress);
}
