package com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bean.IdAndEmail;
import com.dao.jpa.IdAndEmailRepository;
import com.service.IdAndEmailService;

import java.util.List;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/8 16:42
 * @Description:
 */
@Service
public class IdAndEmailServiceImpl implements IdAndEmailService{

    @Autowired
    private IdAndEmailRepository idAndEmailRepository;

    @Override
    public List<IdAndEmail> getIdAndEmailListByIds(String ids) {
        return idAndEmailRepository.getIdAndEmailsByIds(ids);
    }
}
