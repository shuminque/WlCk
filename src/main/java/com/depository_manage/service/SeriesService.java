package com.depository_manage.service;

import com.depository_manage.entity.Series;
import com.depository_manage.pojo.SeriesP;

import java.util.List;
import java.util.Map;

public interface SeriesService {
    Integer insertSeries(Map<String, Object> map);
    List<Series> findSeriesAll();
    Integer findCountByCondition(Map<String, Object> map);
    List<SeriesP> findSeriesPByCondition(Map<String, Object> map);
    Integer deleteSeries(int id);
    Integer updateSeries(Map<String, Object> map);
}
