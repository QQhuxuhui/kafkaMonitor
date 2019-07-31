package com.service;

import com.bean.IdAndEmail;

import java.util.List;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/8 16:42
 * @Description:
 */
public interface IdAndEmailService {
    /**
     * 根据关联的ID查询邮箱信息
     * @param ids
     * @return
     */
    List<IdAndEmail> getIdAndEmailListByIds(String ids);
}
