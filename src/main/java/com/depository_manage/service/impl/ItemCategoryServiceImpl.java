package com.depository_manage.service.impl;

import com.depository_manage.entity.ItemCategory;
import com.depository_manage.mapper.ItemCategoryMapper;
import com.depository_manage.service.ItemCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ItemCategoryServiceImpl implements ItemCategoryService {

    @Autowired
    private ItemCategoryMapper itemCategoryMapper;

    @Override
    public List<ItemCategory> findAll(Map<String, Object> params) {
        return itemCategoryMapper.findAll(params);
    }

    @Override
    public ItemCategory findByAtId(Integer atId) {
        return itemCategoryMapper.findByAtId(atId);
    }

    @Override
    public int insert(ItemCategory itemCategory) {
        return itemCategoryMapper.insert(itemCategory);
    }

    @Override
    public int update(ItemCategory itemCategory) {
        return itemCategoryMapper.update(itemCategory);
    }

    @Override
    public int deleteById(Integer id) {
        return itemCategoryMapper.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> findTotalQuantityByCategoryAndEngin(Map<String, Object> params) {
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        return itemCategoryMapper.findTotalQuantityByCategoryAndEngin(startDate, endDate);
    }
}
