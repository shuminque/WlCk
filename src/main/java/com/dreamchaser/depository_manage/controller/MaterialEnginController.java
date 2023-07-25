package com.dreamchaser.depository_manage.controller;

import com.dreamchaser.depository_manage.pojo.RestResponse;
import com.dreamchaser.depository_manage.service.MaterialEnginService;
import com.dreamchaser.depository_manage.utils.CrudUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MaterialEnginController {
    @Autowired
    MaterialEnginService materialEnginService;
    @PostMapping("/materialEngin")
    public RestResponse insertMaterialEngin(@RequestBody Map<String,Object> map){
        return CrudUtil.postHandle(materialEnginService.insertMaterialEngin(map),1);
    }

}
