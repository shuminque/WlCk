package com.depository_manage.entity;

import lombok.Data;

@Data
public class NoticeAlert {
    private Integer id;            // 自增ID
    private Integer atId;          // 物品ID
    private Integer alertQuantity; // 预警数量
    private String mname;
    private String model;

}
