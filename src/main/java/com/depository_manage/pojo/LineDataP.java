package com.depository_manage.pojo;

import com.depository_manage.entity.LineData;
import lombok.Data;
import java.util.Date;

@Data
public class LineDataP {
    private Integer id;
    private Date date;
    private String lineName;
    private Integer production;
    private String model; // 新增的 model 字段

    // 更新后的构造函数
    public LineDataP(Integer id, Date date, String lineName, Integer production, String model) {
        this.id = id;
        this.date = date;
        this.lineName = lineName;
        this.production = production;
        this.model = model;
    }

    // 更新基于 LineData 对象的构造函数
    public LineDataP(LineData lineData) {
        this.id = lineData.getId();
        this.date = lineData.getDate();
        this.lineName = lineData.getLineName();
        this.production = lineData.getProduction();
        this.model = lineData.getModel(); // 从 LineData 对象中获取 model 字段
    }
}
