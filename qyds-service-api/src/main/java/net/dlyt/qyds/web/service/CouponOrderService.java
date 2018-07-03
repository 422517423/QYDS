package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ext.CouponMemberExt;
import net.dlyt.qyds.common.dto.ext.CouponOrderExt;

/**
 * Created by cjk on 2016/12/13.
 */
public interface CouponOrderService {
    JSONObject paySuccess(CouponOrderExt form);

    JSONObject sendCoupon(CouponOrderExt form);
}
