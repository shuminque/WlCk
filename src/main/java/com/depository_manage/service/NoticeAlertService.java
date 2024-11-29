package com.depository_manage.service;

import com.depository_manage.entity.NoticeAlert;
import java.util.List;
import java.util.Map;

public interface NoticeAlertService {

    // 查询所有预警记录
    List<NoticeAlert> findAll(Map<String, Object> params);

    // 根据物品ID查询预警记录
    NoticeAlert findByAtId(Integer atId);

    // 插入新的预警记录
    int insert(NoticeAlert noticeAlert);

    // 更新预警记录
    int update(NoticeAlert noticeAlert);

    // 删除预警记录
    int deleteById(Integer id);

    Integer  findAlertQuantityByAtId(Integer atId);
}
