package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpPointRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpPointRecordMapper {
    int deleteByPrimaryKey(Integer recordid);

    int insert(ErpPointRecord record);

    int insertSelective(ErpPointRecord record);

    ErpPointRecord selectByPrimaryKey(Integer recordid);

    int updateByPrimaryKeySelective(ErpPointRecord record);

    int updateByPrimaryKey(ErpPointRecord record);
}