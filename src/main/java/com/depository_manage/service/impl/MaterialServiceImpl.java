package com.depository_manage.service.impl;

import com.depository_manage.entity.Material;
import com.depository_manage.mapper.*;
import com.depository_manage.mapper.*;
import com.depository_manage.pojo.MaterialP;
import com.depository_manage.service.MaterialService;
import com.depository_manage.utils.ObjectFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MaterialServiceImpl implements MaterialService {
    @Autowired
    MaterialMapper materialMapper;
    @Autowired
    DepositoryMapper depositoryMapper;
    @Autowired
    MaterialTypeMapper materialTypeMapper;
    @Autowired
    MaterialStateMapper materialStateMapper;
    @Autowired
    MaterialEnginMapper materialEnginMapper;

    @Override
    public Integer insertMaterial(Map<String, Object> map) {
        return materialMapper.insertMaterial(map);
    }

    @Override
    public Integer updateMaterial(int id, Map<String, Object> map) {
        return materialMapper.updateMaterial(id, map);
    }


    @Override
    public Integer deleteMaterialById(int id) {
        return materialMapper.deleteMaterialById(id);
    }

    @Override
    public List<MaterialP> findMaterialPByCondition(Map<String, Object> map) {
        Integer size = 10,page=1;
        if (map.containsKey("size")){
            size= ObjectFormatUtil.toInteger(map.get("size"));
            map.put("size", size);
        }
        if (map.containsKey("page")){
            page=ObjectFormatUtil.toInteger(map.get("page"));
            map.put("begin",(page-1)*size);
        }
        List<Material> list=materialMapper.findMaterialByCondition(map);
        return pack(list);
    }

    @Override
    public List<Material> findMaterialAll() {
        return materialMapper.findMaterialAll();
    }

    @Override
    public Material findMaterialById(int id) {
        return materialMapper.findMaterialById(id);
    }

    @Override
    public Material findMaterialByIds(List<Integer> ids) {
        return materialMapper.findMaterialByIds(ids);
    }

    @Override
    public Integer findCount() {
        return materialMapper.findCount();
    }

    @Override
    public Integer findCountByCondition(Map<String,Object> map) {
        return materialMapper.findCountByCondition(map);
    }




    /**
     * 对查出来的记录进行包装，包装成前端需要的数据
     * @param list DepositoryRecord集合
     * @return 包装好的集合
     */
    private List<MaterialP> pack(List<Material> list){
        List<MaterialP> result=new ArrayList<>(list.size());
        for (Material material: list){
            MaterialP m=new MaterialP(material);
            m.setDepositoryName(depositoryMapper.findDepositoryNameById(material.getDepositoryId()));
            m.setTypeName(materialTypeMapper.findMaterialTypeNameById(material.getTypeId()));
            m.setStateName(materialStateMapper.findMaterialStateNameById(material.getStateId()));
            m.setEnginName(materialEnginMapper.findMaterialEnginNameById(material.getEnginId()));
            result.add(m);
        }
        return result;
    }
    @Override
    public BigDecimal findSABpriceSum() {
        return materialMapper.findSABpriceSum();
    }
    @Override
    public BigDecimal findZABpriceSum() {
        return materialMapper.findZABpriceSum();
    }
    @Override
    public Integer findSABcountSum() {
        return materialMapper.findSABcountSum();
    }
    @Override
    public Integer findZABcountSum() {
        return materialMapper.findZABcountSum();
    }
}
