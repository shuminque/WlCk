package com.depository_manage.mapper;

import com.depository_manage.entity.Series;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
@Mapper
public interface SeriesMapper {
    Integer insertSeries(Map<String, Object> map);
    List<Series> findSeriesAll();
    Integer findCountByCondition(Map<String, Object> map);
    List<Series> findSeriesByCondition(Map<String, Object> map);
    Integer deleteSeriesById(int id);
    Integer updateSeries(Map<String, Object> map);
}
