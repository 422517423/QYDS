package net.dlyt.qyds.web.controller.ord_dispatch;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.OrdTransferListExt;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.BnkMasterService;
import net.dlyt.qyds.web.service.OrdTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * Created by ZLH on 2016/12/22.
 */
@Controller
@RequestMapping("/ord_transfer")
public class OrdTransferController {

    @Autowired
    private OrdTransferService ordTransferService;
    @Autowired
    private BnkMasterService bnkMasterService;

    /**
     * 获取门店收货信息
     *
     * @return json
     */
    @RequestMapping("getAddressDelivery")
    public
    @ResponseBody
    JSONObject getAddressDelivery(OrdTransferList form) {
        JSONObject json = new JSONObject();
        try {
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            //获取门店地址信息
            json = ordTransferService.getById(form.getOrderTransferId());
        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 获取门店收货信息
     *
     * @return json
     */
    @RequestMapping("getAddress")
    public
    @ResponseBody
    JSONObject getAddress(OrdTransferList form) {
        JSONObject json = new JSONObject();
        try {
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            //获取门店地址信息
            json = ordTransferService.getApplyAddress(userMap, form.getSubOrderId());
        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 提交调货申请
     *
     * @return json
     */
    @RequestMapping("apply")
    public
    @ResponseBody
    JSONObject apply(OrdTransferList form) {
        JSONObject json = new JSONObject();
        try {
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            //填入用户信息
            form.setApplyStore(userMap.get("orgId").toString());
            form.setApplyUser(userMap.get("loginId").toString());
            form.setInsertUserId(userMap.get("userId").toString());
            form.setUpdateUserId(userMap.get("userId").toString());
            json = ordTransferService.apply(form);
        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 获取分派调货订单列表
     *
     * @return json
     */
    @RequestMapping("getDispatchList")
    public
    @ResponseBody
    JSONObject getDispatchList(OrdTransferListExt param) {
        JSONObject json = new JSONObject();
        try {
            //获取分派调货订单列表
            json = ordTransferService.getDispatchList(param);
        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 获取门店列表
     *
     * @return json
     */
    @RequestMapping("getStoreList")
    public
    @ResponseBody
    JSONObject getStoreList(BnkMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            //获取分派调货订单列表
            json = bnkMasterService.getSkuStoreList(form);
        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 分派门店列表
     *
     * @return json
     */
    @RequestMapping("dispatch")
    public
    @ResponseBody
    JSONObject dispatch(OrdTransferList form) {
        JSONObject json = new JSONObject();
        try {
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            //获取分派调货订单列表
            form.setDispatchUser(userMap.get("loginId").toString());
            form.setUpdateUserId(userMap.get("userId").toString());
            json = ordTransferService.dispatch(form);
        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 获取门店分派调货订单列表
     *
     * @return json
     */
    @RequestMapping("getDeliveryList")
    public
    @ResponseBody
    JSONObject getDeliveryList(OrdTransferListExt form) {
        JSONObject json = new JSONObject();
        try {
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            form.setDispatchStore(userMap.get("orgId").toString());
            //获取门店分派调货订单列表
            json = ordTransferService.getDispatchList(form);
        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 拒绝调货
     *
     * @return json
     */
    @RequestMapping("refuse")
    public
    @ResponseBody
    JSONObject refuse(OrdTransferList form) {
        JSONObject json = new JSONObject();
        try {
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            //获取分派调货订单列表
            form.setRefuseUser(userMap.get("loginId").toString());
            form.setRefuseStore(userMap.get("orgId").toString());
            form.setUpdateUserId(userMap.get("userId").toString());
            json = ordTransferService.refuse(form);
        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 接受调货
     *
     * @return json
     */
    @RequestMapping("accept")
    public
    @ResponseBody
    JSONObject accept(OrdTransferList form) {
        JSONObject json = new JSONObject();
        try {
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            //获取分派调货订单列表
            form.setAcceptUser(userMap.get("loginId").toString());
            form.setUpdateUserId(userMap.get("userId").toString());
            json = ordTransferService.accept(form);
        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 调货发货
     *
     * @return json
     */
    @RequestMapping("delivery")
    public
    @ResponseBody
    JSONObject delivery(OrdTransferList form) {
        JSONObject json = new JSONObject();
        try {
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            //获取分派调货订单列表
//            form.setDispatchStore(userMap.get("orgId").toString());
            form.setDeliveryUser(userMap.get("loginId").toString());
            form.setUpdateUserId(userMap.get("userId").toString());
            // 发货人姓名
            form.setDeliveryContactor(userMap.get("userName").toString());
            json = ordTransferService.delivery(form);
        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 获取门店收货调货订单列表
     *
     * @return json
     */
    @RequestMapping("getReceiveList")
    public
    @ResponseBody
    JSONObject getReceiveList(OrdTransferListExt form) {
        JSONObject json = new JSONObject();
        try {
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            form.setApplyStore(userMap.get("orgId").toString());
            //获取门店分派调货订单列表
            json = ordTransferService.getDispatchList(form);
        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 调货收货
     *
     * @return json
     */
    @RequestMapping("receive")
    public
    @ResponseBody
    JSONObject receive(OrdTransferList form) {
        JSONObject json = new JSONObject();
        try {
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            //获取分派调货订单列表
//            form.setApplyStore(userMap.get("orgId").toString());
            form.setReceiveUser(userMap.get("loginId").toString());
            form.setUpdateUserId(userMap.get("userId").toString());
            json = ordTransferService.receive(form);
        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 调货收货
     *
     * @return json
     */
    @RequestMapping("detail")
    public
    @ResponseBody
    JSONObject detail(OrdTransferListExt form) {
        JSONObject json = new JSONObject();
        try {
            //获取指定调货订单
            json = ordTransferService.getDetailById(form.getOrderTransferId());
        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }
}
