package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.OrdHistory;

/**
 * Created by cjk on 16/10/15.
 */
public class OrdHistoryExt extends OrdHistory {

    private String orderStatusName;

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }
}
