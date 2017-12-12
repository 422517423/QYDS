package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GdsTypeExt implements Serializable {

    //分类ID
    private String goodsTypeId;

    //分类简称
    private String goodsTypeNameCn;

    //分类logo
    private String imageUrl;

    List<GdsTypeExt> secondTypeList;

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public String getGoodsTypeNameCn() {
        return goodsTypeNameCn;
    }

    public void setGoodsTypeNameCn(String goodsTypeNameCn) {
        this.goodsTypeNameCn = goodsTypeNameCn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<GdsTypeExt> getSecondTypeList() {
        return secondTypeList;
    }

    public void setSecondTypeList(List<GdsTypeExt> secondTypeList) {
        this.secondTypeList = secondTypeList;
    }
}