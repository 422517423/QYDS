package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.GdsSchedule;

/**
 * Created by panda on 16/9/26.
 */
public class GdsScheduleExt extends GdsSchedule {

    private String goodsName;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
