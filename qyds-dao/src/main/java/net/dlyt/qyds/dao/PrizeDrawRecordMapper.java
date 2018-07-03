package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.PrizeDrawRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface PrizeDrawRecordMapper {
    int deleteByPrimaryKey(String prizeDrawRecordId);

    int insert(PrizeDrawRecord record);

    int insertSelective(PrizeDrawRecord record);

    PrizeDrawRecord selectByPrimaryKey(String prizeDrawRecordId);

    int updateByPrimaryKeySelective(PrizeDrawRecord record);

    int updateByPrimaryKey(PrizeDrawRecord record);
}