package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.CouponMaster;
import net.dlyt.qyds.common.dto.ext.CouponMasterExt;

import java.util.List;

/**
 * Created by cjk on 16/8/6.
 */
public interface CouponMasterService {
    JSONObject getList(CouponMasterExt form);

    JSONObject delete(CouponMasterExt form);

    JSONObject add(CouponMasterExt form);

    JSONObject edit(CouponMasterExt form);

    JSONObject apply(CouponMasterExt form);

    JSONObject getDetail(CouponMasterExt form);

    JSONObject getApproveList(CouponMasterExt form);

    JSONObject approve(CouponMasterExt form);

    JSONObject reject(CouponMasterExt form);

    // 获取PC购物券
    JSONObject getPcCouponList(String data);

    JSONObject getPcCoupon(String data);

    JSONObject getSendList();

    JSONObject setSort(CouponMaster form);

    JSONObject setValid(CouponMasterExt form);

    JSONObject setInvalid(CouponMasterExt form);

    JSONObject getSkuListById(String id);

    JSONObject importSkuList(String loginId, String couponId, List<String> skuList);

    JSONObject addGoods(CouponMasterExt form);

    JSONObject getGoodsList(CouponMasterExt form);

    JSONObject deleteGoods(CouponMasterExt form);
}
