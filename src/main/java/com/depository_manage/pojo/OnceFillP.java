package com.depository_manage.pojo;

import com.depository_manage.entity.OnceFill;
import lombok.Data;

import java.util.Date;
@Data
public class OnceFillP {
    private Integer id;
    private Integer depositoryId;
    private String name;
    private String model;
    /** 材料种类名称 */
    private String typeName;
    private Double unitPrice;
    private Integer quantity;
    private Double price;
    private Date time;
    private String applyRemark;
    public OnceFillP(Integer id,  String name, String model, Integer quantity, Double price, Double unitPrice,String typeName,
                     Integer depositoryId, String applyRemark, Date time) {
        this.id = id;
        this.name = name;
        this.model =model;
        this.quantity = quantity;
        this.price = price;
        this.unitPrice=unitPrice;
        this.typeName = typeName;
        this.depositoryId = depositoryId;
        this.applyRemark = applyRemark;
        this.time =time;
    }
    public OnceFillP(OnceFill onceFill) {
        this.id = onceFill.getId();
        this.name = onceFill.getName();
        this.model =onceFill.getModel();
        this.quantity = onceFill.getQuantity();
        this.price = onceFill.getPrice();
        this.unitPrice=onceFill.getUnitPrice();
        this.depositoryId = onceFill.getDepositoryId();
        this.applyRemark = onceFill.getApplyRemark();
        this.time =onceFill.getTime();
    }
}
