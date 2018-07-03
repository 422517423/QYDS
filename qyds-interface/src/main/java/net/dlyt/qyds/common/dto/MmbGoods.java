package net.dlyt.qyds.common.dto;

import java.io.Serializable;

public class MmbGoods implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mmbGoodsId;
    private String goodsId;
    private String mmbLevelId;
    private String goodsName;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getMmbGoodsId() {
        return mmbGoodsId;
    }

    public void setMmbGoodsId(String mmbGoodsId) {
        this.mmbGoodsId = mmbGoodsId == null ? null : mmbGoodsId.trim();
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId  == null ? null : goodsId.trim();
    }

    public String getMmbLevelId() {
        return mmbLevelId;
    }

    public void setMmbLevelId(String mmbLevelId) {
        this.mmbLevelId = mmbLevelId  == null ? null : mmbLevelId.trim();
    }
}
