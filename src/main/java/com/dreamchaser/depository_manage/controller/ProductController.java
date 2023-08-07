//package com.dreamchaser.depository_manage.controller;
//
//import com.dreamchaser.depository_manage.pojo.RestResponse;
//import com.dreamchaser.depository_manage.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
///**
// */
//@RestController
//public class ProductController {
//    @Autowired
//    ProductService productService;
//    @GetMapping("/product")
//    public RestResponse findProduct(@RequestParam Map<String,Object> map){
//        return new RestResponse(productService.findProductByCondition(map),productService.findCountByCondition(map),200);
//    }
//}
