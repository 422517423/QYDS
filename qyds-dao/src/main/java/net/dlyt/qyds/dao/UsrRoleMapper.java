package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.UsrRole;
import org.springframework.stereotype.Repository;

@Repository
public interface UsrRoleMapper {
    int deleteByPrimaryKey(String userRoleId);

    int insert(UsrRole record);

    int insertSelective(UsrRole record);

    UsrRole selectByPrimaryKey(String userRoleId);

    int updateByPrimaryKeySelective(UsrRole record);

    int updateByPrimaryKey(UsrRole record);
}