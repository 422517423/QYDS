package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ActGroup;
import org.springframework.stereotype.Repository;

@Repository
public interface ActGroupMapper {
    int deleteByPrimaryKey(String actMemberId);

    int insert(ActGroup record);

    int insertSelective(ActGroup record);

    ActGroup selectByPrimaryKey(String actMemberId);

    int updateByPrimaryKeySelective(ActGroup record);

    int updateByPrimaryKey(ActGroup record);
}