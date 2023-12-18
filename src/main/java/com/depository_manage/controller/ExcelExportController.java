package com.depository_manage.controller;

import com.depository_manage.service.ReportService;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/export")
public class ExcelExportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/report")
    public ResponseEntity<ByteArrayResource> exportReportToExcel(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("depositoryId") int depositoryId) throws Exception {
        List<Map<String, Object>> data = reportService.fetchReportData(startDate, endDate, depositoryId);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Report");
        XSSFSheet subtotalSheet = workbook.createSheet("分类小计");

        // 创建单元格样式并设置边框
        XSSFCellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        // 创建表头行并设置样式
        XSSFRow headerRow = sheet.createRow(0);
        createCellWithStyle(headerRow, 0, "分类", style);
        createCellWithStyle(headerRow, 1, "AT号", style);
        createCellWithStyle(headerRow, 2, "品名", style);
        createCellWithStyle(headerRow, 3, "规格", style);
        createCellWithStyle(headerRow, 4, "入库数量", style);
//        createCellWithStyle(headerRow, 5, "", style); // 空白列
        createCellWithStyle(headerRow, 5, "入库金额", style);
//        createCellWithStyle(headerRow, 7, "", style); // 空白列
        createCellWithStyle(headerRow, 6, "出库数量", style);
//        createCellWithStyle(headerRow, 9, "", style); // 空白列
        createCellWithStyle(headerRow, 7, "出库金额", style);
//        createCellWithStyle(headerRow, 11, "", style); // 空白列
        createCellWithStyle(headerRow, 8, "库存数量", style);
//        createCellWithStyle(headerRow, 13, "", style); // 空白列
        createCellWithStyle(headerRow, 9, "在库金额", style);
//        createCellWithStyle(headerRow, 15, "", style); // 空白列

        XSSFRow subtotalHeaderRow = subtotalSheet.createRow(0);
        createCellWithStyle(subtotalHeaderRow, 0, "分类", style);
        createCellWithStyle(subtotalHeaderRow, 1, "入库数量", style);
        createCellWithStyle(subtotalHeaderRow, 2, "入库金额", style);
        createCellWithStyle(subtotalHeaderRow, 3, "出库数量", style);
        createCellWithStyle(subtotalHeaderRow, 4, "出库金额", style);
        createCellWithStyle(subtotalHeaderRow, 5, "库存数量", style);
        createCellWithStyle(subtotalHeaderRow, 6, "在库金额", style);
        double totalInAmount = 0.0, totalOutAmount = 0.0, totalStockAmount = 0.0;
        double totalInQty = 0.0, totalOutQty = 0.0, totalStockQty = 0.0;
        String previousCategory = "";
        int rowIndex = 1;
        for (Map<String, Object> record : data) {
            // 如果当前记录没有分类，则将其标记为"未分类"
            String currentCategory = (String) getOrDefault(record, "分类", "未分类");

            // 检查当前分类是否已更改，表示一个新分类的开始
            if (!currentCategory.equals(previousCategory) && rowIndex != 1) {
                // 在分类小计表中添加前一个分类的小计行
                addSubtotalRow(subtotalSheet, previousCategory.isEmpty() ? "未分类" : previousCategory, totalInQty, totalOutQty, totalStockQty, totalInAmount, totalOutAmount, totalStockAmount, style);

                // 在主数据表中添加空白行
                int blankRowIndex = rowIndex++;
                sheet.createRow(blankRowIndex);

                // 重置累计变量
                totalInQty = totalOutQty = totalStockQty = totalInAmount = totalOutAmount = totalStockAmount = 0.0;
            }

            XSSFRow row = sheet.createRow(rowIndex);

            // 如果是新分类的开始并且分类名称是空的，则在这一行设置分类名称为"未分类"
            createCellWithStyle(row, 0, currentCategory.equals(previousCategory) ? "" : currentCategory, style);
            // 其他列的处理保持不变
            createCellWithStyle(row, 1, ((Integer) getOrDefault(record, "AT号", 0)).intValue(), style);
            createCellWithStyle(row, 2, (String) getOrDefault(record, "品名", ""), style);
            createCellWithStyle(row, 3, (String) getOrDefault(record, "规格", ""), style);
            createCellWithStyle(row, 4, ((Double) getOrDefault(record, "入库数量", 0.0)).doubleValue(), style);
//            createCellWithStyle(row, 5, Double.parseDouble((String) getOrDefault(record, "入库金额", "0.00")), style);
            createCellWithStyle(row, 6 , ((Double) getOrDefault(record, "出库数量", 0.0)).doubleValue(), style);
//            createCellWithStyle(row, 7, Double.parseDouble((String) getOrDefault(record, "出库金额", "0.00")), style);
            createCellWithStyle(row, 8, ((Double) getOrDefault(record, "库存数量", 0.0)).doubleValue(), style);
//            createCellWithStyle(row, 9, Double.parseDouble((String) getOrDefault(record, "在库金额", "0.00")), style);
//            createCellWithStyle(row, 6, Double.parseDouble(((String) getOrDefault(record, "入库金额", "0.00")).replace(",", "")), style);
//            createCellWithStyle(row, 6, Double.parseDouble(((String) getOrDefault(record, "入库金额", "0.00")).replace(",", "")), style);
//            createCellWithStyle(row, 10, Double.parseDouble(((String) getOrDefault(record, "出库金额", "0.00")).replace(",", "")), style);
//            createCellWithStyle(row, 14, Double.parseDouble(((String) getOrDefault(record, "在库金额", "0.00")).replace(",", "")), style);
            createCellWithStyle(row, 5, Double.parseDouble(((String) getOrDefault(record, "入库金额", "0.00")).replace(",", "")), style);
            createCellWithStyle(row, 7, Double.parseDouble(((String) getOrDefault(record, "出库金额", "0.00")).replace(",", "")), style);
            createCellWithStyle(row, 9, Double.parseDouble(((String) getOrDefault(record, "在库金额", "0.00")).replace(",", "")), style);

            // Accumulate totals
            totalInQty += parseToDouble(getOrDefault(record, "入库数量", "0.0").toString());
            totalOutQty += parseToDouble(getOrDefault(record, "出库数量", "0.0").toString());
            totalStockQty += parseToDouble(getOrDefault(record, "库存数量", "0.0").toString());

            totalInAmount += parseToDouble(getOrDefault(record, "入库金额", "0.0").toString());
            totalOutAmount += parseToDouble(getOrDefault(record, "出库金额", "0.0").toString());
            totalStockAmount += parseToDouble(getOrDefault(record, "在库金额", "0.0").toString());

            rowIndex++;
            previousCategory = currentCategory;
        }

        if (!previousCategory.isEmpty() || rowIndex == 1) { // rowIndex == 1 表示从未添加过小计行，即所有数据都没有分类
            // 如果前一个分类为空，表示数据未分类
            previousCategory = previousCategory.isEmpty() ? "未分类" : previousCategory;
            addSubtotalRow(subtotalSheet, previousCategory, totalInQty, totalOutQty, totalStockQty, totalInAmount, totalOutAmount, totalStockAmount, style);
            // 在主数据表中添加最后的空白行
            sheet.createRow(rowIndex);
        }
        subtotalSheet.autoSizeColumn(0); // 第一列的索引是0

        // Add final total row
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        String filename = "物品库存报表_" + startDate + "_to_" + endDate + ".xlsx";
        String encodedFilename = URLEncoder.encode(filename, "UTF-8").replace("+", "%20");
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename)
                .body(resource);
    }
    private void addSubtotalRow(XSSFSheet sheet, int rowIndex, String categoryName, double inQty, double outQty, double stockQty, double inAmount, double outAmount, double stockAmount, XSSFCellStyle style) {
        XSSFRow totalRow = sheet.createRow(rowIndex);
        createCellWithStyle(totalRow, 0, categoryName + " 小计", style);
        createCellWithStyle(totalRow, 4, inQty, style); // 入库数量
        createCellWithStyle(totalRow, 5, inAmount, style); // 入库金额
        createCellWithStyle(totalRow, 6, outQty, style); // 出库数量
        createCellWithStyle(totalRow, 7, outAmount, style); // 出库金额
        createCellWithStyle(totalRow, 8, stockQty, style); // 库存数量
        createCellWithStyle(totalRow, 9, stockAmount, style); // 在库金额
    }
    private void addSubtotalRow(XSSFSheet subtotalSheet, String categoryName, double inQty, double outQty, double stockQty, double inAmount, double outAmount, double stockAmount, XSSFCellStyle style) {
        int rowIndex = subtotalSheet.getLastRowNum() + 1; // 获取下一个空行的行索引
        XSSFRow totalRow = subtotalSheet.createRow(rowIndex);
        createCellWithStyle(totalRow, 0, categoryName, style);
        createCellWithStyle(totalRow, 1, inQty, style);      // 入库数量
        createCellWithStyle(totalRow, 2, inAmount, style);   // 入库金额
        createCellWithStyle(totalRow, 3, outQty, style);     // 出库数量
        createCellWithStyle(totalRow, 4, outAmount, style);  // 出库金额
        createCellWithStyle(totalRow, 5, stockQty, style);   // 库存数量
        createCellWithStyle(totalRow, 6, stockAmount, style); // 在库金额
    }

    private double parseToDouble(String value) {
        try {
            return Double.parseDouble(value.replace(",", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void createCellWithStyle(XSSFRow row, int column, String value, XSSFCellStyle style) {
        XSSFCell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void createCellWithStyle(XSSFRow row, int column, double value, XSSFCellStyle style) {
        XSSFCell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void createCellWithStyle(XSSFRow row, int column, int value, XSSFCellStyle style) {
        XSSFCell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    @GetMapping("/every")
    public ResponseEntity<ByteArrayResource> everyTypeToExcel(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("depositoryId") int depositoryId) throws Exception {
        List<Map<String, Object>> data = reportService.everyTypeData(startDate, endDate, depositoryId);
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
            row.createCell(1).setCellValue((String) getOrDefault(record, "分类", "aaa"));
            row.createCell(2).setCellValue((String) getOrDefault(record, "入库金额", "0.00"));
            row.createCell(3).setCellValue((String) getOrDefault(record, "出库金额", "0.00"));
            row.createCell(4).setCellValue((String) getOrDefault(record, "在库金额", "0.00"));
        }
        for (int i = 0; i < 5; i++) { // 根据您的实际列数修改这里的数字
            sheet.autoSizeColumn(i);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        String filename = String.format("分类报表_%s_to_%s.xlsx", startDate, endDate);
        String encodedFilename = URLEncoder.encode(filename, "UTF-8").replace("+", "%20");
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename)
                .body(resource);
    }


    @GetMapping("/transfer")
    public ResponseEntity<ByteArrayResource> transferToExcel(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("depositoryId") int depositoryId) throws Exception {
        List<Map<String, Object>> data = reportService.transferData(startDate, endDate);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Report");
// 分割数据
        List<Map<String, Object>> zabToSabData = data.stream()
                .filter(record -> "ZAB转入SAB".equals(record.get("备注")))
                .collect(Collectors.toList());
        List<Map<String, Object>> sabToZabData = data.stream()
                .filter(record -> "SAB转入ZAB".equals(record.get("备注")))
                .collect(Collectors.toList());

// 创建 ZAB向SAB部分
        int currentRowIndex = 0;
        XSSFRow titleRowZabToSab = sheet.createRow(currentRowIndex++);
        titleRowZabToSab.createCell(0).setCellValue("SAB向ZAB借用物资清单");
//        currentRowIndex += 2; // 留空一行
        createHeaderRow(sheet, currentRowIndex++);
        currentRowIndex = fillDataRows(sheet, currentRowIndex, zabToSabData);
        currentRowIndex += 2; // 留空一行

// 创建 SAB向ZAB部分
        XSSFRow titleRowSabToZab = sheet.createRow(currentRowIndex++);
        titleRowSabToZab.createCell(0).setCellValue("ZAB向SAB借用物资清单");
//        currentRowIndex += 2; // 留空一行
        createHeaderRow(sheet, currentRowIndex++);
        fillDataRows(sheet, currentRowIndex, sabToZabData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        String filename = String.format("物品转移报表_%s_to_%s.xlsx", startDate, endDate);

        String encodedFilename = URLEncoder.encode(filename, "UTF-8").replace("+", "%20");
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename)
                .body(resource);
    }
    private void createHeaderRow(XSSFSheet sheet, int rowIndex) {
        XSSFRow headerRow = sheet.createRow(rowIndex);
        headerRow.createCell(0).setCellValue("日期");
        headerRow.createCell(1).setCellValue("分类");
        headerRow.createCell(2).setCellValue("品名");
        headerRow.createCell(3).setCellValue("型号");
        headerRow.createCell(4).setCellValue("单价");
        headerRow.createCell(5).setCellValue("数量");
        headerRow.createCell(6).setCellValue("总价");
        headerRow.createCell(7).setCellValue("备注");
    }

    private int fillDataRows(XSSFSheet sheet, int startingRowIndex, List<Map<String, Object>> data) {
        int rowIndex = startingRowIndex;
        for (Map<String, Object> record : data) {
            XSSFRow row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue((String) getOrDefault(record, "日期", ""));
            row.createCell(1).setCellValue((String) getOrDefault(record, "分类", "aaa"));
            row.createCell(2).setCellValue((String) getOrDefault(record, "品名", "aaa"));
            row.createCell(3).setCellValue((String) getOrDefault(record, "型号", "aaa"));
            row.createCell(4).setCellValue((String) getOrDefault(record, "单价", "0.00"));
            row.createCell(5).setCellValue(((Double) getOrDefault(record, "数量", 0.0)).doubleValue());
            row.createCell(6).setCellValue((String) getOrDefault(record, "总价", "0.00"));
            row.createCell(7).setCellValue((String) getOrDefault(record, "备注", "aaa"));
        }
        return rowIndex;
    }

    private Object getOrDefault(Map<String, Object> map, String key, Object defaultValue) {
        return map.containsKey(key) && map.get(key) != null ? map.get(key) : defaultValue;
    }

}
