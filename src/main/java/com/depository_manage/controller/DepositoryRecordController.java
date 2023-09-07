package com.depository_manage.controller;

import com.depository_manage.exception.MyException;
import com.depository_manage.pojo.DepositoryRecordP;
import com.depository_manage.pojo.RestResponse;
import com.depository_manage.security.bean.UserToken;
import com.depository_manage.service.DepositoryRecordService;
import com.depository_manage.utils.CrudUtil;
import com.depository_manage.utils.ObjectFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @GetMapping("/get_record")
    public DepositoryRecordP getDepositoryRecordById(@RequestParam Integer id) {
        return depositoryRecordService.findDepositoryRecordById(id);
    }
    @GetMapping("/depositoryRecord")
    public RestResponse findDepositoryRecordByCondition(@RequestParam Map<String,Object> map){
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
        System.out.println(reviewGroupId);

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
    @PostMapping("/OutdepositoryRecord")
    public RestResponse insertDepositoryRecord2(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        UserToken userToken = (UserToken) request.getAttribute("userToken");
        map.put("applicantId",userToken.getUser().getId());
        map.put("reviewerId",userToken.getUser().getId());
        Integer result = depositoryRecordService.applyDirectOutbound(map);
        return CrudUtil.postHandle(result, 1);
    }
    @PutMapping("/depositoryRecord/{id}")
    public RestResponse updateDepositoryRecord(@PathVariable Integer id, @RequestBody Map<String,Object> map, HttpServletRequest request){
        map.put("id", id);
        return CrudUtil.postHandle(depositoryRecordService.updateDepositoryRecord(map),1);
    }

    @DeleteMapping("/depositoryRecord")
    public RestResponse deleteDepositoryRecord(@RequestBody Map<String,Object> map){
        if (map.containsKey("id")){
            Integer id=ObjectFormatUtil.toInteger(map.get("id"));
            return CrudUtil.deleteHandle(depositoryRecordService.deleteDepositoryRecordById(id),1);
        }else if (map.containsKey("ids")){
            List<Integer> ids=(List<Integer>) map.get("ids");
            return CrudUtil.deleteHandle(depositoryRecordService.deleteDepositoryRecordByIds(ids),ids.size());
        }else {
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
}
