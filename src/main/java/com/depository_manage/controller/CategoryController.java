package com.depository_manage.controller;

import com.depository_manage.entity.Category;
import com.depository_manage.pojo.RestResponse;
import com.depository_manage.service.CategoryService;
import com.depository_manage.utils.CrudUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/sab")
    public List<Category> getSABCategories() {
        return categoryService.getAllSABCategories();
    }

    @GetMapping("/zab")
    public List<Category> getZABCategories() {
        return categoryService.getAllZABCategories();
    }

    @PostMapping("/add")
    public Map<String, Boolean> addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return response;
    }

    @PostMapping("/update")
    public Map<String, Boolean> updateCategory(@RequestBody Category category) {
        categoryService.updateCategory(category);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return response;
    }

    @DeleteMapping("/delete/{id}")
    public RestResponse deleteCategory(@PathVariable int id){
        int result = categoryService.deleteCategory(id);
        return CrudUtil.deleteHandle(result,1);
    }
}
