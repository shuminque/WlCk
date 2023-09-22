package com.depository_manage.controller;

import com.depository_manage.entity.Category;
import com.depository_manage.pojo.RestResponse;
import com.depository_manage.service.CategoryService;
import com.depository_manage.utils.CrudUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

//    @GetMapping("/sab")
//    public List<Category> getSABCategories() {
//        return categoryService.getAllSABCategories();
//    }
    @GetMapping("/sab")
    public ResponseEntity<Map<String, Object>> getSABCategories() {
        List<Category> categories = categoryService.getAllSABCategories();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("msg", "");
        response.put("data", categories);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/zab")
    public ResponseEntity<Map<String, Object>> getZABCategories() {
        List<Category> categories = categoryService.getAllZABCategories();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("count",2);
        response.put("data", categories);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public RestResponse addCategory(@RequestBody Category category) {
        int result = categoryService.addCategory(category);
        return CrudUtil.postHandle(result, 1);  // 假设 CrudUtil 有一个名为 addHandle 的方法
    }

    @PutMapping("/update")
    public RestResponse updateMaterial(@RequestBody Map<String, Object> map) {
        int result = categoryService.update(map);
        return CrudUtil.putHandle(result, 1);  // 假设 CrudUtil 有一个名为 updateHandle 的方法
    }


    @DeleteMapping("/delete/{id}")
    public RestResponse deleteCategory(@PathVariable int id){
        int result = categoryService.deleteCategory(id);
        return CrudUtil.deleteHandle(result,1);
    }
}
