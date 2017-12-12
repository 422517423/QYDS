package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.SysMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by panda on 16/7/4.
 */
@Repository
public interface SysMenuMapperExt{

    //获得全部菜单
    List<SysMenu> getMenu();

    //插入菜单并返回主键
    void addMenu(SysMenu dto);

    //编辑菜单
    void editMenu(SysMenu dto);

    //插入时更新sort
    void updateSort(SysMenu dto);

    //删除时更新sort
    int updateSortForDel(SysMenu dto);

    //逻辑删除菜单
    int deleteMenu(SysMenu dto);

    //获取左侧动态菜单
    List<SysMenu> getValidMenu(Integer userId);

    List<SysMenu> getRoleMenu(Integer roleId);
}
