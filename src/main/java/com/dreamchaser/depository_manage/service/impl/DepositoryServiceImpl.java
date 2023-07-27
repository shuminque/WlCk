package com.dreamchaser.depository_manage.service.impl;

import com.dreamchaser.depository_manage.entity.Depository;
import com.dreamchaser.depository_manage.entity.Material;
import com.dreamchaser.depository_manage.mapper.DepositoryMapper;
import com.dreamchaser.depository_manage.pojo.DepositoryP;
import com.dreamchaser.depository_manage.pojo.MaterialP;
import com.dreamchaser.depository_manage.service.DepositoryService;
import com.dreamchaser.depository_manage.utils.ObjectFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DepositoryServiceImpl implements DepositoryService {
    @Autowired
    DepositoryMapper depositoryMapper;
    @Override
    public Integer insertDepository(Map<String, Object> map) {
        return depositoryMapper.insertDepository(map);
    }

    @Override
    public List<Depository> findDepositoryAll() {
        return depositoryMapper.findDepositoryAll();
    }

    @Override
    public Integer findCountByCondition(Map<String, Object> map) {
        return depositoryMapper.findCountByCondition(map);
    }

    @Override
    public List<DepositoryP> findDepositoryPByCondition(Map<String, Object> map) {
        Integer size = 10,page=1;
        if (map.containsKey("size")){
            size= ObjectFormatUtil.toInteger(map.get("size"));
            map.put("size", size);
        }
        if (map.containsKey("page")){
            page=ObjectFormatUtil.toInteger(map.get("page"));
            map.put("begin",(page-1)*size);
        }
        List<Depository> list=depositoryMapper.findDepositoryByCondition(map);
        return pack(list);
    }
    private List<DepositoryP> pack(List<Depository> list){
        List<DepositoryP> result=new ArrayList<>(list.size());
        for (Depository depository: list){
            DepositoryP m=new DepositoryP(depository);
            result.add(m);
        }
        return result;
    }


}
