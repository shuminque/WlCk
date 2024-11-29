package com.depository_manage.mapper;

import com.depository_manage.entity.NoticeAlert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeAlertMapper {

    // 查询所有预警记录，支持分页或过滤条件
    List<NoticeAlert> findAll(Map<String, Object> params);

    // 根据物品ID查询单个预警记录
    NoticeAlert findByAtId(@Param("atId") Integer atId);

    // 插入新的预警记录
    int insert(NoticeAlert noticeAlert);

    // 更新预警记录
    int update(NoticeAlert noticeAlert);

    // 根据ID删除预警记录
    int deleteById(@Param("id") Integer id);

    Integer  findAlertQuantityByAtId(@Param("atId") Integer atId);

}
