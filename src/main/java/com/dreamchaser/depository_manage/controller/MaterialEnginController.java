package com.dreamchaser.depository_manage.controller;

import com.dreamchaser.depository_manage.pojo.RestResponse;
import com.dreamchaser.depository_manage.service.MaterialEnginService;
import com.dreamchaser.depository_manage.utils.CrudUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MaterialEnginController {
    @Autowired
    MaterialEnginService materialEnginService;
    @PostMapping("/materialEngin")
    public RestResponse insertMaterialEngin(@RequestBody Map<String,Object> map){
        return CrudUtil.postHandle(materialEnginService.insertMaterialEngin(map),1);
    }
    @GetMapping("/materialEngin")
    public RestResponse findMaterialEngin(@RequestParam Map<String,Object> map){
        return new RestResponse(materialEnginService.findMaterialEnginAll(),materialEnginService.findCountByCondition(map),200);
    }
    @DeleteMapping("/materialEngin/{id}")
    public RestResponse deleteMaterialEngin(@PathVariable int id) {
        int result = materialEnginService.deleteMaterialEngin(id);
        return CrudUtil.deleteHandle(result,1);
    }
}
