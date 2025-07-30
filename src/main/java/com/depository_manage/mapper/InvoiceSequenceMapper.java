package com.depository_manage.mapper;

import com.depository_manage.entity.InvoiceSequence;
import org.apache.ibatis.annotations.*;

@Mapper
public interface InvoiceSequenceMapper {

    @Select("SELECT * FROM invoice_sequence WHERE invoice_month = #{invoiceMonth}")
    InvoiceSequence getByMonth(@Param("invoiceMonth") String invoiceMonth);

    @Insert("INSERT INTO invoice_sequence (invoice_month, current_sequence) VALUES (#{invoiceMonth}, #{currentSequence})")
    int insert(InvoiceSequence invoiceSequence);

    @Update("UPDATE invoice_sequence SET current_sequence = #{currentSequence}, update_time = NOW() WHERE invoice_month = #{invoiceMonth}")
    int updateSequence(@Param("invoiceMonth") String invoiceMonth, @Param("currentSequence") int currentSequence);
}
