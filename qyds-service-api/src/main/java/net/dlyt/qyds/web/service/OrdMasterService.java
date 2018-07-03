package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.MmbShoppingSKuExt;
import net.dlyt.qyds.common.dto.ext.OrdHistoryExt;
import net.dlyt.qyds.common.form.OrdLogisticsForm;
import net.dlyt.qyds.common.form.OrdMasterForm;

import java.util.List;
import java.util.Map;

/**
 * Created by wenxuechao on 16/7/23.
 */
public interface OrdMasterService {

    //活动列表  优惠劵列表
    JSONObject getActivityCouponList(String data);

    /**
     * 获取该商铺的所有订单数据
     * @param ordMasterExt
     * @return
     */
    List<OrdMasterExt> getAllDatas(OrdMasterExt ordMasterExt);

    /**
     * 获取该商铺的订单数据条数
     * @param ordMasterExt
     * @return
     */
    int getAllDatasCount(OrdMasterExt ordMasterExt);

    /**
     * 根据主键获取订单数据
     * @param orderId
     * @return
     */
    OrdMasterExt selectByPrimaryKey(String orderId);

    /**
     * 根据订单id获取订单商品信息
     * @param orderId
     * @return
     */
    List<OrdList> selectOrderGoodsInfo(String orderId);

    /**
     * 根据订单id获取子订单信息
     * @param orderId
     * @return
     */
    List<OrdSubListExt> selectOrderSubInfo(String orderId);

    /**
     * 根据用户id获取订单信息
     * @模块 微信端接口
     * @param ordMasterExt
     * @return
     */
    List<OrdMasterExt> getOrdListByUserId(OrdMasterExt ordMasterExt);

    /**
     * 根据订单id获取订单商品信息
     * @模块 微信端接口
     * @param ordMasterExt
     * @return
     */
    List<OrdMasterExt> getOrdDetailByOrderId(OrdMasterExt ordMasterExt);

    /**
     * 根据订单ID取得商品SKU详细信息
     * @param detailId
     * @return
     */
    List<MmbShoppingSKuExt> getSKUListByOrderDetailId(String detailId);

    /**
     * 主订单处确认收货按钮
     * @param ordMasterExt
     * @return
     */
    JSONObject confirmReceiptInMaster(OrdMasterExt ordMasterExt);

    /**
     * 物流列表处确认收货按钮
     *
     * @param form memberId:会员ID
     *             orderId:订单编号 : 收货用标识符-门店自提
     *             expressNo:邮件编号 : 收货用标识符-物流送货
     * @return
     * @功能位置 物流列表单个物流确认收货
     */
    @Deprecated
    JSONObject confirmReceiptInSub(OrdLogisticsForm form);

    /**
     * 买家删除订单
     * @param ordMasterExt
     * @return
     */
    String deleteOrder(OrdMasterExt ordMasterExt);

    /**
     * 买家取消订单
     * @param ordMasterExt
     * @return
     */
    JSONObject cancelOrder(OrdMasterExt ordMasterExt);

    /**
     * @功能位置    主订单初始申请退货
     * @触发条件    1、没有活动的场合全单退货
     *             2、有活动的场合全单退货
     * @param ordReturnExchangeExt
     * @return
     */
    JSONObject applyReturnGoods(OrdReturnExchangeExt ordReturnExchangeExt);

    /**
     * @功能位置    买家子订单初始申请退货
     * @功能位置    子订单退货
     * @触发条件    没有活动的场合子单退货
     */
    JSONObject applyReturnSubGoods(OrdReturnExchangeExt ordReturnExchangeExt);

    /**
     * 买家查阅退货信息
     * @param ordReturnExchangeExt
     * @return
     */
    List<OrdReturnExchangeExt> viewApplyReturnGoods(OrdReturnExchangeExt ordReturnExchangeExt);

    /**
     * 买家主订单审批通过后申请退货
     * @param ordReturnExchangeExt
     * @return
     */
    String reSubmitReturnGoodsByOrderId(OrdReturnExchangeExt ordReturnExchangeExt);

    /**
     * 买家立即购买场合确认订单
     * @param ordMasterExt
     * @return
     */
    OrdConfirmExt confirmOrderFromGoods(OrdMasterExt ordMasterExt);

    /**
     * 买家立即购买套装场合确认订单
     * @param ordMasterExt
     * @return
     */
    OrdConfirmExt confirmOrderFromSuitGoods(OrdMasterExt ordMasterExt);

    /**
     * 买家购物车场合确认订单
     * @param ordMasterExt
     * @return
     */
    OrdConfirmExt confirmOrderFromBag(OrdMasterExt ordMasterExt);

    /**
     * 提交生成订单
     * @param ordMasterExt
     * @return
     */
    OrdMaster submitOrder(OrdMasterExt ordMasterExt) throws Exception;

    /**
     * 卖家获取退货信息
     * @param ordReturnExchangeExt
     * @return
     */
    List<OrdReturnExchangeExt> getReturnGoodsInfo(OrdReturnExchangeExt ordReturnExchangeExt);

    /**
     * 订单发货处理
     * @param ordMasterExt
     * @return
     */
    JSONObject sendOrder(String ordMasterExt);

    /**
     * 订单物流信息查询
     * @param orderId
     * @return
     */
    JSONObject queryLogisticsInfo(String orderId);

    /**
     * 退货商品卖家确认收货
     * @param orderId
     */
    void acceptReturnAllGoods(String orderId, Map<String, Object> userMap);

    /**
     * 拆单退货,卖家确认收货
     * @param subOrderId
     */
    void acceptReturnSubGoods(String subOrderId, Map<String, Object> userMap);

    /**
     * 卖家单个商品确认退单
     * @param ordReturnExchange
     */
    void submitReturnMoney(OrdReturnExchange ordReturnExchange, Map<String, Object> userMap);

    /**
     * 定时任务
     * 清理30分钟未支付的订单
     * @param orderId
     */
    void cancancelOrderQuartz(String orderId);

    /**
     * 确认收货7天不允许再次提交退货
     * @param orderCode
     */
    void cancelReturnOrderQuartz(String orderCode);

    JSONObject getReturnInfo(OrdReturnExchangeExt ordReturnExchange);

    /**
     * 买家主订单申请退款
     * @功能位置    主订单退款
     * @触发条件    1、订单付款成功
     *             2、未发货
     * @param data
     * @return
     */
    JSONObject applyRefund(String data);

    /**
     * 取得退款审批订单列表
     * @功能位置    退款审批
     * @触发条件    1、订单付款成功
     *             2、未发货
     *             3、会员已申请退款
     * @param form
     * @return
     */
    JSONObject getRefundApproveList(OrdMasterForm form);

    /**
     * 取得退款审批订单列表
     * @功能位置    退款审批
     * @触发条件    1、订单付款成功
     *             2、未发货
     *             3、会员已申请退款
     * @param form
     * @return
     */
    JSONObject getRefundPage(OrdReturnExchangeExt form);

    JSONObject getReturnInfoById(String id);

    /**
     * 卖家审批退单
     * @param ordReturnExchange
     */
    JSONObject refundApprove(OrdReturnExchange ordReturnExchange);

    /**
     * 卖家退单付款
     * @param ordReturnExchange
     */
    JSONObject refundPay(OrdReturnExchange ordReturnExchange);

    /**
     * 退货待收货列表
     * @param form
     * @return
     */
    JSONObject getReturnGoodsReceiveList(OrdReturnExchangeExt form);

    /**
     * 退货审批
     * @param ordReturnExchange
     * @return
     */
    JSONObject returnGoodsApprove(OrdReturnExchangeExt ordReturnExchange);

    /**
     * 退货确认已收货
     * @param ordReturnExchange
     * @return
     */
    JSONObject returnGoodsReceive(OrdReturnExchangeExt ordReturnExchange);

    /**
     * 退货确认已退款
     * @param ordReturnExchange
     * @return
     */
    JSONObject returnGoodsRefund(OrdReturnExchangeExt ordReturnExchange);

    /**
     * 确认电话号码是否存在
     * @param tel
     * @return
     */
    JSONObject checkInputTel(String tel);


    List<OrdHistoryExt> getOrderHistoryList(String orderId);

    JSONObject getSelfOrderList(OrdMasterForm form, SysUser sysUser);

    JSONObject editSubOrderStore(OrdReturnExchange form);

    JSONObject getOrderCountByMemberId(OrdMasterExt ordMasterExt);

    JSONObject getOrderListByMemberId(OrdMasterExt ordMasterExt);

    JSONObject getOrderListOffLineByMemberId(OrdMasterExt ordMasterExt);

    JSONObject getProofInfo(String orderId, Map<String, Object> userMap);

    JSONObject receiveOrder15DaysAgo();

    JSONObject finishOrder7DaysAgo();

    JSONObject closeOrder30DaysAgo();

    JSONObject getOrderListOffLineByMemberIdForWeb(String memberId);

    JSONObject getReportSale(OrdMasterExt ordMasterExt);

    JSONObject getReportReturn(OrdMasterExt ordMasterExt);

    /**
     * 支付前校验秒杀活动商品信息
     * @param orderId
     * @param orderCode
     * @return
     */
    void checkSecActivityOrderInfo(String orderId, String orderCode);

    /**
     * 导出excel
     */
    List<OrdMasterExt> excelExport(OrdMasterExt form);

    void checkConfirmOrderFromGoods(OrdMasterExt ordMasterExt);

    void checkConfirmOrderFromBag(OrdMasterExt ordMasterExt);
}
