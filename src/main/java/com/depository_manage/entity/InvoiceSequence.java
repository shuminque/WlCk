package com.depository_manage.entity;

import lombok.Data;

import java.util.Date;

@Data
public class InvoiceSequence {
    private Long id;
    private String invoiceMonth;      // 格式为 yyyyMM
    private Integer currentSequence;
    private Date createTime;
    private Date updateTime;
}
