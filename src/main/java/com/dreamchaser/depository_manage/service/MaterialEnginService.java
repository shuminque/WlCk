package com.dreamchaser.depository_manage.service;

import com.dreamchaser.depository_manage.entity.MaterialEngin;

import java.util.List;
import java.util.Map;

public interface MaterialEnginService {
    Integer insertMaterialEngin(Map<String,Object> map);
    List<MaterialEngin> findMaterialEnginAll();
}
