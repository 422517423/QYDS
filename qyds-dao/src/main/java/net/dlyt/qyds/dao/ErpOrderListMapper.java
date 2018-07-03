package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpOrderList;
import org.springframework.stereotype.Repository;

@Repository

public interface ErpOrderListMapper {
    int deleteByPrimaryKey(String ticketSubno);

    int insert(ErpOrderList record);

    int insertSelective(ErpOrderList record);

    ErpOrderList selectByPrimaryKey(String ticketSubno);

    int updateByPrimaryKeySelective(ErpOrderList record);

    int updateByPrimaryKey(ErpOrderList record);
}