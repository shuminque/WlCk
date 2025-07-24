package com.depository_manage.service.impl;

import com.depository_manage.entity.Supplier;
import com.depository_manage.mapper.SupplierMapper;
import com.depository_manage.pojo.SupplierP;
import com.depository_manage.service.SupplierService;
import com.depository_manage.utils.ObjectFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public Integer insertSupplier(Map<String, Object> map) {
        return supplierMapper.insertSupplier(map);
    }

    @Override
    public List<Supplier> findSupplierAll() {
        return supplierMapper.findSupplierAll();
    }

    @Override
    public Integer findCountByCondition(Map<String, Object> map) {
        return supplierMapper.findCountByCondition(map);
    }

    @Override
    public List<SupplierP> findSupplierPByCondition(Map<String, Object> map) {
        Integer size = 10, page = 1;
        if (map.containsKey("size")) {
            size = ObjectFormatUtil.toInteger(map.get("size"));
            map.put("size", size);
        }
        if (map.containsKey("page")) {
            page = ObjectFormatUtil.toInteger(map.get("page"));
            map.put("begin", (page - 1) * size);
        }

        List<Supplier> list = supplierMapper.findSupplierByCondition(map);
        return pack(list);
    }

    @Override
    public Integer deleteSupplier(int id) {
        return supplierMapper.deleteSupplierById(id);
    }

    @Override
    public Integer updateSupplier(Map<String, Object> map) {
        return supplierMapper.updateSupplier(map);
    }

    private List<SupplierP> pack(List<Supplier> list) {
        List<SupplierP> result = new ArrayList<>(list.size());
        for (Supplier supplier : list) {
            result.add(new SupplierP(supplier));
        }
        return result;
    }
}
