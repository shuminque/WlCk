package com.depository_manage.pojo;

import com.depository_manage.entity.OnceFill;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
@Data
public class OnceFillP {
    private Integer id;
    private Integer depositoryId;
    private String name;
    private String model;
    /** 材料种类名称 */
    private Integer typeId;
    private String typeName;
    private Double unitPrice;
    private BigDecimal quantity;
    private Double price;
    private Date time;
    private String applyRemark;
    private String introduce;
    private String reviewRemark;
    private String checkRemark;
    private String checkPass;
    public OnceFillP(Integer id,  String name, String model, BigDecimal quantity, Double price, Double unitPrice,Integer typeId, String typeName,
                     Integer depositoryId, String applyRemark, Date time, String introduce
            , String reviewRemark, String checkRemark, String checkPass) {
        this.id = id;
        this.name = name;
        this.model =model;
        this.setQuantity(quantity);
        this.price = price;
        this.unitPrice=unitPrice;
        this.typeId = typeId;
        this.typeName = typeName;
        this.depositoryId = depositoryId;
        this.applyRemark = applyRemark;
        this.time =time;
        this.introduce = introduce;
        this.reviewRemark = reviewRemark;
        this.checkRemark = checkRemark;
        this.checkPass = checkPass;
    }
    public OnceFillP(OnceFill onceFill) {
        this.id = onceFill.getId();
        this.name = onceFill.getName();
        this.typeId = onceFill.getTypeId();
        this.model =onceFill.getModel();
        this.setQuantity(onceFill.getQuantity());
        this.price = onceFill.getPrice();
        this.unitPrice=onceFill.getUnitPrice();
        this.depositoryId = onceFill.getDepositoryId();
        this.applyRemark = onceFill.getApplyRemark();
        this.time =onceFill.getTime();
        this.introduce = onceFill.getIntroduce();
        this.reviewRemark = onceFill.getReviewRemark();
        this.checkRemark = onceFill.getCheckRemark();
        this.checkPass = onceFill.getCheckPass();
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity == null ? null : quantity.setScale(2, RoundingMode.HALF_UP);
    }
}
