package net.dlyt.qyds.common.form;

import java.util.Date;
import java.util.List;

/**
 * Created by YiLian on 16/8/2.
 */
public class MmbShoppingBagForm {


    /**
     * 删除多件商品时使用的参数
     */
    private List<String> delBags;

    /**
     * 购物袋编号
     */
    private String bagNo;

    /**
     * 商品ID
     */
    private String memberId;

    /**
     * 商品ID
     */
    private String goodsId;

    /**
     * 商品活动ID
     */
    private String actGoodsId;

    /**
     * 件数
     */
    private Integer quantity;

    /**
     * sku
     */
    private List<MmbShoppingSkuForm> skuList;

    private List<ActMasterForm> activityList;

    /**
     * 最后一条更新时间(分页用)
     */
    private Date lastUpdateTime;

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getBagNo() {
        return bagNo;
    }

    public void setBagNo(String bagNo) {
        this.bagNo = bagNo;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getActGoodsId() {
        return actGoodsId;
    }

    public void setActGoodsId(String actGoodsId) {
        this.actGoodsId = actGoodsId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<MmbShoppingSkuForm> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<MmbShoppingSkuForm> skuList) {
        this.skuList = skuList;
    }

    public List<String> getDelBags() {
        return delBags;
    }

    public void setDelBags(List<String> delBags) {
        this.delBags = delBags;
    }

    public List<ActMasterForm> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<ActMasterForm> activityList) {
        this.activityList = activityList;
    }
}
