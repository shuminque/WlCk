package com.depository_manage.service.impl;

import com.depository_manage.entity.Material;
import com.depository_manage.entity.OnceFill;
import com.depository_manage.mapper.OnceFillMapper;
import com.depository_manage.mapper.UserMapper;
import com.depository_manage.pojo.MaterialP;
import com.depository_manage.pojo.OnceFillP;
import com.depository_manage.service.OnceFillService;
import com.depository_manage.utils.ObjectFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OnceFillServiceImpl implements OnceFillService {

    @Autowired
    private OnceFillMapper onceFillMapper;
    @Autowired
    private UserMapper userMapper;

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
    public List<OnceFillP> findOnceFillPByCondition(Map<String, Object> map) {
        List<OnceFill> list=onceFillMapper.findOnceFillByCondition(map);
        return pack(list);
    }
    @Override
    public void deleteOnceFillById(Integer id) {
        onceFillMapper.deleteOnceFillById(id);
    }
    @Override
    public Integer findCountByCondition(Map<String,Object> map) {
        return onceFillMapper.findCountByCondition(map);
    }
    @Override
    public void saveAll(List<OnceFill> records, Integer depositoryId) {
        for (OnceFill record : records) {
            record.setDepositoryId(depositoryId);
            record.setPrice(record.getUnitPrice() * record.getQuantity());
        }
        onceFillMapper.insertBatch(records);
    }

    private List<OnceFillP> pack(List<OnceFill> list){
        List<OnceFillP> result=new ArrayList<>(list.size());
        for (OnceFill onceFill: list){
            OnceFillP m=new OnceFillP(onceFill);
            result.add(m);
        }
        return result;
    }
}
