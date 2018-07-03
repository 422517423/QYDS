package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ShpOrg;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wanglijie on 16/7/29.
 */
@Repository
public interface ShpOrgMapperExt {

    //获得全部组织
    List<ShpOrg> getOrg();
    //获取子组织名称
    List<ShpOrg> getChildOrg();
    //插入菜单并返回主键
    void addOrg(ShpOrg dto);

    //编辑菜单
    void editOrg(ShpOrg dto);

    //插入时更新sort
    void updateSort(ShpOrg dto);

    //删除时更新sort
    void updateSortForDel(ShpOrg dto);

    //逻辑删除菜单
    void deleteOrg(ShpOrg dto);

    //删除所有门店
    void deleteAllStore();

    //更新门店名称单
    void updateStoreName(ShpOrg dto);

    //插入门店
    void insertStore(ShpOrg dto);

    List<ShpOrg> selectByStoresubid(String storesubid);

}
