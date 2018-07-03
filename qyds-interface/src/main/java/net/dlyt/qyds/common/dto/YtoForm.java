package net.dlyt.qyds.common.dto;

public class YtoForm {
    private String logistics_interface;//消息内容
    private String data_digest;//消息签名
    private String clientId;//客户编码（电商标识）
    private String type;//订单类型（online:在线下单，offline:线下下单）

    public String getLogistics_interface() {
        return logistics_interface;
    }

    public void setLogistics_interface(String logistics_interface) {
        this.logistics_interface = logistics_interface;
    }

    public String getData_digest() {
        return data_digest;
    }

    public void setData_digest(String data_digest) {
        this.data_digest = data_digest;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
