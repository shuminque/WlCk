package com.dreamchaser.depository_manage.controller;
import com.dreamchaser.depository_manage.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public List<Map<String, Object>> getReportData(@RequestParam int depositoryId) {
        return reportService.fetchReportData(depositoryId);
    }
}

