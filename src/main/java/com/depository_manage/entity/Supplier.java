package com.depository_manage.entity;

import lombok.Data;

@Data
public class Supplier {
    private static final long serialVersionUID = 1L;

    private Integer id;            // 主键
    private String  supplierName;  // 供应商名称
    private String  contact;       // 联系方式
}
