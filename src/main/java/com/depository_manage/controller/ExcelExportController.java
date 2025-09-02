package com.depository_manage.controller;

import com.depository_manage.service.ReportService;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.util.CellRangeAddress;
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
        XSSFRow blankRow = sheet.createRow(0); // 顶部的空白行，用于合并单元格
        // 顶部空白行合并单元格设置
        createCellWithStyle(blankRow, 0, "", style); // 空白单元格占位
        createCellWithStyle(blankRow, 1, "", style); // 空白单元格占位
        createCellWithStyle(blankRow, 2, "", style); // 空白单元格占位
        createCellWithStyle(blankRow, 3, "", style); // 空白单元格占位
        createCellWithStyle(blankRow, 4, "新刀内制入库", style); // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 5));
        createCellWithStyle(blankRow, 6, "采购入库", style); // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 7));
        createCellWithStyle(blankRow, 8, "正常出库", style); // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 9));
        createCellWithStyle(blankRow, 10, "转移出库", style); // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 10, 11));
        createCellWithStyle(blankRow, 12, "销售出库", style); // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 12, 13));
        createCellWithStyle(blankRow, 14, "新刀制作室出库", style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 14, 15));
// 16-17：库存数量、在库金额
        createCellWithStyle(blankRow, 16, "", style);
        createCellWithStyle(blankRow, 17, "", style);
        XSSFRow headerRow = sheet.createRow(1); // 将原来第 0 行的表头移动到第 1 行
        createCellWithStyle(headerRow, 0, "分类", style);
        createCellWithStyle(headerRow, 1, "AT号", style);
        createCellWithStyle(headerRow, 2, "品名", style);
        createCellWithStyle(headerRow, 3, "规格", style);
        createCellWithStyle(headerRow, 4, "内制入库数量", style);
        createCellWithStyle(headerRow, 5, "内制入库金额", style);
        createCellWithStyle(headerRow, 6, "入库数量", style);
        createCellWithStyle(headerRow, 7, "入库金额", style);
        createCellWithStyle(headerRow, 8, "出库数量", style);
        createCellWithStyle(headerRow, 9, "出库金额", style);
        // 新增的列放在出库金额之后
        createCellWithStyle(headerRow, 10, "转移数量", style);
        createCellWithStyle(headerRow, 11, "转移金额", style);
        createCellWithStyle(headerRow, 12, "销售数量", style);
        createCellWithStyle(headerRow, 13, "销售金额", style);
        createCellWithStyle(headerRow, 14, "制作室出库数量", style);
        createCellWithStyle(headerRow, 15, "制作室出库金额", style);

        createCellWithStyle(headerRow, 16, "库存数量", style);
        createCellWithStyle(headerRow, 17, "在库金额", style);

// 第二张表：分类小计表
        XSSFRow subtotalBlankRow = subtotalSheet.createRow(0); // 顶部的空白行，用于合并单元格
// 顶部空白行合并单元格设置
        createCellWithStyle(subtotalBlankRow, 0, "", style); // 空白单元格占位
        createCellWithStyle(subtotalBlankRow, 1, "新刀内制入库", style);
        subtotalSheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
        createCellWithStyle(subtotalBlankRow, 3, "采购入库", style);
        subtotalSheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
        createCellWithStyle(subtotalBlankRow, 5, "正常出库", style);
        subtotalSheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));
        createCellWithStyle(subtotalBlankRow, 7, "转移出库", style);
        subtotalSheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));
        createCellWithStyle(subtotalBlankRow, 9, "销售出库", style);
        subtotalSheet.addMergedRegion(new CellRangeAddress(0, 0, 9, 10));
        createCellWithStyle(subtotalBlankRow, 11, "新刀制作室出库", style);
        subtotalSheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 12));
        createCellWithStyle(subtotalBlankRow, 13, "", style); // 库存数量
        createCellWithStyle(subtotalBlankRow, 14, "", style); // 在库金额
// 创建分类小计表的具体列名表头
        XSSFRow subtotalHeaderRow = subtotalSheet.createRow(1);
        createCellWithStyle(subtotalHeaderRow, 0, "分类", style);

        createCellWithStyle(subtotalHeaderRow, 1, "内制入库数量", style);
        createCellWithStyle(subtotalHeaderRow, 2, "内制入库金额", style);

        createCellWithStyle(subtotalHeaderRow, 3, "入库数量", style);
        createCellWithStyle(subtotalHeaderRow, 4, "入库金额", style);

        createCellWithStyle(subtotalHeaderRow, 5, "出库数量", style);
        createCellWithStyle(subtotalHeaderRow, 6, "出库金额", style);

        createCellWithStyle(subtotalHeaderRow, 7, "转移数量", style);
        createCellWithStyle(subtotalHeaderRow, 8, "转移金额", style);

        createCellWithStyle(subtotalHeaderRow, 9, "销售数量", style);
        createCellWithStyle(subtotalHeaderRow, 10, "销售金额", style);

        createCellWithStyle(subtotalHeaderRow, 11, "制作室出库数量", style);
        createCellWithStyle(subtotalHeaderRow, 12, "制作室出库金额", style);

        createCellWithStyle(subtotalHeaderRow, 13, "库存数量", style);
        createCellWithStyle(subtotalHeaderRow, 14, "在库金额", style);


        double totalInAmount = 0.0, totalOutAmount = 0.0, totalStockAmount = 0.0;
        double totalInQty = 0.0, totalOutQty = 0.0, totalStockQty = 0.0;
        // 下面是转移数量、转移金额、销售数量、销售金额
        double totalTransferQty = 0.0, totalTransferAmount = 0.0;
        double totalSalesQty = 0.0, totalSalesAmount = 0.0;
        double totalInnerInQty = 0.0, totalInnerInAmount = 0.0;
        double totalNewKnifeQty = 0.0, totalNewKnifeAmount = 0.0;

        String previousCategory = "";
        int rowIndex = 2;
        for (Map<String, Object> record : data) {
            // 如果当前记录没有分类，则将其标记为"未分类"
            String currentCategory = (String) getOrDefault(record, "分类", "未分类");
            // 检查当前分类是否已更改，表示一个新分类的开始
            if (!currentCategory.equals(previousCategory) && rowIndex != 2) {
                // 在分类小计表中添加前一个分类的小计行
//                addSubtotalRow(subtotalSheet, previousCategory.isEmpty() ? "未分类" : previousCategory, totalInQty, totalOutQty, totalStockQty, totalInAmount, totalOutAmount, totalStockAmount, style);
                addSubtotalRow(
                        subtotalSheet,
                        previousCategory.isEmpty() ? "未分类" : previousCategory,
                        totalInnerInQty, totalInnerInAmount,
                        totalInQty, totalInAmount,
                        totalOutQty, totalOutAmount,
                        totalTransferQty, totalTransferAmount,
                        totalSalesQty, totalSalesAmount,
                        totalNewKnifeQty, totalNewKnifeAmount,
                        totalStockQty, totalStockAmount,
                        style
                );
                // 在主数据表中添加空白行
                int blankRowIndex = rowIndex++;
                sheet.createRow(blankRowIndex);
                // 重置累计变量
                totalInQty = totalOutQty = totalTransferQty = totalSalesQty = totalStockQty = 0.0;
                totalInAmount = totalOutAmount = totalTransferAmount = totalSalesAmount = totalStockAmount = 0.0;
                totalInnerInQty = totalInnerInAmount = 0.0;
                totalNewKnifeQty = totalNewKnifeAmount = 0.0;
            }

            XSSFRow row = sheet.createRow(rowIndex);

            // 如果是新分类的开始并且分类名称是空的，则在这一行设置分类名称为"未分类"
            createCellWithStyle(row, 0, currentCategory.equals(previousCategory) ? "" : currentCategory, style);
            createCellWithStyle(row, 1, ((Integer) getOrDefault(record, "AT号", 0)).intValue(), style);
            createCellWithStyle(row, 2, (String) getOrDefault(record, "品名", ""), style);
            createCellWithStyle(row, 3, (String) getOrDefault(record, "规格", ""), style);

            createCellWithStyle(row, 4, ((Double) getOrDefault(record, "内制入库数量", 0.0)).doubleValue(), style);
            createCellWithStyle(row, 5, Double.parseDouble(((String) getOrDefault(record, "内制入库金额", "0.00")).replace(",", "")), style);

            createCellWithStyle(row, 6, ((Double) getOrDefault(record, "入库数量", 0.0)).doubleValue(), style);
            createCellWithStyle(row, 7, Double.parseDouble(((String) getOrDefault(record, "入库金额", "0.00")).replace(",", "")), style);

            createCellWithStyle(row, 8,
                    Double.parseDouble(
                            String.valueOf(getOrDefault(record, "出库数量", "0.00")).replace(",", "")
                    ),
                    style
            );
            createCellWithStyle(row, 9, Double.parseDouble(((String) getOrDefault(record, "出库金额", "0.00")).replace(",", "")), style);

            createCellWithStyle(row, 10, ((Double) getOrDefault(record, "转移数量", 0.0)).doubleValue(), style);
            createCellWithStyle(row, 11, Double.parseDouble(((String) getOrDefault(record, "转移金额", "0.00")).replace(",", "")), style);

            createCellWithStyle(row, 12, ((Double) getOrDefault(record, "销售数量", 0.0)).doubleValue(), style);
            createCellWithStyle(row, 13, Double.parseDouble(((String) getOrDefault(record, "销售金额", "0.00")).replace(",", "")), style);

            createCellWithStyle(row, 14, ((Double) getOrDefault(record, "制作室出库数量", 0.0)).doubleValue(), style);
            createCellWithStyle(row, 15, Double.parseDouble(((String) getOrDefault(record, "制作室出库金额", "0.00")).replace(",", "")), style);

            createCellWithStyle(row, 16, Double.parseDouble(
                            String.valueOf(getOrDefault(record, "库存数量", "0.00")).replace(",", ""))
                    , style);
            createCellWithStyle(row, 17, Double.parseDouble(((String) getOrDefault(record, "在库金额", "0.00")).replace(",", "")), style);


            // Accumulate totals
            totalInQty += parseToDouble(getOrDefault(record, "入库数量", "0.0").toString());
            totalOutQty += parseToDouble(getOrDefault(record, "出库数量", "0.0").toString());
            totalStockQty += parseToDouble(getOrDefault(record, "库存数量", "0.0").toString());
            totalInAmount += parseToDouble(getOrDefault(record, "入库金额", "0.0").toString());
            totalOutAmount += parseToDouble(getOrDefault(record, "出库金额", "0.0").toString());
            totalStockAmount += parseToDouble(getOrDefault(record, "在库金额", "0.0").toString());
            totalTransferQty += parseToDouble(getOrDefault(record, "转移数量", "0.0").toString());
            totalTransferAmount += parseToDouble(getOrDefault(record, "转移金额", "0.0").toString());
            totalSalesQty += parseToDouble(getOrDefault(record, "销售数量", "0.0").toString());
            totalSalesAmount += parseToDouble(getOrDefault(record, "销售金额", "0.0").toString());

            totalInnerInQty += parseToDouble(getOrDefault(record, "内制入库数量", "0.0").toString());
            totalInnerInAmount += parseToDouble(getOrDefault(record, "内制入库金额", "0.0").toString());

            totalNewKnifeQty += parseToDouble(getOrDefault(record, "制作室出库数量", "0.0").toString());
            totalNewKnifeAmount += parseToDouble(getOrDefault(record, "制作室出库金额", "0.0").toString());

            rowIndex++;
            previousCategory = currentCategory;
        }

        if (!previousCategory.isEmpty() || rowIndex == 2) { // rowIndex == 1 表示从未添加过小计行，即所有数据都没有分类
            // 如果前一个分类为空，表示数据未分类
//            addSubtotalRow(subtotalSheet, previousCategory, totalInQty, totalOutQty, totalStockQty, totalInAmount, totalOutAmount, totalStockAmount, style);
            addSubtotalRow(
                    subtotalSheet,
                    previousCategory.isEmpty() ? "未分类" : previousCategory,
                    totalInnerInQty, totalInnerInAmount,
                    totalInQty, totalInAmount,
                    totalOutQty, totalOutAmount,
                    totalTransferQty, totalTransferAmount,
                    totalSalesQty, totalSalesAmount,
                    totalNewKnifeQty, totalNewKnifeAmount,
                    totalStockQty, totalStockAmount,
                    style
            );
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
    @GetMapping("/reportDz")
    public ResponseEntity<ByteArrayResource> exportReportDzToExcel(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("depositoryId") int depositoryId) throws Exception {
        List<Map<String, Object>> data = reportService.fetchReportDzData(startDate, endDate, depositoryId);
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
        XSSFRow blankRow = sheet.createRow(0); // 顶部的空白行，用于合并单元格
        // 顶部空白行合并单元格设置
        createCellWithStyle(blankRow, 0, "", style); // 空白单元格占位
        createCellWithStyle(blankRow, 1, "", style); // 空白单元格占位
        createCellWithStyle(blankRow, 2, "", style); // 空白单元格占位
        createCellWithStyle(blankRow, 3, "", style); // 空白单元格占位
        createCellWithStyle(blankRow, 4, "", style); // 空白单元格占位
        createCellWithStyle(blankRow, 5, "", style); // 空白单元格占位
        createCellWithStyle(blankRow, 6, "正常出库", style); // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 7));
        createCellWithStyle(blankRow, 8, "转移出库", style); // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 9));
        createCellWithStyle(blankRow, 10, "销售出库", style); // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 10, 11));
        createCellWithStyle(blankRow, 12, "", style); // 空白单元格占位
        createCellWithStyle(blankRow, 13, "", style); // 空白单元格占位
        createCellWithStyle(blankRow, 14, "", style); // 空白单元格占位
        XSSFRow headerRow = sheet.createRow(1); // 将原来第 0 行的表头移动到第 1 行
        createCellWithStyle(headerRow, 0, "呆滞仓分类", style);
        createCellWithStyle(headerRow, 1, "AT号", style);
        createCellWithStyle(headerRow, 2, "品名", style);
        createCellWithStyle(headerRow, 3, "规格", style);
        createCellWithStyle(headerRow, 4, "入库数量", style);
        createCellWithStyle(headerRow, 5, "入库金额", style);
        createCellWithStyle(headerRow, 6, "出库数量", style);
        createCellWithStyle(headerRow, 7, "出库金额", style);
        // 新增的列放在出库金额之后
        createCellWithStyle(headerRow, 8, "转移数量", style);
        createCellWithStyle(headerRow, 9, "转移金额", style);
        createCellWithStyle(headerRow, 10, "销售数量", style);
        createCellWithStyle(headerRow, 11, "销售金额", style);
        // 库存数量和在库金额
        createCellWithStyle(headerRow, 12, "库存数量", style);
        createCellWithStyle(headerRow, 13, "在库金额", style);
        createCellWithStyle(headerRow, 14, "转入呆滞仓时间", style); // 新添加的列

// 第二张表：分类小计表
        XSSFRow subtotalBlankRow = subtotalSheet.createRow(0); // 顶部的空白行，用于合并单元格
// 顶部空白行合并单元格设置
        createCellWithStyle(subtotalBlankRow, 0, "", style); // 空白单元格占位
        createCellWithStyle(subtotalBlankRow, 1, "", style); // 空白单元格占位
        createCellWithStyle(subtotalBlankRow, 2, "", style); // 空白单元格占位
        createCellWithStyle(subtotalBlankRow, 3, "正常出库", style); // 合并单元格
        subtotalSheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
        createCellWithStyle(subtotalBlankRow, 5, "转移出库", style); // 合并单元格
        subtotalSheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));
        createCellWithStyle(subtotalBlankRow, 7, "销售出库", style); // 合并单元格
        subtotalSheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));
        createCellWithStyle(subtotalBlankRow, 9, "", style); // 空白单元格占位
        createCellWithStyle(subtotalBlankRow, 10, "", style); // 空白单元格占位

// 创建分类小计表的具体列名表头
        XSSFRow subtotalHeaderRow = subtotalSheet.createRow(1);
        createCellWithStyle(subtotalHeaderRow, 0, "呆滞仓分类", style);
        createCellWithStyle(subtotalHeaderRow, 1, "入库数量", style);
        createCellWithStyle(subtotalHeaderRow, 2, "入库金额", style);
        createCellWithStyle(subtotalHeaderRow, 3, "出库数量", style);
        createCellWithStyle(subtotalHeaderRow, 4, "出库金额", style);
        createCellWithStyle(subtotalHeaderRow, 5, "转移数量", style);
        createCellWithStyle(subtotalHeaderRow, 6, "转移金额", style);
        createCellWithStyle(subtotalHeaderRow, 7, "销售数量", style);
        createCellWithStyle(subtotalHeaderRow, 8, "销售金额", style);
        createCellWithStyle(subtotalHeaderRow, 9, "库存数量", style);
        createCellWithStyle(subtotalHeaderRow, 10, "在库金额", style);

        double totalInAmount = 0.0, totalOutAmount = 0.0, totalStockAmount = 0.0;
        double totalInQty = 0.0, totalOutQty = 0.0, totalStockQty = 0.0;
        // 下面是转移数量、转移金额、销售数量、销售金额
        double totalTransferQty = 0.0, totalTransferAmount = 0.0;
        double totalSalesQty = 0.0, totalSalesAmount = 0.0;
        String previousCategory = "";
        int rowIndex = 2;

        for (Map<String, Object> record : data) {
            // 如果当前记录没有分类，则将其标记为"未分类"
            String currentCategory = (String) getOrDefault(record, "呆滞仓分类", "未分类");

            // 检查当前分类是否已更改，表示一个新分类的开始
            if (!currentCategory.equals(previousCategory) && rowIndex != 2) {
                // 在分类小计表中添加前一个分类的小计行
//                addSubtotalRow(subtotalSheet, previousCategory.isEmpty() ? "未分类" : previousCategory, totalInQty, totalOutQty, totalStockQty, totalInAmount, totalOutAmount, totalStockAmount, style);
                addSubtotalRow(subtotalSheet, previousCategory.isEmpty() ? "未分类" : previousCategory, totalInQty, totalOutQty, totalTransferQty, totalSalesQty, totalInAmount, totalOutAmount, totalTransferAmount, totalSalesAmount, totalStockQty, totalStockAmount, style);
                // 在主数据表中添加空白行
                int blankRowIndex = rowIndex++;
                sheet.createRow(blankRowIndex);

                // 重置累计变量
                totalInQty = totalOutQty = totalTransferQty = totalSalesQty = totalStockQty = 0.0;
                totalInAmount = totalOutAmount = totalTransferAmount = totalSalesAmount = totalStockAmount = 0.0;
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
            createCellWithStyle(row, 5, Double.parseDouble(((String) getOrDefault(record, "入库金额", "0.00")).replace(",", "")), style);
            createCellWithStyle(row, 6 , ((Double) getOrDefault(record, "出库数量", 0.0)).doubleValue(), style);
            createCellWithStyle(row, 7, Double.parseDouble(((String) getOrDefault(record, "出库金额", "0.00")).replace(",", "")), style);
            createCellWithStyle(row, 8, ((Double) getOrDefault(record, "转移数量", 0.0)).doubleValue(), style);
            createCellWithStyle(row, 9, Double.parseDouble(((String) getOrDefault(record, "转移金额", "0.00")).replace(",", "")), style);
            createCellWithStyle(row, 10, ((Double) getOrDefault(record, "销售数量", 0.0)).doubleValue(), style);
            createCellWithStyle(row, 11, Double.parseDouble(((String) getOrDefault(record, "销售金额", "0.00")).replace(",", "")), style);
            // 然后是库存数量和在库金额的填充
            createCellWithStyle(row, 12, ((Double) getOrDefault(record, "库存数量", 0.0)).doubleValue(), style);
            createCellWithStyle(row, 13, Double.parseDouble(((String) getOrDefault(record, "在库金额", "0.00")).replace(",", "")), style);
            createCellWithStyle(row, 14, "2023-12-30", style); // 填入固定的日期值


            // Accumulate totals
            totalInQty += parseToDouble(getOrDefault(record, "入库数量", "0.0").toString());
            totalOutQty += parseToDouble(getOrDefault(record, "出库数量", "0.0").toString());
            totalStockQty += parseToDouble(getOrDefault(record, "库存数量", "0.0").toString());
            totalInAmount += parseToDouble(getOrDefault(record, "入库金额", "0.0").toString());
            totalOutAmount += parseToDouble(getOrDefault(record, "出库金额", "0.0").toString());
            totalStockAmount += parseToDouble(getOrDefault(record, "在库金额", "0.0").toString());
            totalTransferQty += parseToDouble(getOrDefault(record, "转移数量", "0.0").toString());
            totalTransferAmount += parseToDouble(getOrDefault(record, "转移金额", "0.0").toString());
            totalSalesQty += parseToDouble(getOrDefault(record, "销售数量", "0.0").toString());
            totalSalesAmount += parseToDouble(getOrDefault(record, "销售金额", "0.0").toString());
            rowIndex++;
            previousCategory = currentCategory;
        }

        if (!previousCategory.isEmpty() || rowIndex == 2) { // rowIndex == 1 表示从未添加过小计行，即所有数据都没有分类
            // 如果前一个分类为空，表示数据未分类
            previousCategory = previousCategory.isEmpty() ? "未分类" : previousCategory;
//            addSubtotalRow(subtotalSheet, previousCategory, totalInQty, totalOutQty, totalStockQty, totalInAmount, totalOutAmount, totalStockAmount, style);
            addSubtotalRow(subtotalSheet, previousCategory, totalInQty, totalOutQty, totalTransferQty, totalSalesQty, totalInAmount, totalOutAmount, totalTransferAmount, totalSalesAmount, totalStockQty, totalStockAmount, style);
            // 在主数据表中添加最后的空白行
            sheet.createRow(rowIndex);

        }
        subtotalSheet.autoSizeColumn(0); // 第一列的索引是0

        // Add final total row
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        String filename = "呆滞仓物品库存报表_" + startDate + "_to_" + endDate + ".xlsx";
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
    private void addSubtotalRow(XSSFSheet subtotalSheet, String categoryName,
                                double inQty, double outQty,
                                double transferQty, double salesQty,
                                double inAmount, double outAmount,
                                double transferAmount, double salesAmount,
                                double stockQty, double stockAmount,
                                XSSFCellStyle style) {
        int rowIndex = subtotalSheet.getLastRowNum() + 1; // 获取下一个空行的行索引
        XSSFRow totalRow = subtotalSheet.createRow(rowIndex);
        createCellWithStyle(totalRow, 0, categoryName, style);   // 分类名称
        createCellWithStyle(totalRow, 1, inQty, style);          // 入库数量
        createCellWithStyle(totalRow, 2, inAmount, style);       // 入库金额
        createCellWithStyle(totalRow, 3, outQty, style);         // 出库数量
        createCellWithStyle(totalRow, 4, outAmount, style);      // 出库金额
        createCellWithStyle(totalRow, 5, transferQty, style);    // 转移数量
        createCellWithStyle(totalRow, 6, transferAmount, style); // 转移金额
        createCellWithStyle(totalRow, 7, salesQty, style);       // 销售数量
        createCellWithStyle(totalRow, 8, salesAmount, style);    // 销售金额
        createCellWithStyle(totalRow, 9, stockQty, style);       // 库存数量
        createCellWithStyle(totalRow, 10, stockAmount, style);   // 在库金额
    }
    private void addSubtotalRow(XSSFSheet subtotalSheet, String categoryName,
                                double totalInnerInQty, double totalInnerInAmount,
                                double inQty, double inAmount,
                                double outQty, double outAmount,
                                double transferQty, double transferAmount,
                                double salesQty, double salesAmount,
                                double totalNewKnifeQty, double totalNewKnifeAmount,
                                double stockQty, double stockAmount,
                                XSSFCellStyle style) {
        int rowIndex = subtotalSheet.getLastRowNum() + 1; // 获取下一个空行的行索引
        XSSFRow totalRow = subtotalSheet.createRow(rowIndex);
        int col = 0;
        createCellWithStyle(totalRow, col++, categoryName, style);
        createCellWithStyle(totalRow, col++, totalInnerInQty, style);
        createCellWithStyle(totalRow, col++, totalInnerInAmount, style);
        createCellWithStyle(totalRow, col++, inQty, style);
        createCellWithStyle(totalRow, col++, inAmount, style);
        createCellWithStyle(totalRow, col++, outQty, style);
        createCellWithStyle(totalRow, col++, outAmount, style);
        createCellWithStyle(totalRow, col++, transferQty, style);
        createCellWithStyle(totalRow, col++, transferAmount, style);
        createCellWithStyle(totalRow, col++, salesQty, style);
        createCellWithStyle(totalRow, col++, salesAmount, style);
        createCellWithStyle(totalRow, col++, totalNewKnifeQty, style);
        createCellWithStyle(totalRow, col++, totalNewKnifeAmount, style);
        createCellWithStyle(totalRow, col++, stockQty, style);
        createCellWithStyle(totalRow, col++, stockAmount, style);

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

        // 创建表头行
        XSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("日期");
        headerRow.createCell(1).setCellValue("分类");
        headerRow.createCell(2).setCellValue("入库金额");
        headerRow.createCell(3).setCellValue("正常出库金额");
        headerRow.createCell(4).setCellValue("转移出库金额");
        headerRow.createCell(5).setCellValue("销售出库金额");
        headerRow.createCell(6).setCellValue("在库金额");

        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> record = data.get(i);
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue((String) getOrDefault(record, "日期", ""));
            row.createCell(1).setCellValue((String) getOrDefault(record, "分类", ""));
            row.createCell(2).setCellValue((String) getOrDefault(record, "入库金额", "0.00"));
            row.createCell(3).setCellValue((String) getOrDefault(record, "正常出库金额", "0.00"));
            row.createCell(4).setCellValue((String) getOrDefault(record, "转移出库金额", "0.00"));
            row.createCell(5).setCellValue((String) getOrDefault(record, "销售出库金额", "0.00"));
            row.createCell(6).setCellValue((String) getOrDefault(record, "在库金额", "0.00"));
        }

        for (int i = 0; i <= 6; i++) {
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
