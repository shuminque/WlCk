package com.depository_manage.mapper;

import com.depository_manage.entity.LineData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
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

    List<LineData> findLineDataByDate(Date date);

    List<LineData> findLineDataByCondition(Map<String, Object> map);

    List<LineData> findLineDataAll();

    Integer findCountByCondition(Map<String, Object> map);

    List<LineData> findLineDataByMonth(@Param("year") int year, @Param("month") int month);
    List<LineData> findLineNameData   (@Param("year") int year, @Param("lineName") String lineName);
    List<LineData> findYearlyProductionData(@Param("year") int year);


    @Update("UPDATE craft SET craft = #{craft} WHERE lineName = (SELECT lineName FROM line_data WHERE id = #{id})")
    void updateCraft(Map<String, Object> map);

    List<String> findDiametersAll();

    List<String> findCategoryTitlesByDiameter(String diameter);

    List<LineData> selectMonthlyLinesByDiameter(@Param("year") int year, @Param("diameter") String diameter);

}
