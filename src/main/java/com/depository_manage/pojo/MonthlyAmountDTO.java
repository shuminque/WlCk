package com.depository_manage.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyAmountDTO {

    private String month; // 例如: "2023-01"
    // SAB
    private BigDecimal sabInboundAmount;
    private BigDecimal sabOutboundAmount;
    private BigDecimal sabStockAmount;
    // ZAB
    private BigDecimal zabInboundAmount;
    private BigDecimal zabOutboundAmount;
    private BigDecimal zabStockAmount;

    @Override
    public String toString() {
        return "MonthlyAmountDTO{" +
                "month='" + month + '\'' +
                ", sabInboundAmount=" + sabInboundAmount +
                ", sabOutboundAmount=" + sabOutboundAmount +
                ", sabStockAmount=" + sabStockAmount +
                ", zabInboundAmount=" + zabInboundAmount +
                ", zabOutboundAmount=" + zabOutboundAmount +
                ", zabStockAmount=" + zabStockAmount +
                '}';
    }
}
