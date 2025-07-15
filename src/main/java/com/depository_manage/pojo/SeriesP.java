package com.depository_manage.pojo;

import com.depository_manage.entity.Series;
import lombok.Data;

@Data
public class SeriesP {
    private Integer id;

    private String seriesName;

    private String diameter;

    public SeriesP(Integer id, String seriesName, String diameter) {
        this.id = id;
        this.seriesName = seriesName;
        this.diameter = diameter;
    }

    public SeriesP(Series series) {
        this.id = series.getId();
        this.seriesName = series.getSeriesName();
        this.diameter = series.getDiameter();
    }
}
