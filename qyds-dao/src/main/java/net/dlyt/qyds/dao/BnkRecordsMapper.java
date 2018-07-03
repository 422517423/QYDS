package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.BnkRecords;
import org.springframework.stereotype.Repository;

@Repository
public interface BnkRecordsMapper {
    int deleteByPrimaryKey(Integer recordId);

    int insert(BnkRecords record);

    int insertSelective(BnkRecords record);

    BnkRecords selectByPrimaryKey(Integer recordId);

    int updateByPrimaryKeySelective(BnkRecords record);

    int updateByPrimaryKey(BnkRecords record);
}