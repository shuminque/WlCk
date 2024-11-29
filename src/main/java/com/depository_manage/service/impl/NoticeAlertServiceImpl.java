package com.depository_manage.service.impl;

import com.depository_manage.entity.NoticeAlert;
import com.depository_manage.mapper.NoticeAlertMapper;
import com.depository_manage.service.NoticeAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NoticeAlertServiceImpl implements NoticeAlertService {

    @Autowired
    private NoticeAlertMapper noticeAlertMapper;

    @Override
    public List<NoticeAlert> findAll(Map<String, Object> params) {
        return noticeAlertMapper.findAll(params);
    }

    @Override
    public NoticeAlert findByAtId(Integer atId) {
        return noticeAlertMapper.findByAtId(atId);
    }

    @Override
    public int insert(NoticeAlert noticeAlert) {
        return noticeAlertMapper.insert(noticeAlert);
    }

    @Override
    public int update(NoticeAlert noticeAlert) {
        return noticeAlertMapper.update(noticeAlert);
    }

    @Override
    public int deleteById(Integer id) {
        return noticeAlertMapper.deleteById(id);
    }

    public Integer findAlertQuantityByAtId(Integer atId) {
        return noticeAlertMapper.findAlertQuantityByAtId(atId);
    }
}
