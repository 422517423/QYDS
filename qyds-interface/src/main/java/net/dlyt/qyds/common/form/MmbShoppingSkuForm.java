package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.MmbShoppingSku;

/**
 * Created by YiLian on 16/8/4.
 */
public class MmbShoppingSkuForm {

    private String bagNo;

    private String skuId;

    private String type;

    public String getBagNo() {
        return bagNo;
    }

    public void setBagNo(String bagNo) {
        this.bagNo = bagNo;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
