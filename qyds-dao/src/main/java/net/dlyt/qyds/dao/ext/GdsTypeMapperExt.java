package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.GdsType;
import net.dlyt.qyds.common.dto.GdsTypeExt;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by congkeyan on 16/7/20.
 */
@Repository
public interface GdsTypeMapperExt {

    //获得全部菜单
    List<GdsType> getTreeList(GdsType gdsType);

    void updateSort(GdsType gdsType);

    /***********************API**************************************/

    /**
     * 获取分类的第一层节点
     * 默认ERP分类和电商分类都包含
     * 如果传递参数获取电商分类或者Erp分类的第一层节点
     *
     * @param gdsType type:规则编码
     *             10.ERP分类，20.电商分类
     * @return
     */
    List<GdsTypeExt> getGdsTypeFirstFloor(GdsType gdsType);

    /**
     * 获取分类的第一层节点和第二层节点
     * 默认ERP分类和电商分类都包含
     * 如果传递参数获取电商分类或者Erp分类的第一层节点和第二层节点
     *
     * @param gdsType type:规则编码
     *             10.ERP分类，20.电商分类
     * @return
     */
    List<GdsTypeExt> getSubLevelFloorList(GdsType gdsType);

    /**
     * 获取商品分类详细信息
     * @param goodsTypeId
     * @return
     */
    GdsTypeExt getGdsTypeById(String goodsTypeId);

    GdsType selectErpByName(String typeName);
}
