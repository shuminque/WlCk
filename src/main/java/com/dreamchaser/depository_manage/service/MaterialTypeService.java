package com.dreamchaser.depository_manage.service;

import com.dreamchaser.depository_manage.entity.MaterialType;
import com.dreamchaser.depository_manage.pojo.MaterialTypeP;

import java.util.List;
import java.util.Map;

public interface MaterialTypeService {
    Integer insertMaterialType(Map<String,Object> map);
    List<MaterialType> findMaterialTypeAll();
    Integer findCountByCondition(Map<String,Object> map);
    public List<MaterialTypeP> findMaterialTypePByCondition(Map<String, Object> map);

    Integer deleteMaterialType(int id);
}
