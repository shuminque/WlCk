package com.depository_manage.controller;

import com.depository_manage.entity.InvoiceSequence;
import com.depository_manage.service.InvoiceSequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceSequenceService invoiceSequenceService;


    @GetMapping("/getCurrentSequence")
    public ResponseEntity<Integer> getCurrentSequence(@RequestParam("month") String month) {
        InvoiceSequence seq = invoiceSequenceService.getByMonth(month);
        int current = (seq != null) ? seq.getCurrentSequence() : 0;
        return ResponseEntity.ok(current);
    }

    /**
     * 获取下一个发票序号（格式如：202507-1）
     */
    @PostMapping("/getNextSequence")
    public ResponseEntity<String> getNextSequence(@RequestParam("month") String month) {
        String nextInvoiceNumber = invoiceSequenceService.getNextInvoiceNumber(month);
        return ResponseEntity.ok(nextInvoiceNumber);
    }

}
