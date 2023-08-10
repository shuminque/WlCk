package com.dreamchaser.depository_manage.controller;

import com.dreamchaser.depository_manage.pojo.RestResponse;
import com.dreamchaser.depository_manage.service.MaterialTypeService;
import com.dreamchaser.depository_manage.utils.CrudUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MaterialTypeController {
    @Autowired
    MaterialTypeService materialTypeService;
    @PostMapping("/materialType")
    public RestResponse insertMaterialType(@RequestBody Map<String,Object> map){
        return CrudUtil.postHandle(materialTypeService.insertMaterialType(map),1);
    }
    @GetMapping("/materialType")
    public RestResponse findMaterialType(@RequestParam Map<String,Object> map){
        return new RestResponse(materialTypeService.findMaterialTypeAll(),materialTypeService.findCountByCondition(map),200);
    }
    @DeleteMapping("/materialType/{id}")
    public RestResponse deleteMaterialType(@PathVariable int id) {
        int result = materialTypeService.deleteMaterialType(id);
        return CrudUtil.deleteHandle(result,1);
    }
}
