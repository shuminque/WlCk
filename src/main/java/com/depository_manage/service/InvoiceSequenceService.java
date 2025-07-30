package com.depository_manage.service;

import com.depository_manage.entity.InvoiceSequence;

public interface InvoiceSequenceService {
    String getNextInvoiceNumber(String invoiceMonth);

    InvoiceSequence getByMonth(String month);
}
