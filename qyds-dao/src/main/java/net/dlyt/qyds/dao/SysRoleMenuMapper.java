package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.SysRoleMenuKey;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleMenuMapper {
    int deleteByPrimaryKey(SysRoleMenuKey key);

    int insert(SysRoleMenuKey record);

    int insertSelective(SysRoleMenuKey record);
}