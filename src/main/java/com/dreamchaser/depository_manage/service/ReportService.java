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

    public List<Map<String, Object>> fetchReportData(int year, int month, int depositoryId) {
        String sql =
                "SELECT\n" +
                        "    m.at_id,\n" +
                        "    m.mname,\n" +
                        "    COALESCE(LEFT(m.model, 30), 'N/A') AS model,\n" +
                        "    SUM(CASE WHEN dr.type = 1 THEN dr.quantity ELSE 0 END) AS 入库数量,\n" +
                        "    FORMAT(ROUND((SUM(CASE WHEN dr.type = 1 THEN dr.price * dr.quantity ELSE 0 END)),2), 2) AS 入库金额,\n" +
                        "    SUM(CASE WHEN dr.type = 0 THEN dr.quantity ELSE 0 END) AS 出库数量,\n" +
                        "    FORMAT(ROUND((SUM(CASE WHEN dr.type = 0 THEN dr.price * dr.quantity ELSE 0 END)),2), 2) AS 出库金额,\n" +
                        "    m.quantity AS 存储数量,\n" +
                        "    FORMAT(ROUND(m.price,2), 2) AS 总金额\n" +
                        "FROM\n" +
                        "    material m\n" +
                        "LEFT JOIN\n" +
                        "    depository_record dr\n" +
                        "    ON m.mname = dr.mname\n" +
                        "    AND (m.model IS NULL OR m.model = dr.model)\n" +
                        "    AND m.depository_id = dr.depository_id\n" +
                        "    AND dr.review_pass = 1\n" +
                        "    AND YEAR(dr.review_time) = ?\n" +
                        "    AND MONTH(dr.review_time) = ?\n" +
                        "WHERE m.depository_id = ?\n" +
                        "GROUP BY\n" +
                        "    m.at_id, m.mname, m.model, m.quantity, m.price;";

        return jdbcTemplate.queryForList(sql, year, month, depositoryId);
    }

    public List<Map<String, Object>> everyTypeData(int year, int month, int depositoryId) {
        String sql =
                "WITH CombinedData AS (\n" +
                        "    SELECT\n" +
                        "        mt.id AS type_id,\n" +
                        "        CASE WHEN mt.tname LIKE '%(进口)' THEN 1 ELSE 0 END AS is_import,\n" +
                        "        mt.tname AS material_type_name,\n" +
                        "        COALESCE(SUM(CASE WHEN dr.type = 1 THEN dr.price * dr.quantity ELSE 0 END), 0) AS raw_in_money,\n" +
                        "        COALESCE(SUM(CASE WHEN dr.type = 0 THEN dr.price * dr.quantity ELSE 0 END), 0) AS raw_out_money,\n" +
                        "        FORMAT(COALESCE(SUM(CASE WHEN dr.type = 1 THEN dr.price * dr.quantity ELSE 0 END), 0), 2) AS 入库金额,\n" +
                        "        FORMAT(COALESCE(SUM(CASE WHEN dr.type = 0 THEN dr.price * dr.quantity ELSE 0 END), 0), 2) AS 出库金额,\n" +
                        "        IFNULL(YEAR(dr.review_time), ?) AS report_year,\n" +
                        "        IFNULL(MONTH(dr.review_time), ?) AS report_month\n" +
                        "    FROM material_type mt\n" +
                        "    LEFT JOIN depository_record dr ON mt.tname = dr.type_name\n" +
                        "                               AND MONTH(dr.review_time) = ?\n" +
                        "                               AND YEAR(dr.review_time) = ?\n" +
                        "                               AND dr.review_pass = 1\n" +
                        "                               AND dr.depository_id = ?\n" +
                        "    GROUP BY mt.tname, mt.id, report_year, report_month\n" +
                        "),\n" +
                        "StockValue AS (\n" +
                        "    SELECT\n" +
                        "        mt.id AS type_id,\n" +
                        "        SUM(m.price) AS raw_stock_money,\n" +
                        "        FORMAT(SUM(m.price), 2) AS 在库金额,\n" +
                        "        CASE WHEN mt.tname LIKE '%(进口)' THEN 1 ELSE 0 END AS is_import\n" +
                        "    FROM material_type mt\n" +
                        "    LEFT JOIN material m ON mt.id = m.type_id\n" +
                        "    WHERE m.depository_id = ?\n" +
                        "    GROUP BY mt.id\n" +
                        "),\n" +
                        "FinalData AS (\n" +
                        "    SELECT\n" +
                        "        CONCAT(cd.report_year, '/', cd.report_month) AS 日期,\n" +
                        "        cd.material_type_name AS 材料类型,\n" +
                        "        cd.入库金额,\n" +
                        "        cd.出库金额,\n" +
                        "        COALESCE(s.在库金额, 0) AS 在库金额,\n" +
                        "        cd.is_import,\n" +
                        "        cd.type_id\n" +
                        "    FROM CombinedData cd\n" +
                        "    LEFT JOIN StockValue s ON cd.type_id = s.type_id\n" +
                        "),\n" +
                        "Totals AS (\n" +
                        "    SELECT\n" +
                        "        '总计' AS 日期,\n" +
                        "        ' ' AS 材料类型,\n" +
                        "        FORMAT(SUM(cd.raw_in_money), 2) AS 入库金额,\n" +
                        "        FORMAT(SUM(cd.raw_out_money), 2) AS 出库金额,\n" +
                        "        FORMAT(SUM(COALESCE(s.raw_stock_money, 0)), 2) AS 在库金额,\n" +
                        "        cd.is_import,\n" +
                        "        9999 AS type_id\n" +
                        "    FROM CombinedData cd\n" +
                        "    LEFT JOIN StockValue s ON cd.type_id = s.type_id\n" +
                        "    GROUP BY cd.is_import\n" +
                        ")\n" +
                        "SELECT 日期, 材料类型, 入库金额, 出库金额, 在库金额\n" +
                        "FROM (\n" +
                        "    SELECT 日期, 材料类型, 入库金额, 出库金额, 在库金额, is_import, type_id\n" +
                        "    FROM FinalData\n" +
                        "    UNION ALL\n" +
                        "    SELECT 日期, 材料类型, 入库金额, 出库金额, 在库金额, is_import, type_id\n" +
                        "    FROM Totals\n" +
                        ") AS SubQuery\n" +
                        "ORDER BY is_import DESC, type_id ASC, 日期 DESC;";
        return jdbcTemplate.queryForList(sql, year, month, month, year, depositoryId, depositoryId);
    }

    public List<Map<String, Object>> transferData(int year, int month) {
        String sql = "SELECT\n" +
                "    DATE_FORMAT(o.apply_time, '%d/%m/%Y') AS 日期,\n" +
                "    o.mname AS 品名,\n" +
                "    o.type_name AS 型号,\n" +
                "    FORMAT(o.price, 2) AS 单价,\n" +
                "    o.quantity AS 数量,\n" +
                "    FORMAT(ROUND(o.price * o.quantity, 2), 2) AS 总价,\n" +
                "    o.apply_remark AS 备注\n" +
                "FROM\n" +
                "    depository_record AS o -- 出库记录\n" +
                "JOIN\n" +
                "    depository_record AS i -- 入库记录\n" +
                "ON\n" +
                "    o.mname = i.mname\n" +
                "    AND o.type = 0 AND i.type = 1\n" +
                "    AND o.quantity = i.quantity\n" +
                "    AND o.price = i.price\n" +
                "    AND o.applicant_id = i.applicant_id\n" +
                "WHERE\n" +
                "    (YEAR(o.apply_time) = ? AND MONTH(o.apply_time) = ?)\n" +
                "    AND (\n" +
                "        (o.apply_remark = 'SAB转入ZAB' OR i.apply_remark = 'SAB转入ZAB')\n" +
                "        OR (o.apply_remark = 'ZAB转入SAB' OR i.apply_remark = 'ZAB转入SAB')\n" +
                "    )\n" +
                "ORDER BY\n" +
                "    FIELD(o.apply_remark, 'SAB转入ZAB', 'ZAB转入SAB'),\n" +
                "    o.apply_time DESC;";
        return jdbcTemplate.queryForList(sql, year, month);
    }
}

