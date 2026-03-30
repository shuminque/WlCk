package com.depository_manage.service.impl;

import com.depository_manage.entity.Material;
import com.depository_manage.entity.OnceFill;
import com.depository_manage.mapper.MaterialTypeMapper;
import com.depository_manage.mapper.OnceFillMapper;
import com.depository_manage.mapper.UserMapper;
import com.depository_manage.pojo.MaterialP;
import com.depository_manage.pojo.OnceFillP;
import com.depository_manage.service.OnceFillService;
import com.depository_manage.utils.ObjectFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class OnceFillServiceImpl implements OnceFillService {

    @Autowired
    private OnceFillMapper onceFillMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    MaterialTypeMapper materialTypeMapper;

    @Override
    public void insertOnceFill(Map<String, Object> map) {
        onceFillMapper.insertOnceFill(map);
    }

    @Override
    public Integer updateOnceFill(Map<String, Object> map) {
        normalizeNumericFields(map);
        return onceFillMapper.updateOnceFill(map);
    }

    @Override
    public OnceFill findOnceFillById(Integer id) {
        return onceFillMapper.findOnceFillById(id);
    }

    @Override
    public List<OnceFillP> findOnceFillPByCondition(Map<String, Object> map) {
        Integer size = 10,page=1;
        if (map.containsKey("size")){
            size=ObjectFormatUtil.toInteger(map.get("size"));
            map.put("size", size);
        }
        if (map.containsKey("page")){
            page=ObjectFormatUtil.toInteger(map.get("page"));
            map.put("begin",(page-1)*size);
        }
        if (map.containsKey("typeId")) {
            Object typeId = map.get("typeId");
            if (!(typeId instanceof Collection)) {
                // 如果typeId不是一个列表或集合，将它转换成一个列表
                map.put("typeId", Collections.singletonList(typeId));
            }
        }
        return pack(onceFillMapper.findOnceFillByCondition(map));
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
    public int batchUpdateReviewRemark(List<Integer> ids, String invoiceNumber) {
        return onceFillMapper.batchUpdateReviewRemark(ids,invoiceNumber);
    }

    @Override
    public void saveAll(List<OnceFill> records, Integer depositoryId) {
        for (OnceFill record : records) {
            if(record.getUnitPrice() == null || record.getQuantity() == null) {
                throw new IllegalArgumentException("Unit price and quantity must not be null");
            }
            record.setDepositoryId(depositoryId);
            // 处理 applyRemark
            String applyRemark = record.getApplyRemark();
            if (applyRemark != null && applyRemark.contains("线")) {
                // 使用正则表达式去除括号及内容
                applyRemark = applyRemark.replaceAll("(线\\([^)]*\\))", "线");
                record.setApplyRemark(applyRemark);
            }
            BigDecimal unitPrice = BigDecimal.valueOf(record.getUnitPrice());
            BigDecimal quantity = BigDecimal.valueOf(record.getQuantity().doubleValue());
            BigDecimal totalPrice = unitPrice.multiply(quantity).setScale(2, RoundingMode.HALF_UP);
            record.setPrice(totalPrice.doubleValue());
        }
        onceFillMapper.insertBatch(records);
    }

    private void normalizeNumericFields(Map<String, Object> map) {
        BigDecimal unitPrice = null;
        BigDecimal quantity = null;

        if (map.containsKey("unitPrice") && map.get("unitPrice") != null) {
            unitPrice = toBigDecimal(map.get("unitPrice"));
            map.put("unitPrice", unitPrice.doubleValue());
        }

        if (map.containsKey("quantity") && map.get("quantity") != null) {
            quantity = toBigDecimal(map.get("quantity")).setScale(2, RoundingMode.HALF_UP);
            map.put("quantity", quantity);
        }

        if (unitPrice != null && quantity != null) {
            BigDecimal totalPrice = unitPrice.multiply(quantity).setScale(2, RoundingMode.HALF_UP);
            map.put("price", totalPrice.doubleValue());
            return;
        }

        if (map.containsKey("price") && map.get("price") != null) {
            BigDecimal price = toBigDecimal(map.get("price")).setScale(2, RoundingMode.HALF_UP);
            map.put("price", price.doubleValue());
        }
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        if (value instanceof String && !((String) value).trim().isEmpty()) {
            return new BigDecimal(((String) value).trim());
        }
        throw new IllegalArgumentException("Invalid numeric value: " + value);
    }

    private List<OnceFillP> pack(List<OnceFill> list){
        List<OnceFillP> result=new ArrayList<>(list.size());
        for (OnceFill onceFill: list){
            OnceFillP m=new OnceFillP(onceFill);
            m.setTypeName(materialTypeMapper.findMaterialTypeNameById(onceFill.getTypeId()));
            result.add(m);
        }
        return result;
    }
}
