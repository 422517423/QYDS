package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.OrdSubList;
import net.dlyt.qyds.common.dto.OrdSubListExt;

import java.util.List;

/**
 * Created by YiLian on 16/8/14.
 */
public class OrdLogisticsForm {

    // 子商品信息
    List<OrdSubListExt> ordSubList;

    // 物流信息
    List<OrdTraceForm> traces;

    // 物流状态 10.未发货，20.已发货，21.已收货，29.退货申请中，30.退货已发货，31.退货已收货，40.换货已发退货，41.换货已收退货，43.换货已发新货，44.换货已收新货
    private String deliverStatus;

    // 邮件编号 : 收货用标识符-物流送货
    private String expressNo;

    // 订单编号 : 收货用标识符-门店自提
    private String orderId;

    // 用户ID
    private String memberId;


    public List<OrdTraceForm> getTraces() {
        return traces;
    }

    public void setTraces(List<OrdTraceForm> traces) {
        this.traces = traces;
    }

    public List<OrdSubListExt> getOrdSubList() {
        return ordSubList;
    }

    public void setOrdSubList(List<OrdSubListExt> ordSubList) {
        this.ordSubList = ordSubList;
    }

    public String getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(String deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
