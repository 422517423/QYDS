package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpOrderMapper {
    int deleteByPrimaryKey(String ticketNo);

    int insert(ErpOrder record);

    int insertSelective(ErpOrder record);

    ErpOrder selectByPrimaryKey(String ticketNo);

    int updateByPrimaryKeySelective(ErpOrder record);

    int updateByPrimaryKey(ErpOrder record);
}