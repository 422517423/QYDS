package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.OrdPayRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdPayRecordMapper {
    int deleteByPrimaryKey(String payId);

    int insert(OrdPayRecord record);

    int insertSelective(OrdPayRecord record);

    OrdPayRecord selectByPrimaryKey(String payId);

    int updateByPrimaryKeySelective(OrdPayRecord record);

    int updateByPrimaryKey(OrdPayRecord record);
}