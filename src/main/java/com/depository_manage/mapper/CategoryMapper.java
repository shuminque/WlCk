package com.depository_manage.mapper;

import com.depository_manage.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CategoryMapper {

    Category selectById(Integer id);

    List<Category> findAllByDepositoryId(Integer depositoryId);

    int insert(Category category);

    Integer update(Map<String, Object> map);

    Integer deleteCategory(Integer id);
}
