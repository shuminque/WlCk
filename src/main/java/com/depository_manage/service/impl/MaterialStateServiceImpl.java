package com.depository_manage.service.impl;

import com.depository_manage.entity.MaterialEngin;
import com.depository_manage.entity.MaterialState;
import com.depository_manage.mapper.MaterialStateMapper;
import com.depository_manage.pojo.MaterialEnginP;
import com.depository_manage.pojo.MaterialStateP;
import com.depository_manage.service.MaterialStateService;
import com.depository_manage.utils.ObjectFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MaterialStateServiceImpl implements MaterialStateService {
    @Autowired
    MaterialStateMapper materialStateMapper;
    @Override
    public Integer insertMaterialState(Map<String,Object> map) {
        return materialStateMapper.insertMaterialState(map);
    }

    @Override
    public List<MaterialState> findMaterialStateAll(){
        return materialStateMapper.findMaterialStateAll();
    }
    @Override
    public Integer findCountByCondition(Map<String, Object> map) {
        return materialStateMapper.findCountByCondition(map);
    }
    @Override
    public List<MaterialStateP> findMaterialStatePByCondition(Map<String, Object> map) {
        Integer size = 10,page=1;
        if (map.containsKey("size")){
            size= ObjectFormatUtil.toInteger(map.get("size"));
            map.put("size", size);
        }
        if (map.containsKey("page")){
            page=ObjectFormatUtil.toInteger(map.get("page"));
            map.put("begin",(page-1)*size);
        }
        List<MaterialState> list=materialStateMapper.findMaterialStateByCondition(map);
        return pack(list);
    }
    @Override
    public Integer deleteMaterialState(int id) {
        return materialStateMapper.deleteMaterialStateById(id);
    }
    @Override
    public Integer updateMaterialState(Map<String, Object> map) {
        // 调用MaterialStateMapper的updateMaterialState方法来更新状态信息
        return materialStateMapper.updateMaterialState(map);
    }
    private List<MaterialStateP> pack(List<MaterialState> list){
        List<MaterialStateP> result=new ArrayList<>(list.size());
        for (MaterialState materialState: list){
            MaterialStateP m=new MaterialStateP(materialState);
            result.add(m);
        }
        return result;
    }

}
