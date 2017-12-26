package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.OrdMasterExt;
import net.dlyt.qyds.common.dto.OrdSubList;
import net.dlyt.qyds.common.dto.OrdSubListExt;
import net.dlyt.qyds.common.dto.ext.MmbShoppingSKuExt;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdSubListMapperExt {

    /**
     * 20171205修改了方法的返回值，原返回值为： OrdSubList
     * @param ordMasterExt
     * @return
     */
    OrdSubListExt selectByPrimaryKey(OrdMasterExt ordMasterExt);

    /**
     * 根据订单id获取子订单信息
     * @param orderId
     * @return
     */
    List<OrdSubListExt> selectOrderSubInfo(String orderId);

    /**
     * 根据订单id获取子订单信息
     * 排序按照快递单号处理
     * @param orderId
     * @return
     */
    List<OrdSubListExt> selectOrderSubInfoOrderByExpressNo(String orderId);

    /**
     * 根据订单数组获取子订单信息
     * 列表页面使用
     * @param ord_ids
     * @return
     */
    List<OrdSubList> selectSubOrderByOrderId(String ord_ids);

    /**
     * 更新子订单收货状态为已收货
     * @param ordMasterExt
     * @return
     */
    int confirmReceipt(OrdMasterExt ordMasterExt);

    /**
     * 更新子订单发货状态为退货申请中
     * @param ordMasterExt
     * @return
     */
    int applyReturnGoods(OrdMasterExt ordMasterExt);

    /**
     * 根据商品详细订单id查询子订单数据
     * @param ordMasterExt
     * @return
     */
    List<OrdSubList> selectSubOrderByDetailId(OrdMasterExt ordMasterExt);

    /**
     * 插入子订单数据
     * @param ordSubList
     * @return
     */
    int insertSelective(OrdSubList ordSubList);

    /**
     * 更新ERP发货信息
     * @param record
     * @return
     */
    int updateByErp(OrdSubList record);

    /**
     * 取得订单未发货件数
     * @param id
     * @return
     */
    int getCountNoSendByOrderId(String id);

    /**
     * 取得订单发货件数
     * @param id
     * @return
     */
    int getCountSendByOrderId(String id);

    /**
     * 退货审批通过更新子订单
     * @param ordSubList
     */
    void changeReturnGoods(OrdSubList ordSubList);

    /**
     * 卖家退后确认收货更新子订单
     * @param ordSubList
     */
    void acceptReturnGoods(OrdSubList ordSubList);

    /**
     * 根据订单ID取得ERP子订单数据
     * @param orderId
     * @return
     */
    List<OrdSubListExt> selectErpByOrder(String orderId);

    /**
     * 根据订单ID取得未发送的ERP退货子订单数据
     * @param orderId
     * @return
     */
    List<OrdSubList> selectErpReturnFailByOrder(String orderId);

    /**
     * 根据子订单ID取得未发送的ERP退货子订单数据
     * @param subOrderId
     * @return
     */
    OrdSubList selectErpReturnFailBySubOrder(String subOrderId);

    /**
     * 根据订单ID获取套装对应的所有商品信息
     * @param detailId
     * @return
     */
    List<MmbShoppingSKuExt> querySKUListByOrderDetailId(String detailId);

    List<OrdSubListExt> selectDistinctOrderSubInfo(String orderId);

    List<OrdSubListExt> selectErpReturnByOrder(String orderId);

    OrdSubListExt selectErpReturnBySubOrder(String orderId);

    List<OrdSubListExt> selectProof(OrdSubList record);

    List<OrdSubListExt> selectProofDispatch(OrdSubList record);

    int getUpSeasoningDispatchListCount(OrdSubListExt ordSubListExt);

    List<OrdSubListExt> getUpSeasoningDispatchList(OrdSubListExt ordSubListExt);

    void receiveSubOrder(OrdSubList order);

    List<OrdSubList> getReportReturn(OrdMasterExt form);

    List<OrdSubList> getReportSale(OrdMasterExt form);

    /**
     *
     * 检索消费者某一商品参加某一活动的购买总数
     * @param subList 使用InsertUserId作为memberId传参使用
     * @return
     */
    int countMemberSecKillGoods(OrdSubList subList);

    List<OrdSubList> selectSecKillActivityOrdSubList(String orderId);

    /**
     * 导出子订单excel
     */
    List<OrdSubListExt> excelSubExport(String orderId);

    /**
     * 根据子订单id（物流号）获取子订单信息
     * @param subOrderId
     * @return
     */
    OrdSubListExt selectByTxLogisticID(String subOrderId);
}