package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.ErpGoods;

/**
 * Created by zlh on 2016/7/28.
 */
public class ErpGoodsExt extends ErpGoods {

    // 更新方式,10新增,20修改,30删除
    private String style;
    //尺码种类名称
    private String sizeTypeName;
    //尺码排序
    private int sizeSort;
    //产品线名称
    private String lineName;

    private Short bnkNoLimit;
    private Short bnkLessLimit;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getSizeTypeName() {
        return sizeTypeName;
    }

    public void setSizeTypeName(String sizeTypeName) {
        this.sizeTypeName = sizeTypeName;
    }

    public int getSizeSort() {
        return sizeSort;
    }

    public void setSizeSort(int sizeSort) {
        this.sizeSort = sizeSort;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Short getBnkNoLimit() {
        return bnkNoLimit;
    }

    public void setBnkNoLimit(Short bnkNoLimit) {
        this.bnkNoLimit = bnkNoLimit;
    }

    public Short getBnkLessLimit() {
        return bnkLessLimit;
    }

    public void setBnkLessLimit(Short bnkLessLimit) {
        this.bnkLessLimit = bnkLessLimit;
    }
}
