package com.dreamchaser.depository_manage.entity;

import lombok.Data;

@Data
public class Product {
    /**AT号*/
    private Integer atNumber;
    /**品名*/
    private String name;
    /**型号/规格*/
    private String model;
    /*类别编号*/
    private Integer category_Id;

    private String category_name;
    /**图号*/
    private Integer figure_Number;
    /**工程编号*/
    private Integer engin_Id;
    private String engin_name;
    /**状态编号*/
    private Integer state_id;
    private String state_name;

}
