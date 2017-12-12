package net.dlyt.qyds.web.controller.ord_master;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.form.OrdMasterForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.DataUtils;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.OrdMasterService;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by zlh on 16/10/10.
 */
@Controller
@RequestMapping("/ord_refund")
public class OrdRefundController {
    @Autowired
    private OrdMasterService ordMasterService;

    /**
     * 卖家确认退单
     * @param rexOrderId
     * @return json
     */
    @RequestMapping("getReturnInfo")
    public @ResponseBody
    JSONObject getReturnInfo(String rexOrderId){
        return ordMasterService.getReturnInfoById(rexOrderId);
    }

    /**
     * 根据店铺ID获取订单列表信息
     * @param form
     * @return json
     */
    @RequestMapping("approveList")
    public @ResponseBody
    JSONObject approveList(OrdReturnExchangeExt form){
        JSONObject map = new JSONObject();
        try{
            // 参数设置
            // 退货类型:30,退单
            form.setRexType("30");
            // 退货状态为:10,申请中
            form.setRexStatus("10");
            map = ordMasterService.getRefundPage(form);
        }catch(ExceptionBusiness e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 根据店铺ID获取订单列表信息
     * @param form
     * @return json
     */
    @RequestMapping("payList")
    public @ResponseBody
    JSONObject payList(OrdReturnExchangeExt form){
        JSONObject map = new JSONObject();
        try{
            // 参数设置
            // 退货类型:30,退单
            form.setRexType("30");
            // 退货状态为:40.审批通过
            form.setRexStatus("40");
            //退款状态为10:等待付款
            form.setRefundStatus("10");
            map = ordMasterService.getRefundPage(form);
        }catch(ExceptionBusiness e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 卖家确认退单
     * @param data
     * @return json
     */
    @RequestMapping("approve")
    public @ResponseBody
    JSONObject approve(String data){
        JSONObject json = new JSONObject();
        try {
            //参数处理
            OrdReturnExchange ordReturnExchange = JSON.parseObject(data, OrdReturnExchange.class);
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            //登录人
            String loginId = (String) userMap.get("loginId");
            //设定用户信息
            ordReturnExchange.setApplyAnswerUserId(loginId);
            ordReturnExchange.setUpdateUserId(loginId);
            //退货申请审批处理
            json = ordMasterService.refundApprove(ordReturnExchange);
        }catch (ExceptionBusiness e){
            json.put("resultCode", Constants.INVALID);
            json.put("resultMessage", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 卖家确认退单
     * @param data
     * @return json
     */
    @RequestMapping("pay")
    public @ResponseBody
    JSONObject pay(String data){
        JSONObject json = new JSONObject();
        try {
            //参数处理
            OrdReturnExchange ordReturnExchange = JSON.parseObject(data, OrdReturnExchange.class);
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            //登录人
            String loginId = (String) userMap.get("loginId");
            //设定用户信息
            ordReturnExchange.setApplyAnswerUserId(loginId);
            ordReturnExchange.setUpdateUserId(loginId);
            //退货申请审批处理
            json = ordMasterService.refundPay(ordReturnExchange);
        }catch (ExceptionBusiness eb){
            json.put("resultCode", Constants.INVALID);
            json.put("resultMessage", eb.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }
}
