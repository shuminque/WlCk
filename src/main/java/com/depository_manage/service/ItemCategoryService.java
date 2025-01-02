package com.depository_manage.service;

import com.depository_manage.entity.ItemCategory;
import java.util.List;
import java.util.Map;

public interface ItemCategoryService {

    // 查询所有物品类别映射
    List<ItemCategory> findAll(Map<String, Object> params);

    // 根据物品ID查询物品类别映射
    ItemCategory findByAtId(Integer atId);

    // 插入新的物品类别映射
    int insert(ItemCategory itemCategory);

    // 更新物品类别映射
    int update(ItemCategory itemCategory);

    // 删除物品类别映射
    int deleteById(Integer id);

    List<Map<String, Object>> findTotalQuantityByCategoryAndEngin(Map<String, Object> params);
    List<Map<String, Object>> findTotalQuantityByCategoryAndEnginTwo(Map<String, Object> params);

}
