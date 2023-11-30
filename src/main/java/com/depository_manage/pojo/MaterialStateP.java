package com.depository_manage.pojo;

import com.depository_manage.entity.MaterialState;
import lombok.Data;

@Data
public class MaterialStateP {
    private Integer id;
    private String sname;
    public MaterialStateP(Integer id, String sname) {
        this.id=id;
        this.sname=sname;
    }
    public MaterialStateP(MaterialState materialState) {
        this.id = materialState.getId();
        this.sname = materialState.getSname();
    }
}
