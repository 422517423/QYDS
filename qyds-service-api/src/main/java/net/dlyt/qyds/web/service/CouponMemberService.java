package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.CouponMaster;
import net.dlyt.qyds.common.dto.CouponMember;
import net.dlyt.qyds.common.dto.ext.CouponMasterExt;
import net.dlyt.qyds.common.dto.ext.CouponMemberExt;

import java.io.File;

/**
 * Created by cjk on 16/8/9.
 */
public interface CouponMemberService {

    JSONObject getAllCoupons(CouponMemberExt form);

    JSONObject getPointExchangeCoupons(CouponMemberExt form);

    JSONObject getMyCoupons(CouponMemberExt form);

    JSONObject addCouponsForUser(CouponMemberExt form);

    JSONObject useCoupon(CouponMemberExt form);

    JSONObject returnCoupon(CouponMemberExt form);

    JSONObject getOrderCoupons(CouponMemberExt form);

    CouponMasterExt getOrderCouponById(String memberId, String couponId, float orderPrice);

    void addBirthdayCouponsForUser(CouponMemberExt form, CouponMaster coupon) throws Exception;
    void addRegisterCouponsForUser(CouponMemberExt form, CouponMaster coupon) throws Exception;

    JSONObject distributeCoupon(CouponMasterExt form);

    JSONObject distributeBirthdayCoupon();

    JSONObject selectRecordByPage(CouponMemberExt form);

    JSONObject sendToMember(CouponMember form);


}
