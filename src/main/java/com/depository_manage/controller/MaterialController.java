package com.depository_manage.controller;

import com.depository_manage.pojo.RestResponse;
import com.depository_manage.service.MaterialService;
import com.depository_manage.utils.CrudUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 */
@RestController
public class MaterialController {
    @Autowired
    MaterialService materialService;
    @GetMapping("/material")
    public RestResponse findMaterial(@RequestParam Map<String,Object> map){
        return new RestResponse(materialService.findMaterialPByCondition(map),materialService.findCountByCondition(map),200);
    }
    @PostMapping("/material")
    public RestResponse insertMaterial(@RequestParam Map<String,Object> map){
        return CrudUtil.postHandle(materialService.insertMaterial(map),1);
    }
    @PostMapping("/instmaterial")
    public RestResponse addinsertMaterial(@RequestBody Map<String,Object> map) {
        for(Map.Entry<String, Object> entry : map.entrySet()){
            if("".equals(entry.getValue())){
                map.put(entry.getKey(), null);
            }
        }
        return CrudUtil.postHandle(materialService.insertMaterial(map),1);
    }

    @DeleteMapping("/material/{id}")
    public RestResponse deleteMaterial(@PathVariable int id){
        int result = materialService.deleteMaterialById(id);
        return CrudUtil.deleteHandle(result,1);
    }
}
