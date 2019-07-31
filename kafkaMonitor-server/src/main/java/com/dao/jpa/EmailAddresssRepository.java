package com.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bean.EmailAddress;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/8 10:43
 * @Description: 联系人Dao
 */
public interface EmailAddresssRepository extends JpaRepository<EmailAddress, String> {
    EmailAddress getEmailAddressByEmailAndDeleted(String email, boolean deleted);
}
