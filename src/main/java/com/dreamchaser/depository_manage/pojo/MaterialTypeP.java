package com.dreamchaser.depository_manage.pojo;

import com.dreamchaser.depository_manage.entity.Depository;
import com.dreamchaser.depository_manage.entity.MaterialType;
import lombok.Data;

@Data
public class MaterialTypeP {
    private Integer id;

    private String tname;

    private String introduce;

    public MaterialTypeP(Integer id,String tname,String introduce) {
        this.id=id;
        this.tname=tname;
        this.introduce=introduce;
    }
    public MaterialTypeP(MaterialType materialType) {
        this.id = materialType.getId();
        this.tname = materialType.getTname();
        this.introduce =materialType.getIntroduce();
    }
}
