package com.service.impl;

import com.common.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import com.bean.EmailAddress;
import com.dao.jpa.EmailAddresssRepository;
import com.service.EmailAddressService;

import java.util.List;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/8 17:22
 * @Description:
 */
@Service
public class EmailAddressServiceImple implements EmailAddressService {

    @Autowired
    private EmailAddresssRepository emailAddresssRepository;

    @Override
    public List<EmailAddress> getEmailAddressList() {
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.setDeleted(false);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().
                withMatcher("deleted", ExampleMatcher.GenericPropertyMatchers.exact());
        Example<EmailAddress> example = Example.of(emailAddress ,exampleMatcher);
        return emailAddresssRepository.findAll(example);
    }

    @Override
    public EmailAddress saveEmailAddress(EmailAddress emailAddress) {
        if (StringUtils.isEmpty(emailAddress.getId())) {
            String id = MD5Util.GetMD5Code("email_address" + emailAddress.getEmail());
            emailAddress.setId(id);
        }
        return emailAddresssRepository.save(emailAddress);
    }
}
