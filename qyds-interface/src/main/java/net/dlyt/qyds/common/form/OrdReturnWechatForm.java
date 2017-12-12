package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.OrdReturnExchange;

/**
 * Created by wenxuechao on 16/8/1.
 */
public class OrdReturnWechatForm extends OrdReturnExchange {

    //商品明细订单
    private String detailId;

    //退货商品个数
    private String returnCount;

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(String returnCount) {
        this.returnCount = returnCount;
    }
}
