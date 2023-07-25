package com.dreamchaser.depository_manage.entity;

import lombok.Data;

/**
 * 产品信息记录（库存）(material)
 */
@Data
public class Material {
    /** 版本号 */
    private static final long serialVersionUID = 4604245526757565755L;

    /** 存储id */
    private Integer id;

    /** 仓库名称 */
    private Integer depositoryId;

    /** 材料名称 */
    private String mname;

    /** 数量 */
    private Double quantity;

    /** 总金额 */
    private Double price;

    /** 材料种类id */
    private Integer typeId;

    /**材料状态id*/
    private Integer stateId;

    /**工程名称id*/
    private Integer enginId;
}