package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.OrdMasterExt;
import net.dlyt.qyds.common.dto.OrdReturnExchange;
import net.dlyt.qyds.common.dto.OrdReturnExchangeExt;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdReturnExchangeMapperExt {

    /**
     * 插入退换货订单
     * @param record
     * @return
     */
    int insertSelective(OrdReturnExchange record);

    /**
     * 查看退换货订单信息
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
     * 卖家确认收货
     * @param ordReturnExchange
     */
    void acceptReturnGoods(OrdReturnExchange ordReturnExchange);

    /**
     * 根据订单查询退货未确认收货的退货单
     * @param orderId
     * @return
     */
    int getReturnExchangeCount(String orderId);

    /**
     * 根据订单ID查询退货申请中的退货单的个数
     * @param orderId
     * @return
     */
    int getReturnExchangeApplyingCountByOrdId(String orderId);

    /**
     * 根据订单ID查询审批通过的申请个数
     * @param orderId
     * @return
     */
    int getReturnExchangeApprovedCountByOrdId(String orderId);

    /**
     * 根据订单ID查询还没有退款完成的申请个数
     * @param orderId
     * @return
     */
    int getReturnExchangeUnRefundCountByOrdId(String orderId);

    /**
     * 商家根据子订单ID确认退单
     * @param ordReturnExchange
     */
    void submitReturnMoney(OrdReturnExchange ordReturnExchange);

    /**
     * 根据订单ID查询是否存在待退款的退货单
     * @param orderId
     * @return
     */
    int getReturnMoneyCount(String orderId);

    /**
     * 买家主订单审批通过后申请退货
     * @param ordReturnExchangeExt
     * @return
     */
    int updateSendById(OrdReturnExchange ordReturnExchangeExt);

    List<OrdReturnExchangeExt> selectPageList(OrdReturnExchangeExt ordReturnExchangeExt);



    int countPageList(OrdReturnExchangeExt ordReturnExchangeExt);

    List<OrdMasterExt> selectReceiveGoodsList(OrdReturnExchangeExt ordReturnExchangeExt);

    int selectReceiveGoodsListCount(OrdReturnExchangeExt ordReturnExchangeExt);

    OrdReturnExchangeExt getById(String id);

    int updateById(OrdReturnExchange record);

}