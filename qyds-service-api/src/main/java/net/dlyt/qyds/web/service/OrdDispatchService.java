package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.OrdDispatch;
import net.dlyt.qyds.common.dto.OrdSubList;
import net.dlyt.qyds.common.dto.OrdSubListExt;
import net.dlyt.qyds.common.dto.SysUser;
import net.dlyt.qyds.common.form.OrdDispatchForm;
import net.dlyt.qyds.common.form.OrdMasterForm;

import java.util.Map;

/**
 * Created by YiLian on 2016/10/10.
 */
public interface OrdDispatchService {

    /**
     * 根据订单ID或订单编码获取派单列表
     *
     * @param form
     * @return
     */
    JSONObject getDispatchOrdMasterList(OrdMasterForm form);


    /**
     * 根据订单ID取得订单主表信息
     *
     * @param orderId
     * @return
     */
    JSONObject getDispatchOrdMasterInfo(String orderId);

    /**
     * 根据订单id获取子订单信息
     *
     * @param orderId
     * @return
     */
    JSONObject getDispatchOrdSubInfo(String orderId, String orgId);

    /**
     * 分派订单到门店
     *
     * @param form
     * @return
     */
    JSONObject dispatchOrderToStore(OrdDispatchForm form);

    /**
     * 修改自提的门店
     *
     * @param form
     * @return
     */
    JSONObject changeOrderToStore(OrdDispatchForm form);


    /**
     * 店铺获取对应的待发货列表
     *
     * @param form
     * @return
     */
    JSONObject getProcessedOrdMasterList(OrdDispatchForm form);

    /**
     * 待发货订单详细
     *
     * @param orderId
     * @return
     */
    JSONObject getProcessedOrdMasterInfo(String orderId);

    /**
     * 根据订单id获取子订单信息(待发货)
     *
     * @param orderId
     * @param orgId
     * @return
     */
    JSONObject getProcessedOrdList(String orderId, String orgId);

    /**
     * 根据子订单获取待发货基础信息显示
     *
     * @param form
     * @param orgId
     * @return
     */
    JSONObject getSubOrderDeliverInfo(OrdMasterForm form, String orgId);

    /**
     * 门店电商发货
     *
     * @param ordSubItem
     * @param sysUser
     * @return
     */
    JSONObject deliverSubOrderItem(OrdSubList ordSubItem, SysUser sysUser);

    /**
     * 门店电商拒绝发货
     *
     * @param ordDispatch
     * @param sysUser
     * @return
     */
    JSONObject rejectDeliverSubOrderItem(OrdDispatch ordDispatch, SysUser sysUser);

    /**
     * 门店申请调货
     *
     * @param ordDispatch
     * @param sysUser
     * @return
     */
    JSONObject seasoningDispatch(OrdDispatch ordDispatch, SysUser sysUser);

    /**
     * 调货审批列表
     * @param ordSubListExt
     * @return
     */
    JSONObject getUpSeasoningDispatchList(OrdSubListExt ordSubListExt);



    /**
     * 调货完成
     *
     * @param ordDispatch
     * @param sysUser
     * @return
     */
    JSONObject seasoningComplete(OrdDispatch ordDispatch, SysUser sysUser);

    /**
     * 派单履历
     *
     * @param ordDispatch
     * @return
     */
    JSONObject getDispatchHistory(OrdDispatch ordDispatch);

    JSONObject getProofInfo(String orderId, Map<String, Object> userMap);

    /**
     * 取消门店指派
     *
     * @param form
     * @return
     */
    JSONObject cancelDispatch(OrdDispatchForm form);
}
