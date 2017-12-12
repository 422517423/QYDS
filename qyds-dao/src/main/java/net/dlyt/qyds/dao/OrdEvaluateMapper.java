package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.OrdEvaluate;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdEvaluateMapper {
    int deleteByPrimaryKey(String evaluateId);

    int insert(OrdEvaluate record);

    int insertSelective(OrdEvaluate record);

    OrdEvaluate selectByPrimaryKey(String evaluateId);

    int updateByPrimaryKeySelective(OrdEvaluate record);

    int updateByPrimaryKey(OrdEvaluate record);
}