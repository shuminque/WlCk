package com.depository_manage.service;

import com.depository_manage.entity.LineData;
import com.depository_manage.pojo.LineDataP;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LineDataService {
    Integer insertLineData(Map<String, Object> map);
    void batchInsertLineData(List<LineData> lineDataList);
    Integer deleteLineData(int id);

    Integer updateLineData(Map<String, Object> map);

    LineData findLineDataById(Integer id);

    List<LineData> findLineDataByDate(Date date);

    List<LineData> findLineDataByCondition(Map<String, Object> map);

    List<LineData> findLineDataAll();

    Integer findCountByCondition(Map<String, Object> map);

    List<LineDataP> findLineDataPByCondition(Map<String, Object> map);
}
