package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ActMaster;
import org.springframework.stereotype.Repository;

@Repository
public interface ActMasterMapper {
    int deleteByPrimaryKey(String activityId);

    int insert(ActMaster record);

    int insertSelective(ActMaster record);

    ActMaster selectByPrimaryKey(String activityId);

    int updateByPrimaryKeySelective(ActMaster record);

    int updateByPrimaryKey(ActMaster record);
}