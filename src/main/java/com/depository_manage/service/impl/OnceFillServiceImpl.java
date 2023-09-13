package com.depository_manage.service.impl;

import com.depository_manage.entity.OnceFill;
import com.depository_manage.mapper.OnceFillMapper;
import com.depository_manage.service.OnceFillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OnceFillServiceImpl implements OnceFillService {

    @Autowired
    private OnceFillMapper onceFillMapper;

    @Override
    public void insertOnceFill(Map<String, Object> map) {
        onceFillMapper.insertOnceFill(map);
    }

    @Override
    public void updateOnceFill(Map<String, Object> map) {
        onceFillMapper.updateOnceFill(map);
    }

    @Override
    public OnceFill findOnceFillById(Integer id) {
        return onceFillMapper.findOnceFillById(id);
    }

    @Override
    public List<OnceFill> findOnceFillByCondition(Map<String, Object> map) {
        return onceFillMapper.findOnceFillByCondition(map);
    }

    @Override
    public void deleteOnceFillById(Integer id) {
        onceFillMapper.deleteOnceFillById(id);
    }

    public void saveAll(List<OnceFill> records) {
        for (OnceFill record : records) {
            record.setPrice(record.getUnitPrice() * record.getQuantity());
        }
        onceFillMapper.insertBatch(records);
    }
}
