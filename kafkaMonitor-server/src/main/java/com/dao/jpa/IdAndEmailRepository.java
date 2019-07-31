package com.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import com.bean.IdAndEmail;

import java.util.List;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/8 10:46
 * @Description: id和邮箱的关联表dao
 */
public interface IdAndEmailRepository extends JpaRepository<IdAndEmail, String> {

    @Transactional
    @Modifying
    void deleteByIds(String ids);

    List<IdAndEmail> getIdAndEmailsByIds(String ids);
}
