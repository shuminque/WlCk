package com.dreamchaser.depository_manage.service;

import com.dreamchaser.depository_manage.entity.Depository;
import com.dreamchaser.depository_manage.entity.MaterialType;
import com.dreamchaser.depository_manage.pojo.DepositoryP;
import com.dreamchaser.depository_manage.pojo.MaterialTypeP;

import java.util.List;
import java.util.Map;

/**
 * 材料的服务层接口
 */
public interface MaterialTypeService {
    /**
     * 插入一条材料类型记录
     * @param map 参数map
     * @return 受影响的数量
     */
    Integer insertMaterialType(Map<String,Object> map);

    List<MaterialType> findMaterialTypeAll();
    Integer findCountByCondition(Map<String,Object> map);
    public List<MaterialTypeP> findMaterialTypePByCondition(Map<String, Object> map);
}
