package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpOrderMaster;

public interface ErpOrderMasterMapper {
    int deleteByPrimaryKey(String orderCode);

    int insert(ErpOrderMaster record);

    int insertSelective(ErpOrderMaster record);

    ErpOrderMaster selectByPrimaryKey(String orderCode);

    int updateByPrimaryKeySelective(ErpOrderMaster record);

    int updateByPrimaryKey(ErpOrderMaster record);
}