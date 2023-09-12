package com.depository_manage.mapper;

import com.depository_manage.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    Category selectById(Integer id);

    List<Category> findAllByDepositoryId(Integer depositoryId);

    void insert(Category category);

    void update(Category category);

    Integer deleteCategory(Integer id);
}
