package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.UsrOrg;
import org.springframework.stereotype.Repository;

@Repository
public interface UsrOrgMapper {
    int deleteByPrimaryKey(String userOrgId);

    int insert(UsrOrg record);

    int insertSelective(UsrOrg record);

    UsrOrg selectByPrimaryKey(String userOrgId);

    int updateByPrimaryKeySelective(UsrOrg record);

    int updateByPrimaryKey(UsrOrg record);
}