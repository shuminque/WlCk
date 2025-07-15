package com.depository_manage.controller;

import com.depository_manage.pojo.RestResponse;
import com.depository_manage.service.SeriesService;
import com.depository_manage.utils.CrudUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @PostMapping("/series")
    public ResponseEntity<RestResponse> insertSeries(@RequestBody Map<String, Object> map) {
        try {
            RestResponse response = CrudUtil.postHandle(seriesService.insertSeries(map), 1);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RestResponse("添加错误，检查属性", 500, null));
        }
    }

    @GetMapping("/series")
    public RestResponse findSeries(@RequestParam Map<String, Object> map) {
        return new RestResponse(
                seriesService.findSeriesAll(),
                seriesService.findCountByCondition(map),
                200
        );
    }

    @DeleteMapping("/series/{id}")
    public RestResponse deleteSeries(@PathVariable int id) {
        int result = seriesService.deleteSeries(id);
        return CrudUtil.deleteHandle(result, 1);
    }

    @PutMapping("/series/{id}")
    public RestResponse updateSeries(@PathVariable int id, @RequestBody Map<String, Object> map) {
        map.put("id", id); // 保证 ID 被传入
        int result = seriesService.updateSeries(map);
        return CrudUtil.putHandle(result, 1);
    }
}
