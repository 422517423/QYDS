package net.dlyt.qyds.common.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 圆通接口调用controller后返回xml报文所用的实体类
 */
@XmlRootElement(name = "Response")
public class Response {
    private String logisticProviderID;
    private String txLogisticID;
    private boolean success;

    public String getLogisticProviderID() {
        return logisticProviderID;
    }

    @XmlElement
    public void setLogisticProviderID(String logisticProviderID) {
        this.logisticProviderID = logisticProviderID;
    }

    public String getTxLogisticID() {
        return txLogisticID;
    }

    @XmlElement
    public void setTxLogisticID(String txLogisticID) {
        this.txLogisticID = txLogisticID;
    }

    public boolean isSuccess() {
        return success;
    }

    @XmlElement
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
