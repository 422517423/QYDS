package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.SysMenu;
import org.springframework.stereotype.Repository;

@Repository
public interface SysMenuMapper {
    int deleteByPrimaryKey(Integer menuId);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(Integer menuId);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);
}