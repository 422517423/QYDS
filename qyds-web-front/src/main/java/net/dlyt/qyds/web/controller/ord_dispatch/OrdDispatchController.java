package net.dlyt.qyds.web.controller.ord_dispatch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.form.DispatchStoreForm;
import net.dlyt.qyds.common.form.OrdDispatchForm;
import net.dlyt.qyds.common.form.OrdMasterForm;
import net.dlyt.qyds.common.form.OrdSubmitForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.BnkMasterService;
import net.dlyt.qyds.web.service.OrdDispatchService;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * Created by YiLian on 2016/10/10.
 */
@Controller
@RequestMapping("/ord_dispatch")
public class OrdDispatchController {

    @Autowired
    private OrdDispatchService ordDispatchService;

    @Autowired
    private BnkMasterService bnkMasterService;

    /**
     * 获取未派单的订单列表信息
     *
     * @param form
     * @return json
     */
    @RequestMapping("getDispatchOrdMasterList")
    public
    @ResponseBody
    JSONObject getDispatchOrdMasterList(OrdMasterForm form) {
        JSONObject json;

        try {
            //获取派单列表
            json = ordDispatchService.getDispatchOrdMasterList(form);
        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    @RequestMapping("getDispatchOrdMasterInfo")
    public
    @ResponseBody
    JSONObject getDispatchOrdMasterInfo(String orderId) {
        JSONObject json;
        try {
            if (StringUtils.isEmpty(orderId)) {
                throw new ExceptionErrorParam("缺少主键参数订单ID");
            }

            json = ordDispatchService.getDispatchOrdMasterInfo(orderId);

        } catch (Exception e) {

            json = new JSONObject();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 根据订单id获取子订单信息
     *
     * @param form
     * @return json
     */
    @RequestMapping("getDispatchOrdSubInfo")
    public
    @ResponseBody
    JSONObject getDispatchOrdSubInfo(OrdMasterForm form) {
        JSONObject json;
        try {
            if (null == form || StringUtils.isEmpty(form.getOrderId())) {
                throw new ExceptionErrorParam("缺少主键参数订单ID");
            }

            SysUser sysUser = (SysUser) PamsDataContext.getSysUser();
            json = ordDispatchService.getDispatchOrdSubInfo(form.getOrderId(), sysUser.getOrgId());

        } catch (Exception e) {

            json = new JSONObject();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 获取门店信息
     *
     * @return
     */
    @RequestMapping("getDispatchStoreList")
    public
    @ResponseBody
    JSONObject getDispatchStoreList(OrdDispatchForm form) {

        return bnkMasterService.getDispatchStoreList(form);
    }

    /**
     * 获取所有门店信息
     *
     * @return
     */
    @RequestMapping("getAllStoreList")
    public
    @ResponseBody
    JSONObject getAllStoreList(OrdDispatchForm form) {

        return bnkMasterService.getAllStoreList(form);
    }


    /**
     * 门店地址列表取得
     *
     * @param data
     * @return
     */
    @RequestMapping("getOrgAddressList")
    @ResponseBody
    public JSONObject getAddressList(String data) {
        JSONObject json = new JSONObject();
        try {
            ErpStore store = (ErpStore) JSON.parseObject(data, ErpStore.class);
            json = bnkMasterService.getOrgAddressList(store);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 指派门店
     *
     * @param data
     * @return
     */
    @RequestMapping("dispatchOrderToStore")
    public
    @ResponseBody
    JSONObject dispatchOrderToStore(String data) {
        JSONObject json;
        try {
            OrdDispatchForm form = (OrdDispatchForm) JSON.parseObject(data, OrdDispatchForm.class);
            SysUser sysUser = (SysUser) PamsDataContext.getSysUser();
            form.setUpdateUserId(String.valueOf(sysUser.getUserId()));
            json = ordDispatchService.dispatchOrderToStore(form);

        } catch (Exception e) {

            json = new JSONObject();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }


    /**
     * 修改自提的门店
     *
     * @param data
     * @return
     */
    @RequestMapping("changeOrderToStore")
    public
    @ResponseBody
    JSONObject changeOrderToStore(String data) {
        JSONObject json;
        try {
            OrdDispatchForm form = (OrdDispatchForm) JSON.parseObject(data, OrdDispatchForm.class);
            SysUser sysUser = (SysUser) PamsDataContext.getSysUser();
            form.setUpdateUserId(String.valueOf(sysUser.getUserId()));
            json = ordDispatchService.changeOrderToStore(form);

        } catch (Exception e) {

            json = new JSONObject();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 取消指派门店
     *
     * @param data
     * @return
     */
    @RequestMapping("cancelDispatch")
    public
    @ResponseBody
    JSONObject cancelDispatch(String data) {
        JSONObject json;
        try {
            OrdDispatchForm form = (OrdDispatchForm) JSON.parseObject(data, OrdDispatchForm.class);
            SysUser sysUser = (SysUser) PamsDataContext.getSysUser();
            form.setUpdateUserId(String.valueOf(sysUser.getUserId()));
            json = ordDispatchService.cancelDispatch(form);

        } catch (Exception e) {

            json = new JSONObject();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 获取已分派的待发货订单列表信息
     *
     * @param form
     * @return json
     */
    @RequestMapping("getProcessedOrdMasterList")
    public
    @ResponseBody
    JSONObject getProcessedOrdMasterList(OrdDispatchForm form) {
        JSONObject json;

        try {
            SysUser sysUser = (SysUser) PamsDataContext.getSysUser();
            form.setDispatchStore(sysUser.getOrgId());
            json = ordDispatchService.getProcessedOrdMasterList(form);
        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 待发货订单详细
     *
     * @param orderId
     * @return
     */
    @RequestMapping("getProcessedOrdMasterInfo")
    public
    @ResponseBody
    JSONObject getProcessedOrdMasterInfo(String orderId) {
        JSONObject json;
        try {
            if (StringUtils.isEmpty(orderId)) {
                throw new ExceptionErrorParam("缺少主键参数订单ID");
            }

            json = ordDispatchService.getProcessedOrdMasterInfo(orderId);

        } catch (Exception e) {

            json = new JSONObject();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 根据订单id获取子订单信息(待发货)
     *
     * @param form
     * @return json
     */
    @RequestMapping("getProcessedOrdList")
    public
    @ResponseBody
    JSONObject getProcessedOrdList(OrdMasterForm form) {
        JSONObject json;
        try {
            if (null == form || StringUtils.isEmpty(form.getOrderId())) {
                throw new ExceptionErrorParam("缺少主键参数订单ID");
            }

            SysUser sysUser = (SysUser) PamsDataContext.getSysUser();

            json = ordDispatchService.getProcessedOrdList(form.getOrderId(), sysUser.getOrgId());

        } catch (Exception e) {

            json = new JSONObject();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 根据子订单获取待发货基础信息显示
     *
     * @param data
     * @return json
     */
    @RequestMapping("getSubOrderDeliverInfo")
    public
    @ResponseBody
    JSONObject getSubOrderDeliverInfo(String data) {
        JSONObject json;
        try {
            OrdMasterForm form = (OrdMasterForm) JSON.parseObject(data, OrdMasterForm.class);
            if (null == form
                    || StringUtils.isEmpty(form.getOrderId())
                    || StringUtils.isEmpty(form.getSubOrderId())) {
                throw new ExceptionErrorParam("缺少主键参数订单ID");
            }

            SysUser sysUser = (SysUser) PamsDataContext.getSysUser();

            json = ordDispatchService.getSubOrderDeliverInfo(form, sysUser.getOrgId());

        } catch (Exception e) {

            json = new JSONObject();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }


    /**
     * 门店电商发货
     *
     * @param data
     * @return json
     */
    @RequestMapping("deliverSubOrderItem")
    public
    @ResponseBody
    JSONObject deliverSubOrderItem(String data) {
        JSONObject json;
        try {
            OrdSubList ordSubItem = (OrdSubList) JSON.parseObject(data, OrdSubList.class);
            if (null == ordSubItem
                    || StringUtils.isEmpty(ordSubItem.getOrderId())
                    || StringUtils.isEmpty(ordSubItem.getSubOrderId())
                  // TODO: 2017/12/8  去掉运单号校验
                  /*  || StringUtils.isEmpty(ordSubItem.getExpressNo())*/) {
                throw new ExceptionErrorParam("缺少发货参数");
            }
            /*OrdSubList ordSubItem = new OrdSubList();
            ordSubItem.setOrderId("eb52dc57-e491-4a3a-b677-18dec493f9b9");
            ordSubItem.setSubOrderId("3ac56400-c567-4334-9a15-6dc48988a099");*/

            SysUser sysUser = (SysUser) PamsDataContext.getSysUser();
            json = ordDispatchService.deliverSubOrderItem(ordSubItem, sysUser);

        } catch (Exception e) {

            json = new JSONObject();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 门店电商拒绝发货
     *
     * @param data
     * @return json
     */
    @RequestMapping("rejectDeliverSubOrderItem")
    public
    @ResponseBody
    JSONObject rejectDeliverSubOrderItem(String data) {
        JSONObject json;
        try {
            OrdDispatch ordDispatch = (OrdDispatch) JSON.parseObject(data, OrdDispatch.class);
            if (null == ordDispatch
                    || StringUtils.isEmpty(ordDispatch.getOrderId())
                    || StringUtils.isEmpty(ordDispatch.getSubOrderId())) {
                throw new ExceptionErrorParam("缺少订单参数");
            }

            SysUser sysUser = (SysUser) PamsDataContext.getSysUser();

            json = ordDispatchService.rejectDeliverSubOrderItem(ordDispatch, sysUser);

        } catch (Exception e) {

            json = new JSONObject();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 门店申请调货
     *
     * @param data
     * @return json
     */
    @RequestMapping("seasoningDispatch")
    public
    @ResponseBody
    JSONObject seasoningDispatch(String data) {
        JSONObject json;
        try {
            OrdDispatch ordDispatch = (OrdDispatch) JSON.parseObject(data, OrdDispatch.class);
            if (null == ordDispatch
                    || StringUtils.isEmpty(ordDispatch.getOrderId())
                    || StringUtils.isEmpty(ordDispatch.getSubOrderId())) {
                throw new ExceptionErrorParam("缺少订单参数");
            }

            SysUser sysUser = (SysUser) PamsDataContext.getSysUser();

            json = ordDispatchService.seasoningDispatch(ordDispatch, sysUser);

        } catch (Exception e) {

            json = new JSONObject();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 门店申请调货审批列表
     *
     * @param ordSubListExt
     * @return json
     */
    @RequestMapping("getUpSeasoningDispatchList")
    public
    @ResponseBody
    JSONObject getUpSeasoningDispatchList(OrdSubListExt ordSubListExt) {
        JSONObject json;
        try {
            json = ordDispatchService.getUpSeasoningDispatchList(ordSubListExt);

        } catch (Exception e) {

            json = new JSONObject();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 门店申请调货
     *
     * @param data
     * @return json
     */
    @RequestMapping("seasoningComplete")
    public
    @ResponseBody
    JSONObject seasoningComplete(String data) {
        JSONObject json;
        try {
            OrdDispatch ordDispatch = (OrdDispatch) JSON.parseObject(data, OrdDispatch.class);
            if (null == ordDispatch
                    || StringUtils.isEmpty(ordDispatch.getSubOrderId())) {
                throw new ExceptionErrorParam("缺少订单参数");
            }

            SysUser sysUser = (SysUser) PamsDataContext.getSysUser();

            json = ordDispatchService.seasoningComplete(ordDispatch, sysUser);

        } catch (Exception e) {

            json = new JSONObject();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }


    /**
     * 派单履历
     *
     * @param ordDispatch
     * @return
     */
    @RequestMapping("getDispatchHistory")
    @ResponseBody
    public JSONObject getDispatchHistory(OrdDispatch ordDispatch) {
        JSONObject json = new JSONObject();
        try {
            if (null == ordDispatch
                    || StringUtils.isEmpty(ordDispatch.getOrderId())
                    || StringUtils.isEmpty(ordDispatch.getSubOrderId())) {
                throw new ExceptionErrorParam("缺少订单参数");
            }

            json = ordDispatchService.getDispatchHistory(ordDispatch);
        } catch (Exception e) {
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
            json = ordDispatchService.getProofInfo(orderId, userMap);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }

        return json;
    }

}
