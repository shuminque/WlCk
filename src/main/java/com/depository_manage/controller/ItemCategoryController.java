package com.depository_manage.controller;

import com.depository_manage.entity.ItemCategory;
import com.depository_manage.pojo.RestResponse;
import com.depository_manage.service.ItemCategoryService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/itemCategories")
public class ItemCategoryController {

    private final ItemCategoryService itemCategoryService;

    // 构造方法注入 ItemCategoryService
    public ItemCategoryController(ItemCategoryService itemCategoryService) {
        this.itemCategoryService = itemCategoryService;
    }

    // 获取所有物品类别映射
    @GetMapping("/")
    public ResponseEntity<List<ItemCategory>> getAll(@RequestParam Map<String, Object> params) {
        List<ItemCategory> itemCategories = itemCategoryService.findAll(params);
        return ResponseEntity.ok(itemCategories);
    }

    // 根据物品ID查询物品类别映射
    @GetMapping("/{atId}")
    public ResponseEntity<ItemCategory> getByAtId(@PathVariable Integer atId) {
        ItemCategory itemCategory = itemCategoryService.findByAtId(atId);
        if (itemCategory != null) {
            return ResponseEntity.ok(itemCategory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 插入新的物品类别映射
    @PostMapping("/")
    public ResponseEntity<Void> create(@RequestBody ItemCategory itemCategory) {
        itemCategoryService.insert(itemCategory);
        return ResponseEntity.ok().build();
    }

    // 更新物品类别映射
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody ItemCategory itemCategory) {
        itemCategory.setId(id);  // 设置传入的 ID
        itemCategoryService.update(itemCategory);
        return ResponseEntity.ok().build();
    }

    // 删除物品类别映射
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        itemCategoryService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // 查询各分类和引擎的总数量
    @GetMapping("/totalQuantity")
    public RestResponse  getTotalQuantity(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate+ " 00:00:00");
        params.put("endDate", endDate+ " 23:59:59");
        List<Map<String, Object>> results = itemCategoryService.findTotalQuantityByCategoryAndEngin(params);
        RestResponse response = new RestResponse();
        response.setStatus(200);
        response.setData(results);
        response.setCount(results.size());
        return response;
    }
}