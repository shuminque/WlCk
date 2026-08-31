package com.depository_manage.pojo;

import lombok.Data;

@Data
public class NoticeAlertImportPreview {
    private int rowNumber;
    private Integer atId;
    private Integer existingAlertId;
    private Integer existingAlertQuantity;
    private Integer incomingAlertQuantity;
    private String action;
    private String message;
}
