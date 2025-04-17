package com.depository_manage.controller;

import com.depository_manage.entity.Material;
import com.depository_manage.entity.Notice;
import com.depository_manage.entity.NoticeAlert;
import com.depository_manage.mapper.DepositoryRecordMapper;
import com.depository_manage.mapper.MaterialMapper;
import com.depository_manage.mapper.NoticeMapper;
import com.depository_manage.pojo.RestResponse;
import com.depository_manage.service.MaterialService;
import com.depository_manage.service.NoticeAlertService;
import com.depository_manage.service.NoticeService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/noticeAlerts")
public class NoticeAlertController {

    private final NoticeAlertService noticeAlertService;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private DepositoryRecordMapper depositoryRecordMapper;
    // 构造方法注入 NoticeAlertService
    public NoticeAlertController(NoticeAlertService noticeAlertService) {
        this.noticeAlertService = noticeAlertService;
    }

    // 获取所有预警记录
    @GetMapping("/")
    public ResponseEntity<List<NoticeAlert>> getAll(@RequestParam Map<String, Object> params) {
        List<NoticeAlert> noticeAlerts = noticeAlertService.findAll(params);
        return ResponseEntity.ok(noticeAlerts);
    }

    // 根据物品ID查询预警记录
    @GetMapping("/{atId}")
    public ResponseEntity<NoticeAlert> getByAtId(@PathVariable Integer atId) {
        NoticeAlert noticeAlert = noticeAlertService.findByAtId(atId);
        if (noticeAlert != null) {
            return ResponseEntity.ok(noticeAlert);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 插入新的预警记录
    @PostMapping("/")
    public ResponseEntity<Void> create(@RequestBody NoticeAlert noticeAlert) {
        noticeAlertService.insert(noticeAlert);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/import")
    public RestResponse importNoticeAlerts(@RequestParam("file") MultipartFile file) {
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // 跳过标题行

                Cell atIdCell = row.getCell(0);
                Cell quantityCell = row.getCell(1);
                if (atIdCell == null || quantityCell == null) continue;

                int atId = (int) atIdCell.getNumericCellValue();
                int alertQuantity = (int) quantityCell.getNumericCellValue();


                // 查询当前库存信息
                Map<String, Object> queryParam = new HashMap<>();
                queryParam.put("atId", atId);
                queryParam.put("depositoryId", 2);
                List<Material> materials = materialMapper.findMaterialForOutbound(queryParam);

                if (materials != null && !materials.isEmpty()) {
                    Material material = materials.get(0); // 通常一个atId只有一个记录

                    Double currentQuantity = material.getQuantity();
                    String mname = material.getMname();
                    String model = material.getModel();
                    String typeName = String.valueOf(material.getTypeId());
                    int did = material.getDepositoryId();

                    // 如果库存不足，添加通知
                    if (currentQuantity <= alertQuantity) {
                        String notificationContent = "AT号:" + atId + ", 品名: " + mname +
                                ", 分类: " + typeName + ", 型号: " + model + "，最后出库数:" + currentQuantity;

                        Map<String, Object> notice = new HashMap<>();
                        notice.put("title", (did == 1 ? "SAB：" : "ZAB") + "品名:" + mname + "，库存不足");
                        notice.put("content", notificationContent);
                        notice.put("atId", atId);
                        notice.put("mname", mname);
                        notice.put("depositoryId", did);
                        notice.put("model", model);
                        notice.put("typeName", typeName);
                        notice.put("time", " ");
                        Map<String, Object> checkParam = new HashMap<>();
                        checkParam.put("atId", atId);
                        checkParam.put("depositoryId", did);
                        List<Notice> existingNotices = noticeMapper.findNoticeByAtIdAndDepository(checkParam);
                        if (existingNotices == null || existingNotices.isEmpty()) {
                            // 插入新通知
                            noticeService.addNotice(notice);
                        } else {
                            // 已存在，跳过
                            continue;
                        }
                    }
                }
                NoticeAlert existing = noticeAlertService.findByAtId(atId);
                if (existing != null) {
                    // 如果已存在，则更新
                    existing.setAlertQuantity(alertQuantity);
                    noticeAlertService.update(existing);
                } else {
                    // 如果不存在，则插入（由 XML insert 填充 mname 和 model）
                    NoticeAlert alert = new NoticeAlert();
                    alert.setAtId(atId);
                    alert.setAlertQuantity(alertQuantity);
                    noticeAlertService.insert(alert);
                }
            }

            return new RestResponse("导入成功", 200, null);
        } catch (Exception e) {
            return new RestResponse("导入失败：" + e.getMessage(), 500, null);
        }
    }


    // 更新预警记录
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody NoticeAlert noticeAlert) {
        noticeAlert.setId(id);  // 设置传入的 ID
        noticeAlertService.update(noticeAlert);
        return ResponseEntity.ok().build();
    }

    // 删除预警记录
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        noticeAlertService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/alertQuantity/{atId}")
    public ResponseEntity<Integer> getAlertQuantityByAtId(@PathVariable Integer atId) {
        int alertQuantity = noticeAlertService.findAlertQuantityByAtId(atId);
        return ResponseEntity.ok(alertQuantity);
    }
}
