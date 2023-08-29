package com.dreamchaser.depository_manage.controller;

import com.dreamchaser.depository_manage.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RestController
@RequestMapping("/api")
public class ReportController {

    @Autowired
    private ReportService reportService;

//    @GetMapping("/report")
//    public List<Map<String, Object>> getReportData(
//            @RequestParam(name = "year", required = false, defaultValue = "-1") int year,
//            @RequestParam(name = "month", required = false, defaultValue = "-1") int month,
//            @RequestParam(name = "depositoryId") int depositoryId) {
//        return fetchData(year, month, date -> reportService.fetchReportData(date[0], date[1], depositoryId));
//    }
@GetMapping("/report")
public List<Map<String, Object>> getReportData(
        @RequestParam(name = "startDate") String startDate,
        @RequestParam(name = "endDate") String endDate,
        @RequestParam(name = "depositoryId") int depositoryId) {

    // 根据您的服务方法的定义，您可能需要将字符串日期转换为特定的日期格式
    // 如LocalDate，Date等。在此示例中，我将其保持为字符串。

    return reportService.fetchReportData(startDate, endDate, depositoryId);
}

    @GetMapping("/every")
    public List<Map<String, Object>> everyTypeData(
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate,
            @RequestParam(name = "depositoryId") int depositoryId) {
        return fetchData(startDate, endDate, date -> reportService.everyTypeData(date[0], date[1], depositoryId));
    }


    @GetMapping("/transfer")
    public List<Map<String, Object>> transferData(
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate) {
        return fetchData(startDate, endDate, date -> reportService.transferData(date[0], date[1]));
    }


    public List<Map<String, Object>> fetchData(String startDate, String endDate,
                                               Function<String[], List<Map<String, Object>>> dataFetcher) {
        return dataFetcher.apply(new String[]{startDate, endDate});
    }


    private int[] setCurrentYearAndMonth(int year, int month) {
        if (year == -1) {
            year = LocalDate.now().getYear();
        }
        if (month == -1) {
            month = LocalDate.now().getMonthValue();
        }
        return new int[]{year, month};
    }
}
