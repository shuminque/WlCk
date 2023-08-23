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
                "AND YEAR(dr.review_time) = ? " +
                "AND MONTH(dr.review_time) = ? " +
                "WHERE m.depository_id = ? " +
                "GROUP BY m.at_id, m.mname, m.model, m.quantity, m.price";
        return jdbcTemplate.queryForList(sql, year, month, depositoryId);
    }

    public List<Map<String, Object>> everyTypeData(int year, int month, int depositoryId) {
        String sql =
                "WITH CombinedData AS (\n" +
                        "    SELECT\n" +
                        "        mt.id AS type_id,\n" +
                        "        CASE WHEN mt.tname LIKE '%(进口)' THEN 1 ELSE 0 END AS is_import,\n" +
                        "        mt.tname AS material_type_name,\n" +
                        "        COALESCE(SUM(CASE WHEN dr.type = 1 THEN dr.price * dr.quantity ELSE 0 END), 0) AS 入库金额,\n" +
                        "        COALESCE(SUM(CASE WHEN dr.type = 0 THEN dr.price * dr.quantity ELSE 0 END), 0) AS 出库金额,\n" +
                        "        IFNULL(YEAR(dr.review_time), ?) AS report_year,\n" +
                        "        IFNULL(MONTH(dr.review_time), ?) AS report_month\n" +
                        "    FROM material_type mt\n" +
                        "    LEFT JOIN depository_record dr ON mt.tname = dr.type_name\n" +
                        "                               AND MONTH(dr.review_time) = ?" +
                        "                               AND YEAR(dr.review_time) = ?" +
                        "                               AND dr.review_pass = 1" +
                        "                               AND dr.depository_id = ?" +
                        "    GROUP BY mt.tname, mt.id, report_year, report_month\n" +
                        "),\n" +
                        "\n" +
                        "StockValue AS (\n" +
                        "    SELECT\n" +
                        "        mt.id AS type_id,\n" +
                        "        SUM(m.price) AS 在库金额,\n" +
                        "        CASE WHEN mt.tname LIKE '%(进口)' THEN 1 ELSE 0 END AS is_import\n" +
                        "    FROM material_type mt\n" +
                        "    LEFT JOIN material m ON mt.id = m.type_id\n" +
                        "    WHERE m.depository_id = ?" +
                        "    GROUP BY mt.id\n" +
                        "),\n" +
                        "\n" +
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
                        "    WHERE cd.is_import = 1\n" +
                        "\n" +
                        "    UNION ALL\n" +
                        "\n" +
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
                        "    WHERE cd.is_import = 0\n" +
                        "),\n" +
                        "\n" +
                        "Totals AS (\n" +
                        "    SELECT\n" +
                        "        '总计' AS 日期,\n" +
                        "        ' ' AS 材料类型,\n" +
                        "        SUM(cd.入库金额) AS 入库金额,\n" +
                        "        SUM(cd.出库金额) AS 出库金额,\n" +
                        "        SUM(COALESCE(s.在库金额, 0)) AS 在库金额,\n" +
                        "        cd.is_import,\n" +
                        "        9999 AS type_id\n" +
                        "    FROM CombinedData cd\n" +
                        "    LEFT JOIN StockValue s ON cd.type_id = s.type_id\n" +
                        "    GROUP BY cd.report_year, cd.report_month, cd.is_import\n" +
                        ")\n" +
                        "\n" +
                        "SELECT 日期, 材料类型, 入库金额, 出库金额, 在库金额\n" +
                        "FROM (\n" +
                        "    SELECT 日期, 材料类型, 入库金额, 出库金额, 在库金额, is_import, type_id\n" +
                        "    FROM FinalData\n" +
                        "\n" +
                        "    UNION ALL\n" +
                        "\n" +
                        "    SELECT 日期, 材料类型, 入库金额, 出库金额, 在库金额, is_import, type_id\n" +
                        "    FROM Totals\n" +
                        ") AS SubQuery\n" +
                        "ORDER BY is_import DESC, type_id ASC, 日期 DESC;";

        return jdbcTemplate.queryForList(sql, year, month, month, year, depositoryId, depositoryId);
    }
}

