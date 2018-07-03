package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbInvoice;
import org.springframework.stereotype.Repository;

@Repository
public interface MmbInvoiceMapper {
    int deleteByPrimaryKey(String invoiceId);

    int insert(MmbInvoice record);

    int insertSelective(MmbInvoice record);

    MmbInvoice selectByPrimaryKey(String invoiceId);

    int updateByPrimaryKeySelective(MmbInvoice record);

    int updateByPrimaryKey(MmbInvoice record);
}