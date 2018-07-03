package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.CmsMaster;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMasterMapper {
    int deleteByPrimaryKey(String cmsId);

    int insert(CmsMaster record);

    int insertSelective(CmsMaster record);

    CmsMaster selectByPrimaryKey(String cmsId);

    int updateByPrimaryKeySelective(CmsMaster record);

    int updateByPrimaryKey(CmsMaster record);
}