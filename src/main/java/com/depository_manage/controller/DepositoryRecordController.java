package com.depository_manage.controller;

import com.depository_manage.entity.DepositoryRecord;
import com.depository_manage.entity.Material;
import com.depository_manage.exception.MyException;
import com.depository_manage.mapper.DepositoryRecordMapper;
import com.depository_manage.mapper.MaterialMapper;
import com.depository_manage.pojo.CategoryOutboundDTO;
import com.depository_manage.pojo.DepositoryRecordP;
import com.depository_manage.pojo.MonthlyAmountDTO;
import com.depository_manage.pojo.RestResponse;
import com.depository_manage.security.bean.UserToken;
import com.depository_manage.service.DepositoryRecordService;
import com.depository_manage.utils.CrudUtil;
import com.depository_manage.utils.ObjectFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仓库记录
 * @author Dreamchaser
 */
@RestController
public class DepositoryRecordController {
    @Autowired
    private DepositoryRecordService depositoryRecordService;
    @Autowired
    private DepositoryRecordMapper depositoryRecordMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @GetMapping("/get_record")
    public DepositoryRecordP getDepositoryRecordById(@RequestParam Integer id) {
        return depositoryRecordService.findDepositoryRecordById(id);
    }
    @GetMapping("/depositoryRecord")
    public RestResponse findDepositoryRecordByCondition(@RequestParam Map<String,Object> map,
                                                        @RequestParam(name = "typeName[]", required = false) List<String> typeNames,
                                                        @RequestParam(name = "applyRemark[]", required = false) List<String> applyRemarks){
        if (typeNames != null && !typeNames.isEmpty()) {
            map.put("typeName", typeNames);
        }
        if (applyRemarks != null && !applyRemarks.isEmpty()) {
            map.put("applyRemark", applyRemarks);
        }
        String dateRange = (String) map.get("applyTime");
        if (dateRange !=null && dateRange.contains(" - ")){
            String[] dates = dateRange.split(" - ");
            map.put("startDate", dates[0] + " 00:00:00");
            map.put("endDate", dates[1] + " 23:59:59");
        }
        List<DepositoryRecordP> list=depositoryRecordService.findDepositoryRecordPByCondition(map);
        return new RestResponse(list,depositoryRecordService.findCountByCondition(map),200);
    }
    //方法处理GET请求到/depositoryRecord的路径。这个方法接收一组查询参数，然后调用DepositoryRecordService.findDepositoryRecordPByCondition方法来查询满足条件的仓库记录。
    @GetMapping("/myApply")
    public RestResponse findDepositoryRecordByCondition(@RequestParam Map<String,Object> map,HttpServletRequest request){
        UserToken userToken= (UserToken) request.getAttribute("userToken");
        map.put("applicantId",userToken.getUser().getId());
        List<DepositoryRecordP> list=depositoryRecordService.findDepositoryRecordPByCondition(map);
        return new RestResponse(list,depositoryRecordService.findCountByCondition(map),200);
    }
    @GetMapping("/myTask")
    public RestResponse myTask(@RequestParam Map<String,Object> map, HttpServletRequest request) {
        UserToken userToken = (UserToken) request.getAttribute("userToken");
        Integer reviewGroupId = userToken.getUser().getReview_group_id();

        map.put("userId", userToken.getUser().getId());
        map.put("reviewGroup", reviewGroupId);
        String dateRange = (String) map.get("applyTime");
        if (dateRange != null && dateRange.contains(" - ")) {
            String[] dates = dateRange.split(" - ");
            map.put("startDate", dates[0] + " 00:00:00");
            map.put("endDate", dates[1] + " 23:59:59");
        }
        return new RestResponse(depositoryRecordService.findMyTask(map),
                depositoryRecordService.findMyTaskCount(map), 200);
    }

    @PostMapping("/depositoryRecord")
    public RestResponse insertDepositoryRecord(@RequestBody Map<String,Object> map, HttpServletRequest request){
        UserToken userToken= (UserToken) request.getAttribute("userToken");
        map.put("applicantId",userToken.getUser().getId());
        return CrudUtil.postHandle(depositoryRecordService.apply(map),1);
    }
    @PutMapping("/depositoryRecord/{id}")
    public RestResponse updateDepositoryRecord(@PathVariable Integer id, @RequestBody Map<String,Object> map, HttpServletRequest request){
        map.put("id", id);
        return CrudUtil.postHandle(depositoryRecordService.updateDepositoryRecord(map),1);
    }
    @PostMapping("/OutdepositoryRecord")
    public RestResponse insertDepositoryRecord2(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        UserToken userToken = (UserToken) request.getAttribute("userToken");
        map.put("applicantId",userToken.getUser().getId());
        map.put("reviewerId",userToken.getUser().getId());
        Integer result = depositoryRecordService.applyDirectOutbound(map);
        return CrudUtil.postHandle(result, 1);
    }
    @PutMapping("/OutdepositoryRecord/{id}")
    public RestResponse updateOutdepositoryRecord(@PathVariable Integer id, @RequestBody Map<String,Object> map, HttpServletRequest request){
        map.put("id", id);
        UserToken userToken = (UserToken) request.getAttribute("userToken");
        map.put("reviewerId", userToken.getUser().getId()); // 设置reviewerId
        map.put("state", "已出库");
        return CrudUtil.postHandle(depositoryRecordService.updateOutdepositoryRecord(map),1);
    }

    @DeleteMapping("/depositoryRecord")
    public RestResponse deleteDepositoryRecord(@RequestBody Map<String,Object> map){
        if (map.containsKey("id")){
            Integer id=ObjectFormatUtil.toInteger(map.get("id"));

            // 1. 查询要删除的入库记录的详细信息
            DepositoryRecord record = depositoryRecordMapper.findDepositoryRecordById(id);
            if (record != null && !"审核未通过".equals(record.getState()) && !"待审核".equals(record.getState())) {
                // 2. 根据入库记录的物品信息查询目标仓库中的物料记录
                Map<String, Object> queryMap = new HashMap<>();
                queryMap.put("depositoryId", record.getDepositoryId());
                queryMap.put("atId", record.getAtId());
                queryMap.put("model", record.getModel());
                queryMap.put("mname", record.getMname());
                List<Material> list = materialMapper.findMaterialByCondition(queryMap);

                if (!list.isEmpty()) {
                    Material material = list.get(0);
                    if (record.getType() == 1) { // 假设1代表入库
                        double totalPrice = material.getPrice() - (record.getPrice() * record.getQuantity());
                        BigDecimal bdTotalPrice = new BigDecimal(totalPrice);
                        bdTotalPrice = bdTotalPrice.setScale(2, RoundingMode.HALF_UP); // 保留两位小数，四舍五入
                        material.setPrice(bdTotalPrice.doubleValue());
                        // 计算新的总数量
                        double totalQuantity = material.getQuantity() - record.getQuantity();
                        // 计算新的均价
                        double unitPrice = (totalQuantity == 0) ? 0 : totalPrice / totalQuantity;
                        BigDecimal bdUnitPrice = new BigDecimal(unitPrice);
                        bdUnitPrice = bdUnitPrice.setScale(2, RoundingMode.HALF_UP); // 保留两位小数，四舍五入
                        material.setUnitPrice(bdUnitPrice.doubleValue());
                        material.setQuantity(totalQuantity);
                        materialMapper.updateMaterial(material);
                    } else if (record.getType() == 0) { // 假设0代表出库
                        double totalPrice = material.getPrice() + (record.getPrice() * record.getQuantity());
                        BigDecimal bdTotalPrice = new BigDecimal(totalPrice);
                        bdTotalPrice = bdTotalPrice.setScale(2, RoundingMode.HALF_UP); // 保留两位小数，四舍五入
                        material.setPrice(bdTotalPrice.doubleValue());
                        // 计算新的总数量
                        double totalQuantity = material.getQuantity() + record.getQuantity();
                        // 计算新的均价
                        double unitPrice = totalPrice / totalQuantity;
                        BigDecimal bdUnitPrice = new BigDecimal(unitPrice);
                        bdUnitPrice = bdUnitPrice.setScale(2, RoundingMode.HALF_UP); // 保留两位小数，四舍五入
                        material.setUnitPrice(bdUnitPrice.doubleValue());
                        material.setQuantity(totalQuantity);
                        materialMapper.updateMaterial(material);
                    }
                }
            }
            // 3. 删除入库记录
            return CrudUtil.deleteHandle(depositoryRecordService.deleteDepositoryRecordById(id),1);
        } else if (map.containsKey("ids")){
            List<Integer> ids=(List<Integer>) map.get("ids");
            for (Integer recordId : ids) {
                // Repeat the same logic as above for each record
                // ...
            }
            return CrudUtil.deleteHandle(depositoryRecordService.deleteDepositoryRecordByIds(ids),ids.size());
        } else {
            throw new MyException("所需请求参数缺失！");
        }
    }


    @PutMapping("/review")
    public RestResponse review(@RequestBody Map<String,Object> map, HttpServletRequest request){
        UserToken userToken= (UserToken) request.getAttribute("userToken");
        return CrudUtil.postHandle(depositoryRecordService.review(map,userToken.getUser().getId()),1);
    }
    @PutMapping("/transfer")
    public RestResponse transfer(@RequestBody Map<String,Object> map, HttpServletRequest request){
        UserToken userToken= (UserToken) request.getAttribute("userToken");
        map.put("applicantId",userToken.getUser().getId());
        return CrudUtil.postHandle(depositoryRecordService.transferApply(map),1);
    }
    @GetMapping("/monthly-amounts/{year}")
    public ResponseEntity<List<MonthlyAmountDTO>> getMonthlyAmounts(@PathVariable String year) {
        List<MonthlyAmountDTO> monthlyAmounts = depositoryRecordService.fetchMonthlyAmountsForYear(year);
        return ResponseEntity.ok(monthlyAmounts);
    }
    @GetMapping("/category-outbounds/{year}/{month}/{depositoryId}")
    public ResponseEntity<List<CategoryOutboundDTO>> getCategoryOutbounds(
            @PathVariable String year,
            @PathVariable String month,
            @PathVariable Integer depositoryId) {
        List<CategoryOutboundDTO> categoryOutbounds = depositoryRecordService.fetchCategoryOutboundsForYearMonth(year, month, depositoryId);
        return ResponseEntity.ok(categoryOutbounds);
    }
    @RequestMapping(value = {"/CategoryForYear/{year}/{depositoryId}", "/CategoryForYear/{year}/{depositoryId}/{categoryTitle}"})
    public ResponseEntity<List<CategoryOutboundDTO>> getCategoryOutboundsForYear(
            @PathVariable String year,
            @PathVariable Integer depositoryId,
            @PathVariable(required = false) String categoryTitle) throws UnsupportedEncodingException {
        if (categoryTitle != null && !categoryTitle.isEmpty()) {
            categoryTitle = URLDecoder.decode(categoryTitle, StandardCharsets.UTF_8.name());
        }

        List<CategoryOutboundDTO> categoryOutbounds;

        if (categoryTitle == null || categoryTitle.isEmpty()) {
            categoryOutbounds = depositoryRecordService.getTotalCategoryOutboundsForYear(year, depositoryId);
        } else {
            categoryOutbounds = depositoryRecordService.getCategoryOutboundsForYear(year, depositoryId, categoryTitle);
        }
        return ResponseEntity.ok(categoryOutbounds);
    }
    @RequestMapping(value = {"/TypeForYear/{year}/{depositoryId}", "/TypeForYear/{year}/{depositoryId}/{categoryTitle}"})
    public ResponseEntity<List<CategoryOutboundDTO>> getTypeOutboundsForYear(
            @PathVariable String year,
            @PathVariable Integer depositoryId,
            @PathVariable(required = false) String categoryTitle) throws UnsupportedEncodingException {
        if (categoryTitle != null && !categoryTitle.isEmpty()) {
            categoryTitle = URLDecoder.decode(categoryTitle, StandardCharsets.UTF_8.name());
        }

        List<CategoryOutboundDTO> categoryOutbounds;

        if (categoryTitle == null || categoryTitle.isEmpty()) {
            categoryOutbounds = depositoryRecordService.getTotalCategoryOutboundsForYear(year, depositoryId);
        } else {
            categoryOutbounds = depositoryRecordService.getTypeOutboundsForYear(year, depositoryId, categoryTitle);
        }
        return ResponseEntity.ok(categoryOutbounds);
    }


    @GetMapping("/type-amounts/{year}/{typeId}/{depositoryId}")
    public ResponseEntity<List<MonthlyAmountDTO>> getTypeAmounts(
            @PathVariable String year,
            @PathVariable Integer typeId,
            @PathVariable Integer depositoryId) {
        List<MonthlyAmountDTO> typeAmounts = depositoryRecordService.fetchMonthlyAmountByTypeAndYear(typeId, year, depositoryId);
        return ResponseEntity.ok(typeAmounts);
    }


    @GetMapping("/getMonthlyReport/{year}")
    public ResponseEntity<?> getTransferData(
            @PathVariable String year) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // 处理异常
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("服务器出现错误，无法延迟返回");
        }

        // 调用服务层获取数据
        List<Map<String, Object>> result = depositoryRecordService.getMonthlyReport(year);

        // 构建响应
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);  // layui Table组件通常使用"0"作为成功状态码
        response.put("msg", "");  // 可以根据需求设置消息
        response.put("count", result.size());  // 总记录数
        response.put("data", result);  // 返回的数据列表

        return ResponseEntity.ok(response);
    }
    @GetMapping("/viewPrice")
    public ResponseEntity<?> viewPrice(
            @RequestParam(required = false) String date) {
        Map<String, Object> params = new HashMap<>();
        System.out.println("接收到的日期参数: " + date);
        if (date != null) {
            String startDateStr;
            String endDateStr;

            if (date.contains(" - ")) {
                // 如果是区间格式
                String[] dates = date.split(" - ");
                if (dates.length == 2) {
                    startDateStr = dates[0] + " 00:00:00";
                    endDateStr = dates[1] + " 23:59:59";
                    params.put("startDate", startDateStr);
                    params.put("endDate", endDateStr);
                }
            } else if (date.matches("\\d{4}-\\d{2}")) {
                // 如果是 "2025-06" 格式
                try {
                    LocalDate firstDay = LocalDate.parse(date + "-01");
                    LocalDate lastDay = firstDay.with(TemporalAdjusters.lastDayOfMonth());
                    startDateStr = firstDay.atStartOfDay().toString().replace("T", " ");
                    endDateStr = lastDay.atTime(23, 59, 59).toString().replace("T", " ");
                    params.put("startDate", startDateStr);
                    params.put("endDate", endDateStr);
                } catch (DateTimeParseException e) {
                    return ResponseEntity.badRequest().body("日期格式不正确");
                }
            }
        }
        // 调用服务层获取数据
        List<Map<String, Object>> transferRecords = depositoryRecordService.viewPrice(params);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("msg", "");
        response.put("count", transferRecords.size());
        response.put("data", transferRecords);
        return ResponseEntity.ok(response);
    }
    @RequestMapping(value = {"/ScoreForYear/{year}/{depositoryId}"})
    public ResponseEntity<List<CategoryOutboundDTO>> getScoreForYear(
            @PathVariable String year,
            @PathVariable Integer depositoryId )  {
        List<CategoryOutboundDTO> categoryOutbounds;
            categoryOutbounds = depositoryRecordService.getScoreForYear(year, depositoryId);
        return ResponseEntity.ok(categoryOutbounds);
    }


}
