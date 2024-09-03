package com.depository_manage.controller;

import com.depository_manage.entity.LineData;
import com.depository_manage.pojo.LineDataP;
import com.depository_manage.pojo.RestResponse;
import com.depository_manage.service.LineDataService;
import com.depository_manage.utils.CrudUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lineData")
public class LineDataController {

    @Autowired
    private LineDataService lineDataService;

    @PostMapping("/create")
    public RestResponse insertLineData(@RequestBody Map<String, Object> map) {
        try {
            // 将所有空字符串转为 null
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if ("".equals(entry.getValue())) {
                    map.put(entry.getKey(), null);
                }
            }
            return CrudUtil.postHandle(lineDataService.insertLineData(map), 1);
        } catch (DataIntegrityViolationException e) {
            return new RestResponse("Insertion failed due to a unique constraint violation.", 409, null);
        } catch (RuntimeException e) {
            return new RestResponse("Insertion failed: " + e.getMessage(), 500, null);
        }
    }


    @GetMapping
    public RestResponse findLineData(@RequestParam Map<String, Object> map) {
        return new RestResponse(
                lineDataService.findLineDataPByCondition(map),
                lineDataService.findCountByCondition(map),
                200
        );
    }

    @DeleteMapping("/{id}")
    public RestResponse deleteLineData(@PathVariable Integer id) {
        int result = lineDataService.deleteLineData(id);
        return CrudUtil.deleteHandle(result, 1);
    }

    @PostMapping("/deleteBatch")
    public RestResponse deleteBatch(@RequestBody List<Integer> ids) {
        try {
            for (Integer id : ids) {
                lineDataService.deleteLineData(id);
            }
            return new RestResponse("删除成功", 200, null);
        } catch (Exception e) {
            return new RestResponse("删除失败：" + e.getMessage(), 500, null);
        }
    }


    @PutMapping("/{id}")
    public RestResponse updateLineData(@PathVariable Integer id, @RequestBody Map<String, Object> map) {
        map.put("id", id);
        return CrudUtil.putHandle(lineDataService.updateLineData(map), 1);
    }

    @GetMapping("/findById/{id}")
    public RestResponse findLineDataById(@PathVariable Integer id) {
        return new RestResponse(lineDataService.findLineDataById(id), 1, 200);
    }

    @GetMapping("/findByDate")
    public RestResponse findLineDataByDate(@RequestParam("date") String date) {
        // 这里可以解析字符串到日期类型，或者直接传入字符串（根据你的实际需求）
        List<LineData> data = lineDataService.findLineDataByDate(java.sql.Date.valueOf(date));
        return new RestResponse(data, data.size(), 200);
    }

    @PostMapping("/findByCondition")
    public RestResponse findLineDataByCondition(@RequestBody Map<String, Object> map) {
        List<LineDataP> data = lineDataService.findLineDataPByCondition(map);
        return new RestResponse(data, data.size(), 200);
    }

    @GetMapping("/findAll")
    public RestResponse findLineDataAll() {
        List<LineData> data = lineDataService.findLineDataAll();
        return new RestResponse(data, data.size(), 200);
    }
    @PostMapping("/import")
    public RestResponse importLineData(@RequestParam("file") MultipartFile file, @RequestParam("date") String date) {
        try {
            List<LineData> lineDataList = new ArrayList<>();
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0 || row.getRowNum() == 1) continue;  // 跳过标题行

                LineData lineData = new LineData();
                lineData.setDate(Date.valueOf(date));

                // 处理第一列的名称和型号
                Cell nameCell = row.getCell(0);
                if (nameCell == null) {
                    continue; // 如果名称单元格为空，跳过这一行
                }

                String nameAndModel = nameCell.getStringCellValue();
                String lineName = nameAndModel;
                String model = null;

                // 检查是否包含括号，分离名称和型号
                if ((nameAndModel.contains("（") && nameAndModel.contains("）")) ||
                        (nameAndModel.contains("(") && nameAndModel.contains(")"))) {

                    // 判断是中文括号还是英文括号，并提取内容
                    if (nameAndModel.contains("（") && nameAndModel.contains("）")) {
                        int startIndex = nameAndModel.indexOf("（");
                        int endIndex = nameAndModel.indexOf("）");
                        lineName = nameAndModel.substring(0, startIndex);
                        model = nameAndModel.substring(startIndex + 1, endIndex);
                    } else if (nameAndModel.contains("(") && nameAndModel.contains(")")) {
                        int startIndex = nameAndModel.indexOf("(");
                        int endIndex = nameAndModel.indexOf(")");
                        lineName = nameAndModel.substring(0, startIndex);
                        model = nameAndModel.substring(startIndex + 1, endIndex);
                    }
                }

                lineData.setLineName(lineName.trim());
                lineData.setModel(model != null ? model.trim() : null);

                // 处理第二列的产量
                Cell productionCell = row.getCell(1);
                if (productionCell == null) {
                    lineData.setProduction(0);  // 如果产量单元格为空，设置为0，或者你可以选择跳过这一行
                } else {
                    lineData.setProduction((int) productionCell.getNumericCellValue());
                }

                lineDataList.add(lineData);
            }

            lineDataService.batchInsertLineData(lineDataList);
            return new RestResponse("导入成功", 200, null);
        } catch (Exception e) {
            // 返回更加详细的错误信息，便于调试
            return new RestResponse("导入失败：" + e.getClass().getName() + ": " + e.getMessage(), 500, null);
        }
    }
    @GetMapping("/{year}/{month}")
    public RestResponse getLineDataForMonth(
            @PathVariable("year") int year,
            @PathVariable("month") int month) {
        try {
            // 根据年月和厂区ID获取对应的lineData
            List<LineData> lineDataList = lineDataService.findLineDataByMonth(year, month);
            return new RestResponse(lineDataList, lineDataList.size(), 200);
        } catch (Exception e) {
            return new RestResponse("Failed to fetch line data: " + e.getMessage(), 500, null);
        }
    }

    @GetMapping("/data/{year}/{lineName}")
    public RestResponse getLineNameData(
            @PathVariable("year") int year,
            @PathVariable("lineName") String lineName) throws UnsupportedEncodingException{
            try {
            // 根据年月和厂区ID获取对应的lineData
                lineName = URLDecoder.decode(lineName, StandardCharsets.UTF_8.name());
                List<LineData> lineDataList = lineDataService.findLineNameData(year, lineName);
            return new RestResponse(lineDataList, lineDataList.size(), 200);
        } catch (Exception e) {
            return new RestResponse("Failed to fetch line data: " + e.getMessage(), 500, null);
        }
    }
}
