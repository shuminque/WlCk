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

    @GetMapping("/report")
    public List<Map<String, Object>> getReportData(
            @RequestParam(name = "year", required = false, defaultValue = "-1") int year,
            @RequestParam(name = "month", required = false, defaultValue = "-1") int month,
            @RequestParam(name = "depositoryId") int depositoryId) {
        return fetchData(year, month, date -> reportService.fetchReportData(date[0], date[1], depositoryId));
    }

    @GetMapping("/every")
    public List<Map<String, Object>> everyTypeData(
            @RequestParam(name = "year", required = false, defaultValue = "-1") int year,
            @RequestParam(name = "month", required = false, defaultValue = "-1") int month,
            @RequestParam(name = "depositoryId") int depositoryId) {
        return fetchData(year, month, date -> reportService.everyTypeData(date[0], date[1], depositoryId));
    }

    @GetMapping("/transfer")
    public List<Map<String, Object>> transferData(
            @RequestParam(name = "year", required = false, defaultValue = "-1") int year,
            @RequestParam(name = "month", required = false, defaultValue = "-1") int month) {
        return fetchData(year, month, date -> reportService.transferData(date[0], date[1]));
    }

    private List<Map<String, Object>> fetchData(int year, int month, Function<int[], List<Map<String, Object>>> fetcher) {
        int[] currentYearMonth = setCurrentYearAndMonth(year, month);
        return fetcher.apply(currentYearMonth);
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
