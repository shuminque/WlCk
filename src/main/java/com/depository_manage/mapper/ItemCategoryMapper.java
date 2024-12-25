package com.depository_manage.mapper;

import com.depository_manage.entity.ItemCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemCategoryMapper {

    // 查询所有物品类别映射，支持分页或过滤条件
    List<ItemCategory> findAll(Map<String, Object> params);

    // 根据物品ID查询单个物品类别映射
    ItemCategory findByAtId(@Param("atId") Integer atId);

    // 插入新的物品类别映射
    int insert(ItemCategory itemCategory);

    // 更新物品类别映射
    int update(ItemCategory itemCategory);

    // 根据ID删除物品类别映射
    int deleteById(@Param("id") Integer id);

    List<Map<String, Object>> findTotalQuantityByCategoryAndEngin(@Param("startDate") String startDate, @Param("endDate") String endDate);

}
