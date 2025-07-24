package com.depository_manage.service;

import com.depository_manage.entity.Supplier;
import com.depository_manage.pojo.SupplierP;

import java.util.List;
import java.util.Map;

public interface SupplierService {
    Integer insertSupplier(Map<String, Object> map);
    List<Supplier> findSupplierAll();
    Integer findCountByCondition(Map<String, Object> map);
    List<SupplierP> findSupplierPByCondition(Map<String, Object> map);
    Integer deleteSupplier(int id);
    Integer updateSupplier(Map<String, Object> map);
}
