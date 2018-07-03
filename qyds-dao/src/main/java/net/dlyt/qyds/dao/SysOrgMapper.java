package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.SysOrg;
import org.springframework.stereotype.Repository;

@Repository
public interface SysOrgMapper {
    int deleteByPrimaryKey(String orgId);

    int insert(SysOrg record);

    int insertSelective(SysOrg record);

    SysOrg selectByPrimaryKey(String orgId);

    int updateByPrimaryKeySelective(SysOrg record);

    int updateByPrimaryKey(SysOrg record);
}