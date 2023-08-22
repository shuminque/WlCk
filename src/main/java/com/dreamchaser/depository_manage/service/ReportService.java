package com.dreamchaser.depository_manage.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> fetchReportData(int depositoryId) {
        String sql = "SELECT m.at_id, m.mname, COALESCE(LEFT(m.model, 30), 'N/A') AS model," +
                "SUM(CASE WHEN dr.type = 1 THEN dr.quantity ELSE 0 END) AS 入库数量," +
                "SUM(CASE WHEN dr.type = 1 THEN dr.price * dr.quantity ELSE 0 END) AS 入库金额," +
                "SUM(CASE WHEN dr.type = 0 THEN dr.quantity ELSE 0 END) AS 出库数量," +
                "SUM(CASE WHEN dr.type = 0 THEN dr.price * dr.quantity ELSE 0 END) AS 出库金额," +
                "m.quantity AS 存储数量," +
                "m.price AS 总金额 " +
                "FROM material m " +
                "LEFT JOIN depository_record dr ON " +
                "CASE WHEN m.model IS NULL THEN m.mname = dr.mname " +
                "ELSE m.mname = dr.mname AND m.model = dr.model END " +
                "AND m.depository_id = dr.depository_id " +
                "AND dr.review_pass = 1 " +
                "AND YEAR(dr.apply_time) = 2023 " +
                "AND MONTH(dr.apply_time) = 8 " +
                "WHERE m.depository_id = ? " +
                "GROUP BY m.at_id, m.mname, m.model, m.quantity, m.price";

        return jdbcTemplate.queryForList(sql,depositoryId);
    }
}

