package com.depository_manage.entity;

import lombok.Data;

@Data
public class Series {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String seriesName;
    private String diameter;
}
