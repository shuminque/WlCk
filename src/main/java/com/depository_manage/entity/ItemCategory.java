package com.depository_manage.entity;

import lombok.Data;

@Data
public class ItemCategory {
    private Integer id;            // 自增ID
    private String enginName;
    private String engin;
    private String itemCategory;
    private Integer atId;          // 物品ID

}
