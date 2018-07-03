package net.dlyt.qyds.web.controller.mmb_coupon_pc;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ext.CouponMasterExt;
import net.dlyt.qyds.web.service.CouponMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by C_Nagai on 2016/9/5.
 */
@Controller
@RequestMapping("/mmb_coupon_pc_api")
public class MmbCouponPcApiController {

    @Autowired
    private CouponMasterService couponMasterService;

    @RequestMapping("getList")
    @ResponseBody
    public JSONObject getList(String data) {
        return couponMasterService.getPcCouponList(data);
    }

    @RequestMapping("getCount")
    @ResponseBody
    public JSONObject getCount(String data) {
        return couponMasterService.getPcCoupon(data);
    }
}
