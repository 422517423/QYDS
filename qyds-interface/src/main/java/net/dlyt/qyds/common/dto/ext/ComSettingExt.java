package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.ActMaster;
import net.dlyt.qyds.common.dto.ComSetting;
import net.dlyt.qyds.common.dto.CouponMaster;

/**
 * Created by cjk on 2016/12/14.
 */
public class ComSettingExt extends ComSetting {

    private ActMaster firstBuyActivityDetail = null;
    private CouponMaster firstBuyCouponDetail = null;

    public ActMaster getFirstBuyActivityDetail() {
        return firstBuyActivityDetail;
    }

    public void setFirstBuyActivityDetail(ActMaster firstBuyActivityDetail) {
        this.firstBuyActivityDetail = firstBuyActivityDetail;
    }

    public CouponMaster getFirstBuyCouponDetail() {
        return firstBuyCouponDetail;
    }

    public void setFirstBuyCouponDetail(CouponMaster firstBuyCouponDetail) {
        this.firstBuyCouponDetail = firstBuyCouponDetail;
    }
}
