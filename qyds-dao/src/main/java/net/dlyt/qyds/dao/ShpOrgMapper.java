package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ShpOrg;
import org.springframework.stereotype.Repository;

@Repository
public interface ShpOrgMapper {
    int deleteByPrimaryKey(String orgId);

    int insert(ShpOrg record);

    int insertSelective(ShpOrg record);

    ShpOrg selectByPrimaryKey(String orgId);

    int updateByPrimaryKeySelective(ShpOrg record);

    int updateByPrimaryKey(ShpOrg record);
}