package com.depository_manage.controller;

import com.depository_manage.pojo.RestResponse;
import com.depository_manage.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.lang3.tuple.Pair;

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
    public RestResponse getReportData(
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate,
            @RequestParam(name = "depositoryId") int depositoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pair<List<Map<String, Object>>, Integer> result = reportService.fetchReportData(startDate, endDate, depositoryId, page, size);
        List<Map<String, Object>> data = result.getLeft();
        int count = result.getRight();
        RestResponse response = new RestResponse();
        response.setStatus(200);
        response.setData(data);
        response.setCount(count);
        return response;
    }


    @GetMapping("/reportDz")
    public List<Map<String, Object>> getReportDz(
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate,
            @RequestParam(name = "depositoryId") int depositoryId) {
        return reportService.fetchReportDzData(startDate, endDate, depositoryId);
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
