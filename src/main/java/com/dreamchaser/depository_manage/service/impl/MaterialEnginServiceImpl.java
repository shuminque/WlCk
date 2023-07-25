package com.dreamchaser.depository_manage.service.impl;

import com.dreamchaser.depository_manage.entity.MaterialEngin;
import com.dreamchaser.depository_manage.service.MaterialEnginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 */
@Service
public class MaterialEnginServiceImpl implements MaterialEnginService {
    @Autowired
    MaterialEnginService materialEnginService;
    @Override
    public Integer insertMaterialEngin(Map<String,Object> map) {
        return materialEnginService.insertMaterialEngin(map);
    }

    @Override
    public List<MaterialEngin> findMaterialEnginAll(){
        return materialEnginService.findMaterialEnginAll();
    };
}
