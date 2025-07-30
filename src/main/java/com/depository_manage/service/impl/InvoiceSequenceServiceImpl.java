package com.depository_manage.service.impl;

import com.depository_manage.entity.InvoiceSequence;
import com.depository_manage.mapper.InvoiceSequenceMapper;
import com.depository_manage.service.InvoiceSequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceSequenceServiceImpl implements InvoiceSequenceService {

    @Autowired
    private InvoiceSequenceMapper invoiceSequenceMapper;

    @Override
    public String getNextInvoiceNumber(String invoiceMonth) {
        InvoiceSequence existing = invoiceSequenceMapper.getByMonth(invoiceMonth);
        int nextSeq;

        if (existing == null) {
            // 如果当月还没记录，插入1
            nextSeq = 1;
            InvoiceSequence seq = new InvoiceSequence();
            seq.setInvoiceMonth(invoiceMonth);
            seq.setCurrentSequence(nextSeq);
            invoiceSequenceMapper.insert(seq);
        } else {
            nextSeq = existing.getCurrentSequence() + 1;
            invoiceSequenceMapper.updateSequence(invoiceMonth, nextSeq);
        }

        return invoiceMonth + "-" + nextSeq;
    }
    @Override
    public InvoiceSequence getByMonth(String month) {
        return invoiceSequenceMapper.getByMonth(month);
    }

}
