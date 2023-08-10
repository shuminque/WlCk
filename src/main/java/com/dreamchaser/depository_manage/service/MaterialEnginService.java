package com.dreamchaser.depository_manage.service;

import com.dreamchaser.depository_manage.entity.MaterialEngin;
import com.dreamchaser.depository_manage.pojo.MaterialEnginP;

import java.util.List;
import java.util.Map;

public interface MaterialEnginService {
    Integer insertMaterialEngin(Map<String,Object> map);
    List<MaterialEngin> findMaterialEnginAll();
    Integer findCountByCondition(Map<String,Object> map);
    public List<MaterialEnginP> findMaterialEnginPByCondition(Map<String, Object> map);

    Integer deleteMaterialEngin(int id);
}
