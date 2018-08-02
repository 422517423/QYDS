package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.GdsSchedule;

/**
 * Created by panda on 16/9/26.
 */
public class GdsScheduleExt extends GdsSchedule {

    private String goodsName;

    private String goodsCode;

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
