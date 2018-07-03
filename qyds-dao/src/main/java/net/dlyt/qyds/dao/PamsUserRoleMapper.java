package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.PamsUserRoleKey;
import org.springframework.stereotype.Repository;

@Repository
public interface PamsUserRoleMapper {
    int deleteByPrimaryKey(PamsUserRoleKey key);

    int insert(PamsUserRoleKey record);

    int insertSelective(PamsUserRoleKey record);
}