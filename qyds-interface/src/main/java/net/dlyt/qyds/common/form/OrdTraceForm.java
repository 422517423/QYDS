package net.dlyt.qyds.common.form;

/**
 * Created by YiLian on 16/8/15.
 */
public class OrdTraceForm {

    // 时间
    private String acceptTime;
    // 事件描述
    private String acceptStation;
    // 备注
    private String remark;

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getAcceptStation() {
        return acceptStation;
    }

    public void setAcceptStation(String acceptStation) {
        this.acceptStation = acceptStation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
