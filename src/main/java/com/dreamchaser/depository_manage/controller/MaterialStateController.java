package com.dreamchaser.depository_manage.controller;

import com.dreamchaser.depository_manage.pojo.RestResponse;
import com.dreamchaser.depository_manage.service.MaterialStateService;
import com.dreamchaser.depository_manage.service.MaterialTypeService;
import com.dreamchaser.depository_manage.utils.CrudUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MaterialStateController {
    @Autowired
    MaterialStateService materialStateService;
    @PostMapping("/materialState")
    public RestResponse insertMaterialState(@RequestBody Map<String,Object> map){
        return CrudUtil.postHandle(materialStateService.insertMaterialState(map),1);
    }

}
