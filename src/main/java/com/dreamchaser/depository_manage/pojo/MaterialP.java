package com.dreamchaser.depository_manage.pojo;

import com.dreamchaser.depository_manage.entity.Material;
import lombok.Data;

/**
 * 库存类的包装类
 * @author Dreamchaser
 */
@Data
public class MaterialP {
    /** 存储id */
    private Integer id;

    /**AT号*/
    private Integer atId;

    /** 仓库名称 */
    private String depositoryName;

    /** 材料名称 */
    private String mname;

    /**型号/规格*/
    private String model;

    /** 数量 */
    private Double quantity;

    /** 总金额 */
    private Double price;
    private Double unitPrice;

    /** 材料种类名称 */
    private String typeName;

    private String stateName;

    private String enginName;

    public MaterialP(Integer id, Integer atId, Integer depositoryId, String mname, String model, Double quantity, Double price, Double unitPrice, String typeName,
                     String stateName, String enginName) {
        this.id = id;
        this.atId=atId;
        this.mname = mname;
        this.model =model;
        this.quantity = quantity;
        this.price = price;
        this.unitPrice=unitPrice;
        this.typeName = typeName;
        this.stateName =stateName;
        this.enginName =enginName;
    }
    public MaterialP(Material material) {
        this.id = material.getId();
        this.atId=material.getAtId();
        this.mname = material.getMname();
        this.model = material.getModel();
        this.quantity = material.getQuantity();
        this.price = material.getPrice();
        this.unitPrice=material.getUnitPrice();
    }
}
