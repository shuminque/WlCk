package com.depository_manage.controller;

import com.depository_manage.entity.OnceFill;
import com.depository_manage.service.OnceFillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/onceFill")
public class OnceFillController {

    @Autowired
    private OnceFillService onceFillService;

    // 添加一次性记录
    @PostMapping("/add")
    public ResponseEntity<Void> addRecords(@RequestBody List<OnceFill> records) {
        try {
            // 假设您有一个服务来处理数据的保存
            onceFillService.saveAll(records);
            // 返回成功响应，HTTP 204 No Content
            return ResponseEntity.noContent().build();
        } catch(Exception e) {
            // 返回错误响应，HTTP 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 更新一次性记录
    @PostMapping("/update")
    public Map<String, Object> updateOnceFill(@RequestBody Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            onceFillService.updateOnceFill(map);
            resultMap.put("status", "success");
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    // 根据ID查询一次性记录
    @GetMapping("/get/{id}")
    public OnceFill findOnceFillById(@PathVariable Integer id) {
        return onceFillService.findOnceFillById(id);
    }

    // 根据条件查询一次性记录
    @PostMapping("/list")
    public List<OnceFill> findOnceFillByCondition(@RequestBody Map<String, Object> map) {
        return onceFillService.findOnceFillByCondition(map);
    }

    // 根据ID删除一次性记录
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteOnceFillById(@PathVariable Integer id) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            onceFillService.deleteOnceFillById(id);
            resultMap.put("status", "success");
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
}
