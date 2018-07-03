package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.OrdMaster;
import net.dlyt.qyds.common.dto.OrdMasterExt;
import net.dlyt.qyds.common.form.OrdDispatchForm;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wenxuechao on 16/7/23.
 */

@Repository
public interface OrdMasterMapperExt {

    /**
     * 根据条件获取订单数据
     * @param ordMasterExt
     * @return
     */
    List<OrdMasterExt> getAllDatas(OrdMasterExt ordMasterExt);

    OrdMaster selectByOrderCode(String orderCode);

    /**
     * 跟根据条件获取订单条数
     * @param ordMasterExt
     * @return
     */
    int getAllDatasCount(OrdMasterExt ordMasterExt);

    /**
     * 主订单确认收货按钮
     * @param ordMasterExt
     * @return
     */
    int confirmReceiptInMaster(OrdMasterExt ordMasterExt);

    /**
     * 更新主订单的删除标记
     * @param ordMasterExt
     * @return
     */
    int deleteOrder(OrdMasterExt ordMasterExt);

    /**
     * 更新主订单为取消订单
     * @param ordMasterExt
     * @return
     */
    int cancelOrder(OrdMasterExt ordMasterExt);

    /**
     * 更新主订单状态为订单退单中
     * @param ordMasterExt
     * @return
     */
    int applyReturnGoods(OrdMasterExt ordMasterExt);

    /**
     * 更新订单主表订单状态为申请驳回
     * @param ordMaster
     */
    void changeReturnGoods(OrdMaster ordMaster);

    /**
     * 卖家更新主订单发货状态为退后已收货
     * @param ordMaster
     */
    void acceptReturnGoods(OrdMaster ordMaster);

    /**
     * 卖家更新主订单付款状态为退款完成
     * @param ordMaster
     */
    void submitReturnMoney(OrdMaster ordMaster);

    /**
     * 定时任务
     * 清理30分钟未支付的订单
     * @param orderCode
     */
    void cancancelOrderQuartz(String orderCode);

    /**
     * 根据订单编号获取商品信息
     * @param strOrderCode
     * @return
     */
    OrdMaster getOrderInfoByCode(String strOrderCode);

    /**
     * 更新订单为付款成功
     * @param ordMaster
     */
    void paySuccess(OrdMaster ordMaster);

    /**
     * 查询订单主表信息
     * @param orderId
     * @return
     */
    OrdMasterExt selectOrdInfo(String orderId);

    List<OrdMasterExt> selectSendFail();

    OrdMasterExt selectSendFailById(String orderId);

    List<OrdMasterExt> selectSendFailReturn();

    OrdMasterExt selectSendFailReturnById(String orderId);

    OrdMasterExt selectSendReturnById(String orderId);

    OrdMasterExt selectSendById(String orderId);

    /**
     * 确认收货7天后,不允许退货
     * @param orderCode
     */
    void cancelReturnOrderQuartz(String orderCode);

    /**
     * 发货30天后不允许退货
     * @param orderCode
     */
    void ThirtyUnReturnOrderQuartz(String orderCode);

    /**
     * 更新订单状态-退款申请
     * @param orderId
     * @return
     */
    void applyRefund(String orderId);

    /**
     * 查询未派单列表[包含已指派未发货]
     *
     * @param ordMasterExt
     * @return
     */
    List<OrdMasterExt> selectDispatchOrdMasterList(OrdMasterExt ordMasterExt);

    /**
     * 查询未派单总数[包含已指派未发货]
     *
     * @param ordMasterExt
     * @return
     */
    int countDispatchOrdMaster(OrdMasterExt ordMasterExt);

    /**
     * 查询未派单总数[未指派]
     *
     * @param ordMasterExt
     * @return
     */
    int countPendingDispatchOrder(OrdMasterExt ordMasterExt);

    /**
     * 店铺查询待发货一览
     * @param form
     * @return
     */
    List<OrdMasterExt> selectProcessedOrdMasterList(OrdDispatchForm form);

    /**
     * 待发货数量
     * @param form
     * @return
     */
    int countProcessedOrdMaster(OrdDispatchForm form);

    /**
     * 获取某个用户的全部订单个数
     * @param memberId
     * @return
     */
    int getAllCountByMemberId(String memberId);

    /**
     * 获取某个用户的待付款订单个数
     * @param memberId
     * @return
     */
    int getWaitPayCountByMemberId(String memberId);

    /**
     * 获取某个用户的待发货订单个数
     * @param memberId
     * @return
     */
    int getWaitDeliveryCountByMemberId(String memberId);

    /**
     * 获取某个用户的待收货订单个数
     * @param memberId
     * @return
     */
    int getWaitReceiveCountByMemberId(String memberId);

    /**
     * 获取某个用户的已完成订单个数
     * @param memberId
     * @return
     */
    int getCompletedCountByMemberId(String memberId);

    List<OrdMasterExt> getAllOrderByMemberId(OrdMasterExt memberId);

    List<OrdMasterExt> getWaitPayOrderByMemberId(OrdMasterExt memberId);

    List<OrdMasterExt> getWaitDeliveryOrderByMemberId(OrdMasterExt memberId);

    List<OrdMasterExt> getWaitReceiveOrderByMemberId(OrdMasterExt memberId);

    List<OrdMasterExt> getCompletedOrderByMemberId(OrdMasterExt memberId);

    List<OrdMaster> getUnreceivedOrder15DaysAgo();
    List<OrdMaster> getUnfinishedOrder7DaysAgo();
    List<OrdMaster> getUnclosedOrder30DaysAgo();

    int getOrderCount(OrdMaster ordMaster);

    /**
     * 导出订单excel
     * @param ordMasterExt
     * @return
     */
    List<OrdMasterExt> excelExport(OrdMasterExt ordMasterExt);

    /**
     * todo 返回子订单下的申请退款金额
     * @param orderId
     * @return
     */
    BigDecimal getRexPrice(String orderId);

    /**
     * todo 返回子订单下的实际退款金额
     * @param orderId
     * @return
     */
    BigDecimal getRexInfactPrice(String orderId);

    List<OrdMaster> selectThisYear();
}
