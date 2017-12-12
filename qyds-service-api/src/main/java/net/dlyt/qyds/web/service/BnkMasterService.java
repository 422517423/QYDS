package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.BnkMaster;
import net.dlyt.qyds.common.dto.BnkMasterExt;
import net.dlyt.qyds.common.dto.ErpStore;
import net.dlyt.qyds.common.form.BnkMasterForm;
import net.dlyt.qyds.common.form.BnkRecordsForm;
import net.dlyt.qyds.common.form.DispatchStoreForm;
import net.dlyt.qyds.common.form.OrdDispatchForm;

import java.util.List;

/**
 * Created by wy on 2016/8/2.
 */
public interface BnkMasterService {
    /*获取商品库存列表*/
    JSONObject selectAll(BnkMasterForm form);

    /*根据ID获取商品库存信息*/
    JSONObject selectByPrimaryKey(String bankId);

    /*修改商品库存信息*/
    JSONObject save(String data);

    /** 选择门店列表 */
    JSONObject getOrgList(String data);

    /**
     * 取消订单进行库存的增加
     * @触发条件  电商平台商品
     * @param orderId,memberId
     *
     */
    void cancelOrderAddBank(String orderId, String memberId);

    /**
     * 提交订单进行库存的减少
     * @param orderId
     * @param memberId
     */
//    void submitOrderReduceBank(String orderId, String memberId) throws Exception;

    /**
     * 退货确认收货增加库存
     * @param orderId
     * @param subOrderId
     * @param memberId
     */
//    void returnOrderAddBank(String orderId, String subOrderId, String memberId) throws Exception;

    /**
     * 门店地址
     * @param store
     * @return
     */
    JSONObject getOrgAddressList(ErpStore store);

    /**
     * 根据订单ID或子订单ID获取可发货门店列表
     *
     * @param data
     * @return
     */
    JSONObject getDispatchStoreList(OrdDispatchForm data);

    JSONObject getAllStoreList(OrdDispatchForm form);

    JSONObject getReportBank();

    JSONObject getSkuStoreList(BnkMasterExt form);
}
