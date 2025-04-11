package com.depository_manage.controller;

import com.depository_manage.entity.NoticeAlert;
import com.depository_manage.pojo.RestResponse;
import com.depository_manage.service.NoticeAlertService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/noticeAlerts")
public class NoticeAlertController {

    private final NoticeAlertService noticeAlertService;

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
