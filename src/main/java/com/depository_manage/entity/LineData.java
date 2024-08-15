package com.depository_manage.entity;

import lombok.Data;
import java.util.Date;

@Data
public class LineData {
    private Integer id;
    private Date date;
    private String lineName;
    private Integer production;
}
