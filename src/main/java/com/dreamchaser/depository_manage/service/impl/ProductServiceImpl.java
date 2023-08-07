//package com.dreamchaser.depository_manage.service.impl;
//
//import com.dreamchaser.depository_manage.entity.Material;
//import com.dreamchaser.depository_manage.entity.Product;
//import com.dreamchaser.depository_manage.mapper.DepositoryMapper;
//import com.dreamchaser.depository_manage.mapper.MaterialTypeMapper;
//import com.dreamchaser.depository_manage.mapper.ProductMapper;
//import com.dreamchaser.depository_manage.pojo.ProductP;
//import com.dreamchaser.depository_manage.service.ProductService;
//import com.dreamchaser.depository_manage.utils.ObjectFormatUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author Dreamchaser
// */
//@Service
//public class ProductServiceImpl implements ProductService {
//    @Autowired
//    ProductMapper productMapper;
//    @Autowired
//    DepositoryMapper depositoryMapper;
//    @Autowired
//    MaterialTypeMapper materialTypeMapper;
//    @Override
//    public Integer insertProduct(Map<String, Object> map) {
//        return productMapper.insertProduct(map);
//    }
//
//    @Override
//    public Integer updateProduct(Map<String, Object> map) {
//        return productMapper.updateProduct(map);
//    }
//
//    @Override
//    public Integer updateProduct(Product product) {
//        return null;
//    }
//
//    @Override
//    public Integer changeProduct(Map<String, Object> map) {
//        return null;
//    }
//
//    @Override
//    public Integer deleteProductById(int id) {
//        return productMapper.deleteProductById(id);
//    }
//
//    @Override
//    public List<ProductP> findProductByCondition(Map<String, Object> map) {
//        Integer size = 10,page=1;
//        if (map.containsKey("size")){
//            size= ObjectFormatUtil.toInteger(map.get("size"));
//            map.put("size", size);
//        }
//        if (map.containsKey("page")){
//            page=ObjectFormatUtil.toInteger(map.get("page"));
//            map.put("begin",(page-1)*size);
//        }
//        List<Product> list=productMapper.findProductByCondition(map);
//        return pack(list);
//    }
//
//    @Override
//    public List<Product> findProductAll() {
//        return productMapper.findProductAll();
//    }
//
//    @Override
//    public Product findProductById(int atNumber) {
//        return productMapper.findProductById(atNumber);
//    }
//
//    @Override
//    public Product findProductByIds(List<Integer> ids) {
//        return productMapper.findProductByIds(ids);
//    }
//
//    @Override
//    public Integer findCount() {
//        return productMapper.findCount();
//    }
//
//    @Override
//    public Integer findCountByCondition(Map<String,Object> map) {
//        return productMapper.findCountByCondition(map);
//    }
//
//
//    /**
//     * 对查出来的记录进行包装，包装成前端需要的数据
//     * @param list DepositoryRecord集合
//     * @return 包装好的集合
//     */
//    private List<ProductP> pack(List<Product> list){
//        List<ProductP> result=new ArrayList<>(list.size());
//        for (Product product: list){
//            ProductP m=new ProductP(product);
////            m.setDepositoryName(depositoryMapper.findDepositoryNameById(product.getDepositoryId()));
////            m.setTypeName(materialTypeMapper.findMaterialTypeNameById(product.getTypeId()));
//
//            result.add(m);
//        }
//        return result;
//    }
//}
