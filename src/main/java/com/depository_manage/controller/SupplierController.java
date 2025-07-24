package com.depository_manage.controller;

import com.depository_manage.pojo.RestResponse;
import com.depository_manage.service.SupplierService;
import com.depository_manage.utils.CrudUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    // 添加供应商
    @PostMapping("/supplier")
    public ResponseEntity<RestResponse> insertSupplier(@RequestBody Map<String, Object> map) {
        try {
            RestResponse response = CrudUtil.postHandle(supplierService.insertSupplier(map), 1);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RestResponse("添加错误，检查属性", 500, null));
        }
    }

    // 查询供应商列表 + 条数
    @GetMapping("/supplier")
    public RestResponse findSupplier(@RequestParam Map<String, Object> map) {
        return new RestResponse(
                supplierService.findSupplierAll(),
                supplierService.findCountByCondition(map),
                200
        );
    }

    // 删除供应商
    @DeleteMapping("/supplier/{id}")
    public RestResponse deleteSupplier(@PathVariable int id) {
        int result = supplierService.deleteSupplier(id);
        return CrudUtil.deleteHandle(result, 1);
    }

    // 修改供应商
    @PutMapping("/supplier/{id}")
    public RestResponse updateSupplier(@PathVariable int id, @RequestBody Map<String, Object> map) {
        map.put("id", id);
        int result = supplierService.updateSupplier(map);
        return CrudUtil.putHandle(result, 1);
    }
}
