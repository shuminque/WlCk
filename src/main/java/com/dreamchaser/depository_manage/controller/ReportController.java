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
        if (year == -1) {
            year = LocalDate.now().getYear();
        }
        if (month == -1) {
            month = LocalDate.now().getMonthValue();
        }
        return reportService.fetchReportData( year,  month,  depositoryId);
    }
    @GetMapping("/every")
    public List<Map<String, Object>> everyTypeData(
            @RequestParam(name = "year", required = false, defaultValue = "-1") int year,
            @RequestParam(name = "month", required = false, defaultValue = "-1") int month,
            @RequestParam(name = "depositoryId") int depositoryId) {
        if (year == -1) {
            year = LocalDate.now().getYear();
        }
        if (month == -1) {
            month = LocalDate.now().getMonthValue();
        }
        return reportService.everyTypeData(year, month , depositoryId);
    }
}

