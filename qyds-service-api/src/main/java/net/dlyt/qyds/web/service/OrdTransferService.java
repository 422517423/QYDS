package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.OrdTransferList;
import net.dlyt.qyds.common.dto.ext.OrdTransferListExt;

import java.util.Map;

/**
 * Created by ZLH on 2016/12/22.
 */
public interface OrdTransferService {

    /**
     * 取得调货地址
     *
     * @param userMap
     * @return
     */
    JSONObject getApplyAddress(Map<String, Object> userMap,String subOrderId);

    /**
     * 取得发货地址
     *
     * @param TransferId
     * @return
     */
    JSONObject getById(String TransferId);

    /**
     * 提交调货申请
     *
     * @param form
     * @return
     */
    JSONObject apply(OrdTransferList form);

    /**
     * 获取分派调货订单列表
     *
     * @param form
     * @return
     */
    JSONObject getDispatchList(OrdTransferListExt form);

    /**
     * 调货分派
     *
     * @param form
     * @return
     */
    JSONObject dispatch(OrdTransferList form);

    /**
     * 拒绝分派
     *
     * @param form
     * @return
     */
    JSONObject refuse(OrdTransferList form);

    /**
     * 接受分派
     *
     * @param form
     * @return
     */
    JSONObject accept(OrdTransferList form);

    /**
     * 调货发货
     *
     * @param form
     * @return
     */
    JSONObject delivery(OrdTransferList form);

    /**
     * 调货收货
     *
     * @param form
     * @return
     */
    JSONObject receive(OrdTransferList form);

    /**
     * 取得详细
     *
     * @param id
     * @return
     */
    JSONObject getDetailById(String id);
}
