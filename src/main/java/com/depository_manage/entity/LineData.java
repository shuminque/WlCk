package com.depository_manage.entity;

import lombok.Data;
import java.util.Date;

@Data
public class LineData {
    private Integer id;
    private Date date;
    private String lineName;
    private Integer production;
    private String model; // 新增的 model 字段
    private String craft; // 新增的 model 字段
}
