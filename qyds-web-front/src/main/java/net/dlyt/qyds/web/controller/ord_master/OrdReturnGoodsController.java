package net.dlyt.qyds.web.controller.ord_master;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.form.OrdDispatchForm;
import net.dlyt.qyds.common.form.OrdMasterForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.DataUtils;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.OrdMasterService;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by cjk on 16/10/10.
 */
@Controller
@RequestMapping("/ord_return_goods")
public class OrdReturnGoodsController {
    @Autowired
    private OrdMasterService ordMasterService;

    /**
     * 获取待确认收退货
     * @param form
     * @return json
     */
    @RequestMapping("receiveGoodsList")
    public @ResponseBody
    JSONObject receiveGoodsList(OrdReturnExchangeExt form){
        //取得用户登录信息
        Map<String, Object> userMap = PamsDataContext.get();
        //登录人门店ID
        String orgId = (String) userMap.get("orgId");
        form.setRexStoreId(orgId);
        return ordMasterService.getReturnGoodsReceiveList(form);
    }


    /**
     * 卖家确认退货
     * @param data
     * @return json
     */
    @RequestMapping("approve")
    public @ResponseBody
    JSONObject approve(String data){
        JSONObject json = new JSONObject();
        try {
            //参数处理
            OrdReturnExchangeExt ordReturnExchange = JSON.parseObject(data, OrdReturnExchangeExt.class);
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            //登录人
            String loginId = (String) userMap.get("loginId");
            //设定用户信息
            ordReturnExchange.setApplyAnswerUserId(loginId);
            ordReturnExchange.setUpdateUserId(loginId);
            //退货申请审批处理
            json = ordMasterService.returnGoodsApprove(ordReturnExchange);
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
     * 卖家确认收货
     * @param data
     * @return json
     */
    @RequestMapping("receiveGoods")
    public @ResponseBody
    JSONObject receiveGoods(String data){
        JSONObject json = new JSONObject();
        try {
            //参数处理
            OrdReturnExchangeExt ordReturnExchange = JSON.parseObject(data, OrdReturnExchangeExt.class);
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            //登录人
            String loginId = (String) userMap.get("loginId");
            //设定用户信息
            ordReturnExchange.setApplyAnswerUserId(loginId);
            ordReturnExchange.setUpdateUserId(loginId);
            //确认已收货处理
            json = ordMasterService.returnGoodsReceive(ordReturnExchange);
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
     * 卖家确认退款
     * @param data
     * @return json
     */
    @RequestMapping("refund")
    public @ResponseBody
    JSONObject refund(String data){
        JSONObject json = new JSONObject();
        try {
            //参数处理
            OrdReturnExchangeExt ordReturnExchange = JSON.parseObject(data, OrdReturnExchangeExt.class);
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            //登录人
            String loginId = (String) userMap.get("loginId");
            //设定用户信息
            ordReturnExchange.setApplyAnswerUserId(loginId);
            ordReturnExchange.setUpdateUserId(loginId);
            //确认已收货处理
            json = ordMasterService.returnGoodsRefund(ordReturnExchange);
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
     * 卖家确认付款
     * @param data
     * @return json
     */
    @RequestMapping("pay")
    public @ResponseBody
    JSONObject pay(String data){
        OrdReturnExchange ordReturnExchange = JSON.parseObject(data, OrdReturnExchange.class);
        JSONObject json = new JSONObject();
        try {
            //拆单退货商品卖家确认收货
            ordMasterService.submitReturnMoney(ordReturnExchange, PamsDataContext.get());
            json.put("resultCode", Constants.NORMAL);
        }catch (ExceptionBusiness eb){
            json.put("resultCode", Constants.INVALID);
            json.put("resultMessage", eb.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 修改退货单门店信息
     *
     * @param data
     * @return
     */
    @RequestMapping("editSubOrderStore")
    public
    @ResponseBody
    JSONObject editSubOrderStore(String data) {
        JSONObject json;
        try {
            OrdReturnExchange form = (OrdReturnExchange) JSON.parseObject(data, OrdReturnExchange.class);
            json = ordMasterService.editSubOrderStore(form);

        } catch (Exception e) {

            json = new JSONObject();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 取得凭证信息
     *
     * @param data
     * @return
     */
    @RequestMapping("proof")
    public
    @ResponseBody
    JSONObject proof(String data) {
        JSONObject json = new JSONObject();
        try {
            JSONObject param = JSONObject.parseObject(data);
            if (param == null) {
                throw new ExceptionErrorParam("参数为空");
            }
            String orderId = param.getString("orderId");
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            json = ordMasterService.getProofInfo(orderId,userMap);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }

        return json;
    }
}
