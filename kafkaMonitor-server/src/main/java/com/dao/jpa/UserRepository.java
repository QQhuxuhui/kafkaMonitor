package com.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bean.user.UserInfo;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/15 17:35
 * Long
 */
public interface UserRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findUserInfoByUsername(String username);

    UserInfo findUserInfoByName(String name);

    UserInfo findUserInfoByEmail(String email);

    UserInfo findUserInfoByUsernameAndPassword(String username ,String password);
}
