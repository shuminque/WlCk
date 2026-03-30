package com.depository_manage.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Data
public class OnceFill {
    private Integer id;
    private Integer depositoryId;
    private String name;
    private String model;
    /** 材料种类id */
    private Integer typeId;
    private Double unitPrice;
    private BigDecimal quantity;
    private Double price;
    private Date time;
    private String applyRemark;
    private String introduce;
    private String reviewRemark;
    private String checkRemark;
    private String checkPass;

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity == null ? null : quantity.setScale(2, RoundingMode.HALF_UP);
    }
}
