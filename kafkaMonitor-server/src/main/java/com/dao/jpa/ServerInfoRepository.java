package com.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bean.shell.ServerInfoBean;

/**
 * @Auther: huxuhui
 * @Date: 2019/5/6 11:17
 * @Description:
 */
public interface ServerInfoRepository extends JpaRepository<ServerInfoBean, String> {
}
