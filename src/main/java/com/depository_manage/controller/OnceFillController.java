package com.depository_manage.controller;

import com.depository_manage.entity.OnceFill;
import com.depository_manage.pojo.DepositoryRecordP;
import com.depository_manage.pojo.OnceFillP;
import com.depository_manage.pojo.RestResponse;
import com.depository_manage.security.bean.UserToken;
import com.depository_manage.service.OnceFillService;
import com.depository_manage.utils.CrudUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/onceFill")
public class OnceFillController {

    @Autowired
    private OnceFillService onceFillService;

    // 添加一次性记录
    @PostMapping("/add")
    public ResponseEntity<Void> addRecords(@RequestBody List<OnceFill> records, HttpServletRequest request) {
        try {
            UserToken userToken = (UserToken) request.getAttribute("userToken");
            Integer depositoryId = userToken.getUser().getDepositoryId();
            onceFillService.saveAll(records, depositoryId);
            // 返回成功响应，HTTP 204 No Content
            return ResponseEntity.noContent().build();
        } catch(Exception e) {
            // 返回错误响应，HTTP 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // 更新一次性记录
    @PutMapping("/update/{id}")
    public RestResponse updateOnceFill(@PathVariable int id, @RequestBody Map<String, Object> map) {
        map.put("id", id);
        // 空字符串检查
        for(Map.Entry<String, Object> entry : map.entrySet()){
            if("".equals(entry.getValue())){
                map.put(entry.getKey(), null);
            }
        }
        // 必填字段验证：根据您的业务规则添加
        // if (map.get("someField") == null || "".equals(map.get("someField").toString().trim())) {
        //     return new RestResponse("Some field is required", 400, null);
        // }
        // 数据更新
        int result = onceFillService.updateOnceFill(map);
        // 处理和返回更新结果
        return CrudUtil.putHandle(result,1);
    }


    // 根据ID查询一次性记录
    @GetMapping("/get/{id}")
    public OnceFill findOnceFillById(@PathVariable Integer id) {
        return onceFillService.findOnceFillById(id);
    }

    // 根据条件查询一次性记录
//    @GetMapping("/list")
//    public List<OnceFill> findOnceFillByCondition(@RequestBody Map<String, Object> map) {
//        return onceFillService.findOnceFillByCondition(map);
//    }
    @GetMapping("/list")
    public RestResponse findOnceFillByCondition(@RequestParam Map<String,Object> map,
                                                @RequestParam(name = "typeId[]", required = false) List<Integer> typeIds){
        if (typeIds != null && !typeIds.isEmpty()) {
            map.put("typeId", typeIds);
        }
        String dateRange = (String) map.get("time");
        if (dateRange !=null && dateRange.contains(" - ")){
            String[] dates = dateRange.split(" - ");
            map.put("startDate", dates[0] + " 00:00:00");
            map.put("endDate", dates[1] + " 23:59:59");
        }
        List<OnceFillP> list=onceFillService.findOnceFillPByCondition(map);
        return new RestResponse(list,onceFillService.findCountByCondition(map),200);
    }

    // 根据ID删除一次性记录
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteOnceFillById(@PathVariable Integer id) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            onceFillService.deleteOnceFillById(id);
            resultMap.put("status", "success");
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    @PostMapping("/updateCheckPass")
    public ResponseEntity<?> updateCheckPass(@RequestBody Map<String, Object> map) {
        // 参数校验
        Object idObj = map.get("id");
        Object checkPass = map.get("checkPass");

        if (idObj == null || checkPass == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 400);
            error.put("message", "参数不完整");
            return ResponseEntity.badRequest().body(error);
        }

        // 确保 id 是整数
        try {
            map.put("id", Integer.parseInt(idObj.toString()));
        } catch (NumberFormatException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 400);
            error.put("message", "id 参数格式错误");
            return ResponseEntity.badRequest().body(error);
        }

        // 调用一次性更新方法
        int updated = onceFillService.updateOnceFill(map);

        Map<String, Object> result = new HashMap<>();
        if (updated > 0) {
            result.put("status", 200);
            result.put("message", "更新成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("status", 500);
            result.put("message", "更新失败");
            return ResponseEntity.status(500).body(result);
        }
    }
    @PostMapping("/OnceBatchUpdateReviewRemark")
    public ResponseEntity<?> batchUpdateReviewRemarkForOnceFill(@RequestBody Map<String, Object> map) {
        List<Integer> ids = (List<Integer>) map.get("ids");
        String invoiceNumber = (String) map.get("invoiceNumber");

        Map<String, Object> result = new HashMap<>();

        if (ids == null || ids.isEmpty() || invoiceNumber == null) {
            result.put("status", 400);
            result.put("message", "参数不完整");
            return ResponseEntity.ok(result); // 始终返回 200，由前端判断状态
        }

        int updated = onceFillService.batchUpdateReviewRemark(ids, invoiceNumber);

        if (updated > 0) {
            result.put("status", 200);
            result.put("message", "一次性记录批量更新成功");
        } else {
            result.put("status", 300); // 代表业务无更新（不是错误）
            result.put("message", "一次性记录未更新任何数据");
        }

        return ResponseEntity.ok(result);
    }


}
