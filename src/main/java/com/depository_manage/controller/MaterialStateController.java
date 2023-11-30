package com.depository_manage.controller;

import com.depository_manage.pojo.RestResponse;
import com.depository_manage.service.MaterialStateService;
import com.depository_manage.utils.CrudUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MaterialStateController {
    @Autowired
    MaterialStateService materialStateService;
    @PostMapping("/materialState")
    public RestResponse insertMaterialState(@RequestBody Map<String,Object> map){
        return CrudUtil.postHandle(materialStateService.insertMaterialState(map),1);
    }
    @GetMapping("/materialState")
    public RestResponse findMaterialState(@RequestParam Map<String,Object> map){
        return new RestResponse(materialStateService.findMaterialStateAll(),materialStateService.findCountByCondition(map),200);
    }
    @DeleteMapping("/materialState/{id}")
    public RestResponse deleteMaterialState(@PathVariable int id) {
        int result = materialStateService.deleteMaterialState(id);
        return CrudUtil.deleteHandle(result,1);
    }

}
