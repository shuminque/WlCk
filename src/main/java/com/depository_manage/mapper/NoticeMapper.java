package com.depository_manage.mapper;

import com.depository_manage.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface NoticeMapper {

    /**
     * 增加一条公告信息
     * @param map 参数map
     * @return 受影响的行数
     */
    Integer addNotice(Map<String,Object> map);

    /**
     * 根据条件查询符合条件的公告信息
     * @param map 参数map
     * @return 符合条件的公告列表
     */
    List<Notice> findNoticeByCondition(Map<String,Object> map);

    Integer deleteNoticeById(Integer id);

    /**
     * 根据 atId 和 仓库 id 查询通知（判断是否已存在）
     * @param map 包含 atId 和 depositoryId
     * @return 通知列表（一般是 0 或 1 条）
     */
    List<Notice> findNoticeByAtIdAndDepository(Map<String, Object> map);

}
