package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpOrderSub;

public interface ErpOrderSubMapper {
    int deleteByPrimaryKey(String subCode);

    int insert(ErpOrderSub record);

    int insertSelective(ErpOrderSub record);

    ErpOrderSub selectByPrimaryKey(String subCode);

    int updateByPrimaryKeySelective(ErpOrderSub record);

    int updateByPrimaryKey(ErpOrderSub record);
}