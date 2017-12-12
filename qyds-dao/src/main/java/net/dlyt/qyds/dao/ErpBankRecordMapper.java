package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpBankRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpBankRecordMapper {
    int deleteByPrimaryKey(Integer recordid);

    int insert(ErpBankRecord record);

    int insertSelective(ErpBankRecord record);

    ErpBankRecord selectByPrimaryKey(Integer recordid);

    int updateByPrimaryKeySelective(ErpBankRecord record);

    int updateByPrimaryKey(ErpBankRecord record);
}