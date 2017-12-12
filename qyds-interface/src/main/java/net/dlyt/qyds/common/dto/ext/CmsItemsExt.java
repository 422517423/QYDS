package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.CmsItems;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CmsItemsExt extends CmsItems {

    // 上级菜单名称
    private String itemNameParent;

    // 栏目的cms列表
    private List<CmsMasterExt> cmList;

    private List<CmsItemsExt> ciList;

    private String isChild;

    // 商品分类
    private String goodsTypeId;

    public String getItemNameParent() {
        return itemNameParent;
    }

    public void setItemNameParent(String itemNameParent) {
        this.itemNameParent = itemNameParent;
    }

    public List<CmsMasterExt> getCmList() {
        return cmList;
    }

    public void setCmList(List<CmsMasterExt> cmList) {
        this.cmList = cmList;
    }

    public List<CmsItemsExt> getCiList() {
        return ciList;
    }

    public void setCiList(List<CmsItemsExt> ciList) {
        this.ciList = ciList;
    }

    public String getIsChild() {
        return isChild;
    }

    public void setIsChild(String isChild) {
        this.isChild = isChild;
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }
}