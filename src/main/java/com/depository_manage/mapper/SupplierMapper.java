package com.depository_manage.mapper;

import com.depository_manage.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface SupplierMapper {
    Integer insertSupplier(Map<String, Object> map);
    List<Supplier> findSupplierAll();
    Integer findCountByCondition(Map<String, Object> map);
    List<Supplier> findSupplierByCondition(Map<String, Object> map);
    Integer deleteSupplierById(int id);
    Integer updateSupplier(Map<String, Object> map);
}
