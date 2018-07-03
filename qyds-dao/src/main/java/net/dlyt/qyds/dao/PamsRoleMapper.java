package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.PamsRole;
import net.dlyt.qyds.common.dto.PamsRoleKey;
import org.springframework.stereotype.Repository;

@Repository
public interface PamsRoleMapper {
    int deleteByPrimaryKey(PamsRoleKey key);

    int insert(PamsRole record);

    int insertSelective(PamsRole record);

    PamsRole selectByPrimaryKey(PamsRoleKey key);

    int updateByPrimaryKeySelective(PamsRole record);

    int updateByPrimaryKey(PamsRole record);
}