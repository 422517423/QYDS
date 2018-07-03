package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ActTempParam;
import org.springframework.stereotype.Repository;

@Repository
public interface ActTempParamMapper {
    int deleteByPrimaryKey(String paramId);

    int insert(ActTempParam record);

    int insertSelective(ActTempParam record);

    ActTempParam selectByPrimaryKey(String paramId);

    int updateByPrimaryKeySelective(ActTempParam record);

    int updateByPrimaryKey(ActTempParam record);
}