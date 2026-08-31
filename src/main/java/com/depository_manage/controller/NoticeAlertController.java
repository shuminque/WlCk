package com.depository_manage.controller;

import com.depository_manage.entity.Material;
import com.depository_manage.entity.Notice;
import com.depository_manage.entity.NoticeAlert;
import com.depository_manage.mapper.DepositoryRecordMapper;
import com.depository_manage.mapper.MaterialMapper;
import com.depository_manage.mapper.NoticeMapper;
import com.depository_manage.pojo.NoticeAlertImportPreview;
import com.depository_manage.pojo.RestResponse;
import com.depository_manage.service.MaterialService;
import com.depository_manage.service.NoticeAlertService;
import com.depository_manage.service.NoticeService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> create(@RequestBody NoticeAlert noticeAlert) {
        if (noticeAlert.getAtId() == null) {
            return ResponseEntity.badRequest().body(new RestResponse(false, "AT号不能为空"));
        }

        int existingCount = noticeAlertService.countByAtId(noticeAlert.getAtId());
        if (existingCount > 0) {
            String message = "AT号" + noticeAlert.getAtId() + "已存在" + existingCount
                    + "条预警记录，请直接编辑已有记录，不可重复添加";
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RestResponse(false, message));
        }

        int inserted = noticeAlertService.insert(noticeAlert);
        if (inserted != 1) {
            return ResponseEntity.unprocessableEntity()
                    .body(new RestResponse(false, "未找到仓库2中对应的物料，无法添加预警"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/import/preview")
    public ResponseEntity<RestResponse> previewNoticeAlertImport(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(buildImportPreviewResponse(buildImportPreview(file)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(failureResponse("预览失败：" + e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PostMapping("/import")
    @Transactional
    public ResponseEntity<RestResponse> importNoticeAlerts(@RequestParam("file") MultipartFile file,
                                                            @RequestParam(defaultValue = "false") boolean replaceExisting) {
        try {
            List<NoticeAlertImportPreview> previewRows = buildImportPreview(file);
            if (hasImportErrors(previewRows)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(importConflictResponse("导入内容存在异常，请修正后重新预览", previewRows));
            }
            if (hasReplacement(previewRows) && !replaceExisting) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(failureResponse("导入内容包含已有预警记录，请在预览中确认替换后再导入", HttpStatus.CONFLICT.value()));
            }

            for (NoticeAlertImportPreview preview : previewRows) {
                if ("新增".equals(preview.getAction())) {
                    NoticeAlert alert = new NoticeAlert();
                    alert.setAtId(preview.getAtId());
                    alert.setAlertQuantity(preview.getIncomingAlertQuantity());
                    if (noticeAlertService.insert(alert) != 1) {
                        throw new IllegalStateException("AT号" + preview.getAtId() + "未找到仓库2中的物料");
                    }
                } else if ("替换".equals(preview.getAction())) {
                    NoticeAlert alert = new NoticeAlert();
                    alert.setId(preview.getExistingAlertId());
                    alert.setAtId(preview.getAtId());
                    alert.setAlertQuantity(preview.getIncomingAlertQuantity());
                    noticeAlertService.update(alert);
                }

                createLowStockNoticeIfNeeded(preview.getAtId(), preview.getIncomingAlertQuantity());
            }

            return ResponseEntity.ok(new RestResponse("导入成功", 200, null));
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(failureResponse("导入失败：" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    private List<NoticeAlertImportPreview> buildImportPreview(MultipartFile file) throws Exception {
        List<NoticeAlertImportPreview> previewRows = new ArrayList<>();
        Map<Integer, NoticeAlertImportPreview> firstRowsByAtId = new HashMap<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0 || isEmptyRow(row)) {
                    continue;
                }

                NoticeAlertImportPreview preview = new NoticeAlertImportPreview();
                preview.setRowNumber(row.getRowNum() + 1);
                preview.setAtId(readIntegerCell(row.getCell(0)));
                preview.setIncomingAlertQuantity(readIntegerCell(row.getCell(1)));
                previewRows.add(preview);

                if (preview.getAtId() == null || preview.getIncomingAlertQuantity() == null) {
                    preview.setAction("异常");
                    preview.setMessage("AT号和预警数量必须为整数");
                    continue;
                }

                NoticeAlertImportPreview firstRow = firstRowsByAtId.putIfAbsent(preview.getAtId(), preview);
                if (firstRow != null) {
                    firstRow.setAction("异常");
                    firstRow.setMessage("Excel内AT号重复，另一行是第" + preview.getRowNumber() + "行");
                    preview.setAction("异常");
                    preview.setMessage("Excel内AT号重复，首次出现于第" + firstRow.getRowNumber() + "行");
                    continue;
                }

                Map<String, Object> alertQuery = new HashMap<>();
                alertQuery.put("atId", preview.getAtId());
                List<NoticeAlert> existingAlerts = noticeAlertService.findAll(alertQuery);
                if (existingAlerts.size() > 1) {
                    preview.setAction("异常");
                    preview.setMessage("系统内已有" + existingAlerts.size() + "条预警记录（"
                            + joinAlertQuantities(existingAlerts) + "），请先合并重复配置");
                    continue;
                }

                Map<String, Object> materialQuery = new HashMap<>();
                materialQuery.put("atId", preview.getAtId());
                materialQuery.put("depositoryId", 2);
                List<Material> materials = materialMapper.findMaterialForOutbound(materialQuery);
                if (materials == null || materials.isEmpty()) {
                    preview.setAction("异常");
                    preview.setMessage("仓库2未找到对应物料");
                    continue;
                }

                if (existingAlerts.isEmpty()) {
                    preview.setAction("新增");
                    preview.setMessage("将新增预警记录");
                } else {
                    NoticeAlert existingAlert = existingAlerts.get(0);
                    preview.setAction("替换");
                    preview.setExistingAlertId(existingAlert.getId());
                    preview.setExistingAlertQuantity(existingAlert.getAlertQuantity());
                    preview.setMessage("确认后替换现有预警数量");
                }
            }
        }
        if (previewRows.isEmpty()) {
            NoticeAlertImportPreview preview = new NoticeAlertImportPreview();
            preview.setAction("异常");
            preview.setMessage("Excel中未找到可导入的数据行");
            previewRows.add(preview);
        }
        return previewRows;
    }

    private Integer readIntegerCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            double value = cell.getNumericCellValue();
            return value == Math.rint(value) ? (int) value : null;
        }
        if (cell.getCellType() == CellType.STRING) {
            try {
                return Integer.valueOf(cell.getStringCellValue().trim());
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }

    private boolean isEmptyRow(Row row) {
        return row.getCell(0) == null && row.getCell(1) == null;
    }

    private boolean hasImportErrors(List<NoticeAlertImportPreview> previewRows) {
        return previewRows.stream().anyMatch(row -> "异常".equals(row.getAction()));
    }

    private boolean hasReplacement(List<NoticeAlertImportPreview> previewRows) {
        return previewRows.stream().anyMatch(row -> "替换".equals(row.getAction()));
    }

    private RestResponse buildImportPreviewResponse(List<NoticeAlertImportPreview> previewRows) {
        return new RestResponse(buildImportPreviewData(previewRows), previewRows.size(), HttpStatus.OK.value());
    }

    private String joinAlertQuantities(List<NoticeAlert> alerts) {
        return alerts.stream()
                .map(alert -> String.valueOf(alert.getAlertQuantity()))
                .collect(Collectors.joining("、"));
    }

    private RestResponse importConflictResponse(String message, List<NoticeAlertImportPreview> previewRows) {
        RestResponse response = new RestResponse(buildImportPreviewData(previewRows), previewRows.size(), HttpStatus.CONFLICT.value());
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    private RestResponse failureResponse(String message, int status) {
        RestResponse response = new RestResponse(false, message);
        response.setStatus(status);
        return response;
    }

    private Map<String, Object> buildImportPreviewData(List<NoticeAlertImportPreview> previewRows) {
        Map<String, Object> data = new HashMap<>();
        data.put("rows", previewRows);
        data.put("newCount", previewRows.stream().filter(row -> "新增".equals(row.getAction())).count());
        data.put("replacementCount", previewRows.stream().filter(row -> "替换".equals(row.getAction())).count());
        data.put("errorCount", previewRows.stream().filter(row -> "异常".equals(row.getAction())).count());
        return data;
    }

    private void createLowStockNoticeIfNeeded(Integer atId, Integer alertQuantity) {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("atId", atId);
        queryParam.put("depositoryId", 2);
        List<Material> materials = materialMapper.findMaterialForOutbound(queryParam);
        if (materials == null || materials.isEmpty()) {
            return;
        }

        Material material = materials.get(0);
        if (material.getQuantity() > alertQuantity) {
            return;
        }

        Map<String, Object> notice = new HashMap<>();
        notice.put("title", (material.getDepositoryId() == 1 ? "SAB：" : "ZAB") + "品名:" + material.getMname() + "，库存不足");
        notice.put("content", "AT号:" + atId + ", 品名: " + material.getMname()
                + ", 分类: " + material.getTypeId() + ", 型号: " + material.getModel()
                + "，最后出库数:" + material.getQuantity());
        notice.put("atId", atId);
        notice.put("mname", material.getMname());
        notice.put("depositoryId", material.getDepositoryId());
        notice.put("model", material.getModel());
        notice.put("typeName", String.valueOf(material.getTypeId()));
        notice.put("time", " ");

        Map<String, Object> checkParam = new HashMap<>();
        checkParam.put("atId", atId);
        checkParam.put("depositoryId", material.getDepositoryId());
        List<Notice> existingNotices = noticeMapper.findNoticeByAtIdAndDepository(checkParam);
        if (existingNotices == null || existingNotices.isEmpty()) {
            noticeService.addNotice(notice);
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
