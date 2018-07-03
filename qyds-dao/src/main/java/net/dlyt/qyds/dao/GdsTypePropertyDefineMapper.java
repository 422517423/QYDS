package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.GdsTypePropertyDefine;
import org.springframework.stereotype.Repository;

@Repository
public interface GdsTypePropertyDefineMapper {
    int deleteByPrimaryKey(String typeDefineId);

    int insert(GdsTypePropertyDefine record);

    int insertSelective(GdsTypePropertyDefine record);

    GdsTypePropertyDefine selectByPrimaryKey(String typeDefineId);

    int updateByPrimaryKeySelective(GdsTypePropertyDefine record);

    int updateByPrimaryKey(GdsTypePropertyDefine record);
}