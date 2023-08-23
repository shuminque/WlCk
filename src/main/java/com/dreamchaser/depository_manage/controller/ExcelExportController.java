package com.dreamchaser.depository_manage.controller;

import com.dreamchaser.depository_manage.service.ReportService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/export")
public class ExcelExportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/report")
    public ResponseEntity<ByteArrayResource> exportReportToExcel(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @RequestParam("depositoryId") int depositoryId) throws Exception {
        List<Map<String, Object>> data = reportService.fetchReportData(year, month, depositoryId);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Report");
        // Create header row
        XSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("AT ID");
        headerRow.createCell(1).setCellValue("MNAME");
        headerRow.createCell(2).setCellValue("MODEL");
        headerRow.createCell(3).setCellValue("入库数量");
        headerRow.createCell(4).setCellValue("入库金额");
        headerRow.createCell(5).setCellValue("出库数量");
        headerRow.createCell(6).setCellValue("出库金额");
        headerRow.createCell(7).setCellValue("存储数量");
        headerRow.createCell(8).setCellValue("总金额");

        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> record = data.get(i);
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(((Integer) getOrDefault(record, "at_id", 0)).intValue());
            row.createCell(1).setCellValue((String) getOrDefault(record, "mname", "aaa"));
            row.createCell(2).setCellValue((String) getOrDefault(record, "model", ""));
            row.createCell(3).setCellValue(((Double) getOrDefault(record, "入库数量", 0.0)).doubleValue());
            row.createCell(4).setCellValue(((Double) getOrDefault(record, "入库金额", 0.0)).doubleValue());
            row.createCell(5).setCellValue(((Double) getOrDefault(record, "出库数量", 0.0)).doubleValue());
            row.createCell(6).setCellValue(((Double) getOrDefault(record, "出库金额", 0.0)).doubleValue());
            row.createCell(7).setCellValue(((Double) getOrDefault(record, "存储数量", 0.0)).doubleValue());
            row.createCell(8).setCellValue(((Double) getOrDefault(record, "总金额", 0.0)).doubleValue());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=report.xlsx")
                .body(resource);
    }


    @GetMapping("/every")
    public ResponseEntity<ByteArrayResource> everyTypeToExcel(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @RequestParam("depositoryId") int depositoryId) throws Exception {
        List<Map<String, Object>> data = reportService.everyTypeData(year, month, depositoryId);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Report");
        // Create header row
        XSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("日期");
        headerRow.createCell(1).setCellValue("分类");
        headerRow.createCell(2).setCellValue("入库金额");
        headerRow.createCell(3).setCellValue("出库金额");
        headerRow.createCell(4).setCellValue("在库金额");


        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> record = data.get(i);
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue((String) getOrDefault(record, "日期", ""));
            row.createCell(1).setCellValue((String) getOrDefault(record, "材料类型", "aaa"));
            row.createCell(2).setCellValue(((Double) getOrDefault(record, "入库金额", 0.0)).doubleValue());
            row.createCell(3).setCellValue(((Double) getOrDefault(record, "出库金额", 0.0)).doubleValue());
            row.createCell(4).setCellValue(((Double) getOrDefault(record, "在库金额", 0.0)).doubleValue());

           }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=report.xlsx")
                .body(resource);
    }

    private Object getOrDefault(Map<String, Object> map, String key, Object defaultValue) {
        return map.containsKey(key) && map.get(key) != null ? map.get(key) : defaultValue;
    }

}
