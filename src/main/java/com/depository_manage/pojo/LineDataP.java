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

    public LineDataP(Integer id, Date date, String lineName, Integer production){
        this.id = id;
        this.date = date;
        this.lineName = lineName;
        this.production = production;
    }

    public LineDataP(LineData lineData){
        this.id = lineData.getId();
        this.date = lineData.getDate();
        this.lineName = lineData.getLineName();
        this.production = lineData.getProduction();
    }
}
