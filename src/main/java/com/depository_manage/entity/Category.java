package com.depository_manage.entity;

import lombok.Data;

@Data
public class Category {
    private Integer id;
    private Integer depositoryId;
    private Integer parentId;
    private String title;


}
