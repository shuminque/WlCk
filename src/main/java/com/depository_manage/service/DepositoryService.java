package com.depository_manage.service;

import com.depository_manage.entity.Depository;
import com.depository_manage.pojo.DepositoryP;

import java.util.List;
import java.util.Map;


/**
 * 仓库类的服务层
 */
public interface DepositoryService {
    /**
     * 插入一条仓库记录
     * @param map 参数map
     * @return 受影响的行数
     */
    Integer insertDepository(Map<String,Object> map);

    /**
     * 查询所有仓库
     * @return 仓库集合
     */
    List<Depository> findDepositoryAll();
    Integer findCountByCondition(Map<String,Object> map);
    List<DepositoryP> findDepositoryPByCondition(Map<String, Object> map);

}
