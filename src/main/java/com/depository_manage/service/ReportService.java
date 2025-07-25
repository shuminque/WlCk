package com.depository_manage.service;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List<Map<String, Object>> fetchReportDzData(String startDate, String endDate, int depositoryId) {
        String sql =
                "SELECT\n" +
                        "    mt.tname as 呆滞仓分类,\n" +
                        "    m.at_id as AT号,\n" +
                        "    m.mname as 品名,\n" +
                        "    COALESCE(LEFT(m.model, 30), 'N/A') AS 规格,\n" +
                        "    SUM(CASE WHEN dr.type = 1 AND dr.apply_remark NOT LIKE ('%领用退回%')THEN dr.quantity ELSE 0 END) AS 入库数量,\n" +
                        "    FORMAT(ROUND((SUM(CASE WHEN dr.type = 1 AND dr.apply_remark NOT LIKE ('%领用退回%') THEN dr.price * dr.quantity ELSE 0 END)),2), 2) AS 入库金额,\n" +
                        "    SUM(CASE WHEN dr.type = 0 AND dr.apply_remark NOT IN ('SAB转入ZAB','ZAB转入SAB') AND dr.apply_remark NOT LIKE ('%销售出库%') THEN dr.quantity ELSE 0 END)" +
                        "   -SUM(CASE WHEN dr.type = 1 AND dr.apply_remark LIKE ('%领用退回%') THEN dr.quantity ELSE 0 END) AS 出库数量,\n" +
                        "    FORMAT(ROUND((SUM(CASE WHEN dr.type = 0 AND dr.apply_remark NOT IN ('SAB转入ZAB','ZAB转入SAB') AND dr.apply_remark NOT LIKE ('%销售出库%') THEN dr.price * dr.quantity ELSE 0 END))" +
                        "                -(SUM(CASE WHEN dr.type = 1 AND dr.apply_remark LIKE ('%领用退回%') THEN dr.price * dr.quantity ELSE 0 END)),2), 2) AS 出库金额,\n" +
                        "    SUM(CASE WHEN dr.type = 0 AND dr.apply_remark IN ('SAB转入ZAB','ZAB转入SAB') THEN dr.quantity ELSE 0 END) AS 转移数量,\n" +
                        "    FORMAT(ROUND((SUM(CASE WHEN dr.type = 0 AND dr.apply_remark IN ('SAB转入ZAB','ZAB转入SAB') THEN dr.price * dr.quantity ELSE 0 END)),2), 2) AS 转移金额,\n" +
                        "    SUM(CASE WHEN dr.type = 0 AND dr.apply_remark LIKE ('%销售出库%') THEN dr.quantity ELSE 0 END) AS 销售数量,\n" +
                        "    FORMAT(ROUND((SUM(CASE WHEN dr.type = 0 AND dr.apply_remark LIKE ('%销售出库%') THEN dr.price * dr.quantity ELSE 0 END)),2), 2) AS 销售金额,\n" +
                        "    m.quantity - COALESCE((SELECT SUM(CASE WHEN dr_after.type = 1 THEN dr_after.quantity ELSE -dr_after.quantity END)\n" +
                        "                          FROM depository_record dr_after\n" +
                        "                          WHERE dr_after.at_id = m.at_id\n" +
                        "                            AND dr_after.depository_id = m.depository_id\n" +
                        "                            AND dr_after.review_pass = 1\n" +
                        "                            AND dr_after.apply_time >= DATE_ADD(?, INTERVAL 1 DAY)), 0) AS 库存数量,\n" +
                        "    FORMAT(ROUND(m.price - COALESCE((SELECT SUM(CASE WHEN dr_after.type = 1 THEN dr_after.price * dr_after.quantity ELSE -dr_after.price * dr_after.quantity END)\n" +
                        "                                  FROM depository_record dr_after\n" +
                        "                                    WHERE dr_after.at_id = m.at_id\n" +
                        "                                    AND dr_after.depository_id = m.depository_id\n" +
                        "                                    AND dr_after.review_pass = 1\n" +
                        "                                    AND dr_after.apply_time >= DATE_ADD(?, INTERVAL 1 DAY)), 0), 2), 2) AS 在库金额\n" +
                        "FROM\n" +
                        "    material m\n" +
                        "LEFT JOIN\n" +
                        "    depository_record dr\n" +
                        "    ON m.at_id = dr.at_id\n" +
                        "    AND m.depository_id = dr.depository_id\n" +
                        "    AND dr.review_pass = 1\n" +
                        "    AND dr.apply_time >= ? AND dr.apply_time < DATE_ADD(?, INTERVAL 1 DAY)\n" +
                        "LEFT JOIN\n" +
                        "    material_type mt\n" +
                        "    ON m.type_id = mt.id\n" +
                        "WHERE m.depository_id = ?\n" +
                        "  AND m.state_id = 6\n" +
                        "GROUP BY\n" +
                        "    mt.id, m.at_id, m.mname, m.model, m.quantity, m.price\n" +
                        "ORDER BY\n" +
                        "    CASE WHEN mt.id IS NULL THEN 1 ELSE 0 END,\n" +
                        "    mt.id, \n" +
                        "    m.at_id;";
        return jdbcTemplate.queryForList(sql, endDate, endDate, startDate, endDate, depositoryId);
    }

    public List<Map<String, Object>> fetchReportData(String startDate, String endDate, int depositoryId) {
        String sql =
                "SELECT\n" +
                        "    mt.tname as 分类,\n" +
                        "    m.at_id as AT号,\n" +
                        "    m.mname as 品名,\n" +
                        "    COALESCE(LEFT(m.model, 30), 'N/A') AS 规格,\n" +
                        "    SUM(CASE WHEN dr.type = 1 AND dr.apply_remark LIKE '%内制入库%' THEN dr.quantity ELSE 0 END) AS 内制入库数量,\n" +
                        "    FORMAT(ROUND(SUM(CASE WHEN dr.type = 1 AND dr.apply_remark LIKE '%内制入库%' THEN dr.price * dr.quantity ELSE 0 END), 2), 2) AS 内制入库金额,\n" +
                        "    SUM(CASE WHEN dr.type = 1 AND dr.apply_remark NOT LIKE '%领用退回%' AND dr.apply_remark NOT LIKE '%内制入库%' THEN dr.quantity ELSE 0 END) AS 入库数量,\n" +
                        "    FORMAT(ROUND(SUM(CASE WHEN dr.type = 1 AND dr.apply_remark NOT LIKE '%领用退回%' AND dr.apply_remark NOT LIKE '%内制入库%' THEN dr.price * dr.quantity ELSE 0 END), 2), 2) AS 入库金额,\n" +
                        "    SUM(CASE WHEN dr.type = 0 AND dr.apply_remark NOT IN ('SAB转入ZAB','ZAB转入SAB') AND dr.apply_remark NOT LIKE ('%销售出库%') AND dr.apply_remark NOT LIKE '%新刀制作室%' THEN dr.quantity ELSE 0 END)" +
                        "   -SUM(CASE WHEN dr.type = 1 AND dr.apply_remark LIKE ('%领用退回%') THEN dr.quantity ELSE 0 END) AS 出库数量,\n" +
                        "    FORMAT(ROUND((SUM(CASE WHEN dr.type = 0 AND dr.apply_remark NOT IN ('SAB转入ZAB','ZAB转入SAB') AND dr.apply_remark NOT LIKE ('%销售出库%') AND dr.apply_remark NOT LIKE '%新刀制作室%' THEN dr.price * dr.quantity ELSE 0 END))" +
                        "                -(SUM(CASE WHEN dr.type = 1 AND dr.apply_remark LIKE ('%领用退回%') THEN dr.price * dr.quantity ELSE 0 END)),2), 2) AS 出库金额,\n" +
                        "    SUM(CASE WHEN dr.type = 0 AND dr.apply_remark IN ('SAB转入ZAB','ZAB转入SAB') THEN dr.quantity ELSE 0 END) AS 转移数量,\n" +
                        "    FORMAT(ROUND((SUM(CASE WHEN dr.type = 0 AND dr.apply_remark IN ('SAB转入ZAB','ZAB转入SAB') THEN dr.price * dr.quantity ELSE 0 END)),2), 2) AS 转移金额,\n" +
                        "    SUM(CASE WHEN dr.type = 0 AND dr.apply_remark LIKE ('%销售出库%') THEN dr.quantity ELSE 0 END) AS 销售数量,\n" +
                        "    FORMAT(ROUND((SUM(CASE WHEN dr.type = 0 AND dr.apply_remark LIKE ('%销售出库%') THEN dr.price * dr.quantity ELSE 0 END)),2), 2) AS 销售金额,\n" +
                        "    SUM(CASE WHEN dr.type = 0 AND dr.apply_remark LIKE '%新刀制作室%' THEN dr.quantity ELSE 0 END) AS 制作室出库数量,\n" +
                        "    FORMAT(ROUND(SUM(CASE WHEN dr.type = 0 AND dr.apply_remark LIKE '%新刀制作室%' THEN dr.price * dr.quantity ELSE 0 END), 2), 2) AS 制作室出库金额,\n" +
                        "    m.quantity - COALESCE((SELECT SUM(CASE WHEN dr_after.type = 1 THEN dr_after.quantity ELSE -dr_after.quantity END)\n" +
                        "                          FROM depository_record dr_after\n" +
                        "                          WHERE dr_after.at_id = m.at_id\n" +
                        "                            AND dr_after.depository_id = m.depository_id\n" +
                        "                            AND dr_after.review_pass = 1\n" +
                        "                            AND dr_after.apply_time >= DATE_ADD(?, INTERVAL 1 DAY)), 0) AS 库存数量,\n" +
                        "    FORMAT(ROUND(m.price - COALESCE((SELECT SUM(CASE WHEN dr_after.type = 1 THEN dr_after.price * dr_after.quantity ELSE -dr_after.price * dr_after.quantity END)\n" +
                        "                                  FROM depository_record dr_after\n" +
                        "                                    WHERE dr_after.at_id = m.at_id\n" +
                        "                                    AND dr_after.depository_id = m.depository_id\n" +
                        "                                    AND dr_after.review_pass = 1\n" +
                        "                                    AND dr_after.apply_time >= DATE_ADD(?, INTERVAL 1 DAY)), 0), 2), 2) AS 在库金额\n" +
                        "FROM\n" +
                        "    material m\n" +
                        "LEFT JOIN\n" +
                        "    depository_record dr\n" +
                        "    ON m.at_id = dr.at_id\n" +
                        "    AND m.depository_id = dr.depository_id\n" +
                        "    AND dr.review_pass = 1\n" +
                        "    AND dr.apply_time >= ? AND dr.apply_time < DATE_ADD(?, INTERVAL 1 DAY)\n" +
                        "LEFT JOIN\n" +
                        "    material_type mt\n" +
                        "    ON m.type_id = mt.id\n" +
                        "WHERE m.depository_id = ?\n" +
                        "  AND m.state_id != 6\n" +
                        "  AND m.type_id is not null \n" +
                        "GROUP BY\n" +
                        "    mt.id, m.at_id, m.mname, m.model, m.quantity, m.price\n" +
                        "ORDER BY\n" +
                        "    CASE WHEN mt.id IS NULL THEN 1 ELSE 0 END,\n" +
                        "    mt.id, \n" +
                        "    m.at_id;";
        return jdbcTemplate.queryForList(sql, endDate, endDate, startDate, endDate, depositoryId);
    }
    public Pair<List<Map<String, Object>>, Integer> fetchReportData(String startDate, String endDate, int depositoryId, int page, int size) {        int offset = (page - 1) * size;
        String sql =
                "SELECT\n" +
                        "    mt.tname as 分类,\n" +
                        "    m.at_id as AT号,\n" +
                        "    m.mname as 品名,\n" +
                        "    me.ename as 工程,\n" +
                        "    COALESCE(LEFT(m.model, 30), 'N/A') AS 规格,\n" +
                        "    SUM(CASE WHEN dr.type = 1 AND dr.apply_remark LIKE '%内制入库%' THEN dr.quantity ELSE 0 END) AS 内制入库数量,\n" +
                        "    FORMAT(ROUND(SUM(CASE WHEN dr.type = 1 AND dr.apply_remark LIKE '%内制入库%' THEN dr.price * dr.quantity ELSE 0 END), 2), 2) AS 内制入库金额,\n" +
                        "    SUM(CASE WHEN dr.type = 1 AND dr.apply_remark NOT LIKE '%领用退回%' AND dr.apply_remark NOT LIKE '%内制入库%' THEN dr.quantity ELSE 0 END) AS 入库数量,\n" +
                        "    FORMAT(ROUND(SUM(CASE WHEN dr.type = 1 AND dr.apply_remark NOT LIKE '%领用退回%' AND dr.apply_remark NOT LIKE '%内制入库%' THEN dr.price * dr.quantity ELSE 0 END), 2), 2) AS 入库金额,\n" +
                        "    SUM(CASE WHEN dr.type = 0 AND dr.apply_remark NOT IN ('SAB转入ZAB','ZAB转入SAB') AND dr.apply_remark NOT LIKE ('%销售出库%') AND dr.apply_remark NOT LIKE '%新刀制作室%' THEN dr.quantity ELSE 0 END)" +
                        "   -SUM(CASE WHEN dr.type = 1 AND dr.apply_remark LIKE ('%领用退回%') THEN dr.quantity ELSE 0 END) AS 出库数量,\n" +
                        "    FORMAT(ROUND((SUM(CASE WHEN dr.type = 0 AND dr.apply_remark NOT IN ('SAB转入ZAB','ZAB转入SAB') AND dr.apply_remark NOT LIKE ('%销售出库%') AND dr.apply_remark NOT LIKE '%新刀制作室%' THEN dr.price * dr.quantity ELSE 0 END))" +
                        "                -(SUM(CASE WHEN dr.type = 1 AND dr.apply_remark LIKE ('%领用退回%') THEN dr.price * dr.quantity ELSE 0 END)),2), 2) AS 出库金额,\n" +
                        "    SUM(CASE WHEN dr.type = 0 AND dr.apply_remark IN ('SAB转入ZAB','ZAB转入SAB') THEN dr.quantity ELSE 0 END) AS 转移数量,\n" +
                        "    FORMAT(ROUND((SUM(CASE WHEN dr.type = 0 AND dr.apply_remark IN ('SAB转入ZAB','ZAB转入SAB') THEN dr.price * dr.quantity ELSE 0 END)),2), 2) AS 转移金额,\n" +
                        "    SUM(CASE WHEN dr.type = 0 AND dr.apply_remark LIKE ('%销售出库%') THEN dr.quantity ELSE 0 END) AS 销售数量,\n" +
                        "    FORMAT(ROUND((SUM(CASE WHEN dr.type = 0 AND dr.apply_remark LIKE ('%销售出库%') THEN dr.price * dr.quantity ELSE 0 END)),2), 2) AS 销售金额,\n" +
                        "    SUM(CASE WHEN dr.type = 0 AND dr.apply_remark LIKE '%新刀制作室%' THEN dr.quantity ELSE 0 END) AS 制作室出库数量,\n" +
                        "    FORMAT(ROUND(SUM(CASE WHEN dr.type = 0 AND dr.apply_remark LIKE '%新刀制作室%' THEN dr.price * dr.quantity ELSE 0 END), 2), 2) AS 制作室出库金额,\n" +
                        "    m.quantity - COALESCE((SELECT SUM(CASE WHEN dr_after.type = 1 THEN dr_after.quantity ELSE -dr_after.quantity END)\n" +
                        "                          FROM depository_record dr_after\n" +
                        "                          WHERE dr_after.at_id = m.at_id\n" +
                        "                            AND dr_after.depository_id = m.depository_id\n" +
                        "                            AND dr_after.review_pass = 1\n" +
                        "                            AND dr_after.apply_time >= DATE_ADD(?, INTERVAL 1 DAY)), 0) AS 库存数量,\n" +
                        "    FORMAT(ROUND(m.price - COALESCE((SELECT SUM(CASE WHEN dr_after.type = 1 THEN dr_after.price * dr_after.quantity ELSE -dr_after.price * dr_after.quantity END)\n" +
                        "                                  FROM depository_record dr_after\n" +
                        "                                    WHERE dr_after.at_id = m.at_id\n" +
                        "                                    AND dr_after.depository_id = m.depository_id\n" +
                        "                                    AND dr_after.review_pass = 1\n" +
                        "                                    AND dr_after.apply_time >= DATE_ADD(?, INTERVAL 1 DAY)), 0), 2), 2) AS 在库金额\n" +
                        "FROM\n" +
                        "    material m\n" +
                        "LEFT JOIN\n" +
                        "    depository_record dr\n" +
                        "    ON m.at_id = dr.at_id\n" +
                        "    AND m.depository_id = dr.depository_id\n" +
                        "    AND dr.review_pass = 1\n" +
                        "    AND dr.apply_time >= ? AND dr.apply_time < DATE_ADD(?, INTERVAL 1 DAY)\n" +
                        "LEFT JOIN\n" +
                        "    material_type mt\n" +
                        "    ON m.type_id = mt.id\n" +
                        "LEFT JOIN\n" +
                        "    material_engin me\n" +
                        "    ON m.engin_id = me.id\n" +
                        "WHERE m.depository_id = ?\n" +
                        "  AND m.state_id != 6\n" +
                        "  AND m.type_id is not null \n" +
                        "GROUP BY\n" +
                        "    mt.id, m.at_id, m.mname, m.model, m.quantity, m.price,me.ename\n" +
                        "ORDER BY\n" +
                        "    CASE WHEN mt.id IS NULL THEN 1 ELSE 0 END,\n" +
                        "    mt.id, \n" +
                        "    m.at_id \n" +
                        "LIMIT ? OFFSET ?;";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, endDate, endDate, startDate, endDate, depositoryId, size, offset);
        String countSql =
                "SELECT COUNT(*) " +
                        "FROM\n" +
                        "    material m\n" +
                        "WHERE m.depository_id = ?\n" +
                        "  AND m.state_id != 6\n" +
                        "  AND m.type_id is not null \n";
        Integer count = jdbcTemplate.queryForObject(countSql, Integer.class,depositoryId);
        return Pair.of(data, count);
    }

    public List<Map<String, Object>> everyTypeData(String startDate, String endDate, int depositoryId) {
        String sql =
                "WITH DepRecordSum AS (\n" +
                        "    SELECT\n" +
                        "        mt.id AS type_id,\n" +
                        "        mt.tname AS material_type_name,\n" +
                        "    SUM(CASE WHEN dr.type = 1 AND dr.apply_remark NOT LIKE ('%领用退回%') THEN dr.price * dr.quantity ELSE 0 END) AS in_sum,\n" +
                        "    SUM(CASE WHEN dr.type = 0 AND dr.apply_remark NOT IN ('SAB转入ZAB', 'ZAB转入SAB') AND dr.apply_remark NOT LIKE '%销售出库%' THEN dr.price * dr.quantity ELSE 0 END)" +
                        "   -SUM(CASE WHEN dr.type = 1 AND dr.apply_remark LIKE '%领用退回%' THEN dr.price * dr.quantity ELSE 0 END) AS normal_out_sum,\n" +
                        "    SUM(CASE WHEN dr.type = 0 AND dr.apply_remark IN ('SAB转入ZAB', 'ZAB转入SAB') THEN dr.price * dr.quantity ELSE 0 END) AS transfer_out_sum,\n" +
                        "    SUM(CASE WHEN dr.type = 0 AND dr.apply_remark LIKE '%销售出库%' THEN dr.price * dr.quantity ELSE 0 END) AS sales_out_sum\n" +
                        "    FROM material_type mt\n" +
                        "    LEFT JOIN depository_record dr ON mt.tname = dr.type_name\n" +
                        "        AND dr.apply_time >= ? AND dr.apply_time < DATE_ADD(?, INTERVAL 1 DAY)\n" +
                        "        AND dr.review_pass = 1\n" +
                        "        AND dr.depository_id = ?\n" +
                        "    LEFT JOIN material m ON m.at_id = dr.at_id AND m.depository_id = dr.depository_id AND m.state_id != 6\n" +
                        "    GROUP BY mt.id, mt.tname\n" +
                        "),\n" +
                        "OnceFillSum AS (\n" +
                        "    SELECT\n" +
                        "        mt.id AS type_id,\n" +
                        "        mt.tname AS material_type_name,\n" +
                        "    SUM(CASE WHEN once_f.time >= ? AND once_f.time < DATE_ADD(?, INTERVAL 1 DAY) AND once_f.apply_remark NOT LIKE '%销售出库%' AND once_f.introduce NOT LIKE '%立项%' THEN once_f.price ELSE 0 END) AS once_normal_sum,\n" +
                        "    SUM(CASE WHEN once_f.time >= ? AND once_f.time < DATE_ADD(?, INTERVAL 1 DAY) AND once_f.apply_remark LIKE '%销售出库%'  THEN once_f.price ELSE 0 END) AS once_sales_sum \n" +
                        "    FROM material_type mt\n" +
                        "    LEFT JOIN once_fill once_f ON mt.id = once_f.type_id\n" +
                        "        AND once_f.depositoryId = ?\n" +
                        "    GROUP BY mt.id, mt.tname\n" +
                        "),\n" +
                        "CombinedData AS (\n" +
                        " SELECT\n" +
                        "        mt.id AS type_id,\n" +
                        "        CASE WHEN mt.tname LIKE '%(进口)' THEN 1 ELSE 0 END AS is_import,\n" +
                        "        mt.tname AS material_type_name,\n" +
                        "        COALESCE(drs.in_sum, 0) + COALESCE(ofs.once_normal_sum, 0) + COALESCE(ofs.once_sales_sum, 0) AS raw_in_money,\n" +
                        "        FORMAT(COALESCE(drs.in_sum, 0) + COALESCE(ofs.once_normal_sum, 0) + COALESCE(ofs.once_sales_sum, 0), 2) AS 入库金额,\n" +
                        "        COALESCE(drs.normal_out_sum, 0) + COALESCE(ofs.once_normal_sum, 0) AS raw_normal_out_money,\n" +
                        "        FORMAT(COALESCE(drs.normal_out_sum, 0) + COALESCE(ofs.once_normal_sum, 0), 2) AS 正常出库金额,\n" +
                        "        COALESCE(drs.transfer_out_sum, 0) AS raw_transfer_out_money,\n" +
                        "        FORMAT(COALESCE(drs.transfer_out_sum, 0), 2) AS 转移出库金额,\n" +
                        "        COALESCE(drs.sales_out_sum, 0) + COALESCE(ofs.once_sales_sum, 0) AS raw_sales_out_money,\n" +
                        "        FORMAT(COALESCE(drs.sales_out_sum, 0) + COALESCE(ofs.once_sales_sum, 0), 2) AS 销售出库金额\n" +
                        "    FROM material_type mt\n" +
                        "    LEFT JOIN DepRecordSum drs ON mt.id = drs.type_id\n" +
                        "    LEFT JOIN OnceFillSum ofs ON mt.id = ofs.type_id\n" +
                        "    GROUP BY mt.id, mt.tname, is_import\n" +
                        "),\n" +
                        "StockValue AS (\n" +
                        "    SELECT\n" +
                        "        mt.id AS type_id,\n" +
                        "        (SUM(m.price) - COALESCE((\n" +
                        "            SELECT SUM(CASE WHEN dr.type = 1 THEN dr.price * dr.quantity ELSE -dr.price * dr.quantity END)\n" +
                        "            FROM depository_record dr\n" +
                        "            WHERE dr.type_name = mt.tname AND dr.apply_time >= DATE_ADD(?, INTERVAL 1 DAY) AND dr.review_pass = 1 AND dr.depository_id = ?\n" +
                        "        ), 0)) AS raw_stock_money,\n" +
                        "        FORMAT((SUM(m.price) - COALESCE((\n" +
                        "            SELECT SUM(CASE WHEN dr.type = 1 THEN dr.price * dr.quantity ELSE -dr.price * dr.quantity END)\n" +
                        "            FROM depository_record dr\n" +
                        "            WHERE dr.type_name = mt.tname AND dr.apply_time >= DATE_ADD(?, INTERVAL 1 DAY) AND dr.review_pass = 1 AND dr.depository_id = ?\n" +
                        "        ), 0)), 2) AS 在库金额,\n" +
                        "        CASE WHEN mt.tname LIKE '%(进口)' THEN 1 ELSE 0 END AS is_import\n" +
                        "    FROM material_type mt\n" +
                        "    LEFT JOIN material m ON mt.id = m.type_id\n" +
                        "    WHERE m.depository_id = ? AND m.state_id != 6" +
                        "    GROUP BY mt.id\n" +
                        "),\n" +
                        "FinalData AS (\n" +
                        "    SELECT\n" +
                        "        CONCAT(?, ' 至 ', ?) AS 日期,\n" +
                        "        cd.material_type_name AS 分类,\n" +
                        "        cd.入库金额,\n" +
                        "        cd.正常出库金额,\n" +
                        "        cd.转移出库金额,\n" +
                        "        cd.销售出库金额,\n" +
                        "        COALESCE(s.在库金额, 0) AS 在库金额,\n" +
                        "        cd.is_import,\n" +
                        "        cd.type_id\n" +
                        "    FROM CombinedData cd\n" +
                        "    LEFT JOIN StockValue s ON cd.type_id = s.type_id\n" +
                        "),\n" +
                            "Totals AS (\n" +
                            "    SELECT\n" +
                            "        '总计' AS 日期,\n" +
                            "        ' ' AS 分类,\n" +
                            "        FORMAT(SUM(cd.raw_in_money), 2) AS 入库金额,\n" +
                            "        FORMAT(SUM(cd.raw_normal_out_money), 2) AS 正常出库金额,\n" +
                            "        FORMAT(SUM(cd.raw_transfer_out_money), 2) AS 转移出库金额,\n" +
                            "        FORMAT(SUM(cd.raw_sales_out_money), 2) AS 销售出库金额,\n" +
                            "        FORMAT(SUM(COALESCE(s.raw_stock_money, 0)), 2) AS 在库金额,\n" +
                            "        cd.is_import,\n" +
                            "        9999 AS type_id\n" +
                            "    FROM CombinedData cd\n" +
                            "    LEFT JOIN StockValue s ON cd.type_id = s.type_id\n" +
                            "    GROUP BY cd.is_import\n" +
                            ")\n" +
                        "SELECT 日期, 分类, 入库金额, 正常出库金额, 转移出库金额, 销售出库金额, 在库金额\n" +
                        "FROM (\n" +
                        "    SELECT 日期, 分类, 入库金额, 正常出库金额, 转移出库金额, 销售出库金额, 在库金额, is_import, type_id\n" +
                        "    FROM FinalData\n" +
                        "    UNION ALL\n" +
                        "    SELECT 日期, 分类, 入库金额, 正常出库金额, 转移出库金额, 销售出库金额, 在库金额, is_import, type_id\n" +
                        "    FROM Totals\n" +
                        ") AS SubQuery\n" +
                        "WHERE 入库金额 != '0.00' OR 正常出库金额 != '0.00' OR 转移出库金额 != '0.00' OR 销售出库金额 != '0.00' OR 在库金额 != '0'\n" +
                        "ORDER BY is_import DESC, type_id ASC, 日期 DESC;\n";
        return jdbcTemplate.queryForList(sql,
                startDate, endDate,  // 1 - 2 (仓库记录)
                depositoryId,        // 3 (仓库记录)
                startDate, endDate,  // 4 - 5 (一次性领用)
                startDate, endDate,  // 4 - 5 (一次性销售)
                depositoryId,        // 6 (一次性领用)
                endDate,             // 7 (库存 - 结束日期之后的记录)
                depositoryId,        // 8 (库存 - 结束日期之后的记录)
                endDate,             // 9 (库存 - 结束日期之后的记录)
                depositoryId,        // 10 (库存 - 结束日期之后的记录)
                depositoryId,        // 11 (库存)
                startDate, endDate   // 12 - 13 (最终数据)
        );
    }

    public List<Map<String, Object>> transferData(String startDate, String endDate) {
        String sql =
                "SELECT\n" +
                        "    DATE_FORMAT(o.apply_time, '%Y/%m/%d') AS 日期,\n" +
                        "    o.type_name AS 分类,\n" +
                        "    o.at_id AS AT,\n" +
                        "    o.mname AS 品名,\n" +
                        "    o.model AS 型号,\n" +
                        "    FORMAT(o.price, 2) AS 单价,\n" +
                        "    o.quantity AS 数量,\n" +
                        "    FORMAT(ROUND(o.price * o.quantity, 2), 2) AS 总价,\n" +
                        "    o.apply_remark AS 备注\n" +
                        "FROM\n" +
                        "    depository_record AS o\n" +
                        "WHERE\n" +
                        "    o.apply_time >= ? AND o.apply_time < DATE_ADD(?, INTERVAL 1 DAY)\n" +
                        "    AND o.type = 0\n" +
                        "    AND (\n" +
                        "        o.apply_remark = 'SAB转入ZAB'\n" +
                        "        OR o.apply_remark = 'ZAB转入SAB'\n" +
                        "    )\n" +
                        "ORDER BY\n" +
                        "    FIELD(o.apply_remark, 'SAB转入ZAB', 'ZAB转入SAB'),\n" +
                        "    o.apply_time DESC;\n";
        return jdbcTemplate.queryForList(sql, startDate, endDate);
    }

}