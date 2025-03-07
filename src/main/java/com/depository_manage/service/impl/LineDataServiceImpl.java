package com.depository_manage.service.impl;

import com.depository_manage.entity.LineData;
import com.depository_manage.mapper.LineDataMapper;
import com.depository_manage.pojo.LineDataP;
import com.depository_manage.service.LineDataService;
import com.depository_manage.utils.ObjectFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class LineDataServiceImpl implements LineDataService {
    @Autowired
    LineDataMapper lineDataMapper;

    @Override
    public Integer insertLineData(Map<String, Object> map) {
        return lineDataMapper.insertLineData(map);
    }

    @Override
    public void batchInsertLineData(List<LineData> lineDataList) {
        lineDataMapper.batchInsert(lineDataList);
    }

    @Override
    public Integer deleteLineData(int id) {
        return lineDataMapper.deleteLineDataById(id);
    }

    @Override
    @Transactional  // 开启事务，确保两个更新要么都成功，要么都回滚
    public Integer updateLineData(Map<String, Object> map) {
        // 更新 line_data 表
        int updatedRows = lineDataMapper.updateLineData(map);

        // 检查 craft 是否需要更新
        if (map.get("craft") != null) {
            lineDataMapper.updateCraft(map);
        }

        return updatedRows;
    }


    @Override
    public LineData findLineDataById(Integer id) {
        return lineDataMapper.findLineDataById(id);
    }

    @Override
    public List<LineData> findLineDataByDate(Date date) {
        return lineDataMapper.findLineDataByDate(date);
    }

    @Override
    public List<LineData> findLineDataByCondition(Map<String, Object> map) {
        return lineDataMapper.findLineDataByCondition(map);
    }

    @Override
    public List<LineData> findLineDataAll() {
        return lineDataMapper.findLineDataAll();
    }

    @Override
    public Integer findCountByCondition(Map<String, Object> map) {
        return lineDataMapper.findCountByCondition(map);
    }

    @Override
    public List<LineDataP> findLineDataPByCondition(Map<String, Object> map) {
        Integer size = 10, page = 1;
        if (map.containsKey("size")) {
            size = ObjectFormatUtil.toInteger(map.get("size"));
            map.put("size", size);
        }
        if (map.containsKey("page")) {
            page = ObjectFormatUtil.toInteger(map.get("page"));
            map.put("begin", (page - 1) * size);
        }
        List<LineData> list = lineDataMapper.findLineDataByCondition(map);
        return pack(list);
    }

    @Override
    public List<LineData> findLineDataByMonth(int year, int month) {
        return lineDataMapper.findLineDataByMonth(year, month);
    }

    @Override
    public List<LineData> findLineNameData(int year, String lineName) {
        return lineDataMapper.findLineNameData(year, lineName);

    }

    @Override
    public List<LineData> findYearlyProductionData(int year) {
        return lineDataMapper.findYearlyProductionData(year);
    }

    private List<LineDataP> pack(List<LineData> list) {
        List<LineDataP> result = new ArrayList<>(list.size());
        for (LineData lineData : list) {
            LineDataP m = new LineDataP(lineData);
            result.add(m);
        }
        return result;
    }
}
