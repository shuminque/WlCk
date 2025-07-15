package com.depository_manage.service.impl;

import com.depository_manage.entity.Series;
import com.depository_manage.mapper.SeriesMapper;
import com.depository_manage.pojo.SeriesP;
import com.depository_manage.service.SeriesService;
import com.depository_manage.utils.ObjectFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SeriesServiceImpl implements SeriesService {

    @Autowired
    private SeriesMapper seriesMapper;

    @Override
    public Integer insertSeries(Map<String, Object> map) {
        return seriesMapper.insertSeries(map);
    }

    @Override
    public List<Series> findSeriesAll() {
        return seriesMapper.findSeriesAll();
    }

    @Override
    public Integer findCountByCondition(Map<String, Object> map) {
        return seriesMapper.findCountByCondition(map);
    }

    @Override
    public List<SeriesP> findSeriesPByCondition(Map<String, Object> map) {
        Integer size = 10, page = 1;
        if (map.containsKey("size")) {
            size = ObjectFormatUtil.toInteger(map.get("size"));
            map.put("size", size);
        }
        if (map.containsKey("page")) {
            page = ObjectFormatUtil.toInteger(map.get("page"));
            map.put("begin", (page - 1) * size);
        }

        List<Series> list = seriesMapper.findSeriesByCondition(map);
        return pack(list);
    }

    @Override
    public Integer deleteSeries(int id) {
        return seriesMapper.deleteSeriesById(id);
    }

    @Override
    public Integer updateSeries(Map<String, Object> map) {
        return seriesMapper.updateSeries(map);
    }

    private List<SeriesP> pack(List<Series> list) {
        List<SeriesP> result = new ArrayList<>(list.size());
        for (Series series : list) {
            result.add(new SeriesP(series));
        }
        return result;
    }
}
