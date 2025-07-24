package com.depository_manage.pojo;

import com.depository_manage.entity.Supplier;
import lombok.Data;

@Data
public class SupplierP {
    private Integer id;
    private String supplierName;
    private String contact;

    public SupplierP(Integer id, String supplierName, String contact) {
        this.id = id;
        this.supplierName = supplierName;
        this.contact = contact;
    }

    public SupplierP(Supplier supplier) {
        this.id = supplier.getId();
        this.supplierName = supplier.getSupplierName();
        this.contact = supplier.getContact();
    }
}
