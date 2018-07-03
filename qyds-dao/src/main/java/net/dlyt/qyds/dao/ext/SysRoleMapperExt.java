package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.SysRole;
import net.dlyt.qyds.common.view.SysMenuExt;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by C_Nagai on 2016/7/5.
 */
@Repository
public interface SysRoleMapperExt {

    List<SysRole> selectAll();

    int insertSelective(SysRole record);

    int updateByPrimaryKeySelective(SysRole record);

    int deleteByPrimaryKey(SysRole record);

    /**
     * 获取角色菜单
     * @param roleId
     */
    List<SysMenuExt> getRoleMenu(int roleId);

    /**
     * 清空角色菜单
     * @param roleId
     */
    void clearRoleMenu(int roleId);
}
