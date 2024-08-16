package com.depository_manage.mapper;

import com.depository_manage.entity.LineData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface LineDataMapper {

    Integer insertLineData(Map<String, Object> map);

    void batchInsert(List<LineData> lineDataList);

    Integer deleteLineDataById(int id);

    Integer updateLineData(Map<String, Object> map);

    LineData findLineDataById(Integer id);

    // 以下方法可以根据实际需求添加或删除
    List<LineData> findLineDataByDate(Date date);

    List<LineData> findLineDataByCondition(Map<String, Object> map);

    List<LineData> findLineDataAll();

    Integer findCountByCondition(Map<String, Object> map);

    List<LineData> findLineDataByMonth(@Param("year") int year, @Param("month") int month);
}
