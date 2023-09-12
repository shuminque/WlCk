package com.depository_manage.service;

import com.depository_manage.entity.Category;
import com.depository_manage.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public Category getCategoryById(Integer id) {
        return categoryMapper.selectById(id);
    }

    public List<Category> getAllSABCategories() {
        return categoryMapper.findAllByDepositoryId(1); // 1 represents SAB
    }

    public List<Category> getAllZABCategories() {
        return categoryMapper.findAllByDepositoryId(2); // 2 represents ZAB
    }

    public void addCategory(Category category) {
        categoryMapper.insert(category);
    }

    public void updateCategory(Category category) {
        categoryMapper.update(category);
    }

    public Integer deleteCategory(Integer id) {
        return categoryMapper.deleteCategory(id);
    }
}
