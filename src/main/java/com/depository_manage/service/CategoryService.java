package com.depository_manage.service;

import com.depository_manage.entity.Category;
import com.depository_manage.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public Category getCategoryById(Integer id) {
        return categoryMapper.selectById(id);
    }

    public List<Category> getAllSABCategories() {
        List<Category> flatCategories = categoryMapper.findAllByDepositoryId(1); // 1 represents SAB
        return buildHierarchy(flatCategories);
    }
    public List<Category> getAllZABCategories() {
        List<Category> flatCategories = categoryMapper.findAllByDepositoryId(2); // 2 represents ZAB
        return buildHierarchy(flatCategories);
    }
    public List<Category> getSABCategories() {
        return categoryMapper.findAllByDepositoryId(1);
    }
    public List<Category> getZABCategories() {
        return categoryMapper.findAllByDepositoryId(2);
    }
    public List<Category> getAllCategories() {
        return categoryMapper.selectAll();
    }
    private List<Category> buildHierarchy(List<Category> flatCategories) {
        List<Category> topLevelCategories = flatCategories.stream()
                .filter(c -> c.getParentId() == null)
                .collect(Collectors.toList());

        topLevelCategories.forEach(topLevelCategory -> addChildren(topLevelCategory, flatCategories));

        return topLevelCategories;
    }

    private void addChildren(Category parent, List<Category> flatCategories) {
        List<Category> children = flatCategories.stream()
                .filter(c -> c.getParentId() != null && c.getParentId().equals(parent.getId()))
                .collect(Collectors.toList());

        parent.setChildren(new ArrayList<>(children));

        children.forEach(child -> addChildren(child, flatCategories));
    }

    public int addCategory(Category category) {
        return categoryMapper.insert(category);
    }


    public Integer update(Map<String, Object> map) {
        return categoryMapper.update(map);
    }


    public Integer deleteCategory(Integer id) {
        return categoryMapper.deleteCategory(id);
    }
}