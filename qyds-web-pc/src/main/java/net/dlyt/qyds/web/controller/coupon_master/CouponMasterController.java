package net.dlyt.qyds.web.controller.coupon_master;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ext.CouponMasterExt;
import net.dlyt.qyds.common.dto.ext.CouponMemberExt;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.CouponMasterService;
import net.dlyt.qyds.web.service.CouponMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by cky on 2016/7/18.
 */
@Controller
@RequestMapping("/coupon_master")
public class CouponMasterController {

    @Autowired
    private CouponMasterService couponMasterService;
    @Autowired
    private CouponMemberService couponMemberService;

    @RequestMapping("getList")
    @ResponseBody
    public JSONObject getList(CouponMasterExt form) {
        return couponMasterService.getList(form);
    }

    @RequestMapping("getApproveList")
    @ResponseBody
    public JSONObject getApproveList(CouponMasterExt form) {
        return couponMasterService.getApproveList(form);
    }


    @RequestMapping("getDetail")
    @ResponseBody
    public JSONObject getDetail(String data) {
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        return couponMasterService.getDetail(form);
    }

    @RequestMapping("delete")
    @ResponseBody
    public JSONObject delete(String data) {
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        return couponMasterService.delete(form);
    }

    @RequestMapping("add")
    @ResponseBody
    public JSONObject add(String data) {
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        form.setShopId(Constants.ORGID);
        form.setInsertUserId((String) PamsDataContext.get("loginId"));
        return couponMasterService.add(form);
    }

    @RequestMapping("edit")
    @ResponseBody
    public JSONObject edit(String data) {
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        form.setShopId(Constants.ORGID);
        form.setUpdateUserId((String) PamsDataContext.get("loginId"));
        return couponMasterService.edit(form);
    }

    /**
     * 申请
     *
     * @param data
     * @return
     */
    @RequestMapping("apply")
    @ResponseBody
    public JSONObject apply(String data) {
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        form.setApplyUserId((String) PamsDataContext.get("loginId"));
        return couponMasterService.apply(form);
    }

    /**
     * 审批通过
     *
     * @param data
     * @return
     */
    @RequestMapping("approve")
    @ResponseBody
    public JSONObject approve(String data) {
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        form.setApproveUserId((String) PamsDataContext.get("loginId"));
        return couponMasterService.approve(form);
    }

    /**
     * 审批驳回
     *
     * @param data
     * @return
     */
    @RequestMapping("reject")
    @ResponseBody
    public JSONObject reject(String data) {
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        form.setApproveUserId((String) PamsDataContext.get("loginId"));
        return couponMasterService.reject(form);
    }


    @RequestMapping("getAllCoupons")
    @ResponseBody
    public JSONObject getAllCoupons(String data) {
        CouponMemberExt form = (CouponMemberExt) JSON.parseObject(data, CouponMemberExt.class);
        return couponMemberService.getAllCoupons(form);
    }

    @RequestMapping("getPointExchangeCoupons")
    @ResponseBody
    public JSONObject getPointExchangeCoupons(String data) {
        CouponMemberExt form = (CouponMemberExt) JSON.parseObject(data, CouponMemberExt.class);
        return couponMemberService.getPointExchangeCoupons(form);
    }


    @RequestMapping("getMyCoupons")
    @ResponseBody
    public JSONObject getCouponsByUser(String data) {
        CouponMemberExt form = (CouponMemberExt) JSON.parseObject(data, CouponMemberExt.class);
        return couponMemberService.getMyCoupons(form);
    }

    @RequestMapping("addCouponsForUser")
    @ResponseBody
    public JSONObject addCouponsForUser(String data) {
        CouponMemberExt form = (CouponMemberExt) JSON.parseObject(data, CouponMemberExt.class);
        return couponMemberService.addCouponsForUser(form);
    }

    @RequestMapping("getOrderCoupons")
    @ResponseBody
    public JSONObject getOrderCoupons(String data) {
        CouponMemberExt form = (CouponMemberExt) JSON.parseObject(data, CouponMemberExt.class);
        return couponMemberService.getOrderCoupons(form);
    }


}
