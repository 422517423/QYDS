package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.OrdHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdHistoryMapper {
    int deleteByPrimaryKey(String seqOrderId);

    int insert(OrdHistory record);

    int insertSelective(OrdHistory record);

    OrdHistory selectByPrimaryKey(String seqOrderId);

    int updateByPrimaryKeySelective(OrdHistory record);

    int updateByPrimaryKey(OrdHistory record);
}