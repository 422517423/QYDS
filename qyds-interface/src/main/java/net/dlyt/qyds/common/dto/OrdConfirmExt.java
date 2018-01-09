package net.dlyt.qyds.common.dto;

import net.dlyt.qyds.common.dto.ext.MmbAddressExt;
import net.dlyt.qyds.common.form.ActMasterForm;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wenxuechao on 16/8/13.
 */
public class OrdConfirmExt implements Serializable {

    //买家收货地址信息
    private MmbAddressExt mmbAddressExt;

    //可参与活动列表
    private List<ActMasterForm> actMasterList;

    //发货方式  10.电商发货，20.门店自提
    private List<ComCode> deliverType;

    private float goodsTotalPrice = 0;

    //商品信息
    private List<OrdConfirmGoodsExt> goodsInfo;

    // 20180109 购物车编号
    private String[] bagNoArray;

    public String[] getBagNoArray() {
        return bagNoArray;
    }

    public void setBagNoArray(String[] bagNoArray) {
        this.bagNoArray = bagNoArray;
    }

    public MmbAddressExt getMmbAddressExt() {
        return mmbAddressExt;
    }

    public void setMmbAddressExt(MmbAddressExt mmbAddressExt) {
        this.mmbAddressExt = mmbAddressExt;
    }

    public List<ActMasterForm> getActMasterList() {
        return actMasterList;
    }

    public void setActMasterList(List<ActMasterForm> actMasterList) {
        this.actMasterList = actMasterList;
    }

    public List<ComCode> getDeliverType() {
        return deliverType;
    }

    public void setDeliverType(List<ComCode> deliverType) {
        this.deliverType = deliverType;
    }

    public List<OrdConfirmGoodsExt> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(List<OrdConfirmGoodsExt> goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public float getGoodsTotalPrice() {
        return goodsTotalPrice;
    }

    public void setGoodsTotalPrice(float goodsTotalPrice) {
        this.goodsTotalPrice = goodsTotalPrice;
    }
}
