package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpStore;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpStoreMapper {
    int deleteByPrimaryKey(String storeCode);

    int insert(ErpStore record);

    int insertSelective(ErpStore record);

    ErpStore selectByPrimaryKey(String storeCode);

    int updateByPrimaryKeySelective(ErpStore record);

    int updateByPrimaryKey(ErpStore record);
}