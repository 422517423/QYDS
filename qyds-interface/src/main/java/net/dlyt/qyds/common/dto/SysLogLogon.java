package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class SysLogLogon implements Serializable {
    private Integer seq;

    private String userId;

    private String userIp;

    private String macAddr;

    private Date logonTime;

    private Date logoffTime;

    private String logonFlag;

    private String exceptionReason;

    private String sessionId;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp == null ? null : userIp.trim();
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr == null ? null : macAddr.trim();
    }

    public Date getLogonTime() {
        return logonTime;
    }

    public void setLogonTime(Date logonTime) {
        this.logonTime = logonTime;
    }

    public Date getLogoffTime() {
        return logoffTime;
    }

    public void setLogoffTime(Date logoffTime) {
        this.logoffTime = logoffTime;
    }

    public String getLogonFlag() {
        return logonFlag;
    }

    public void setLogonFlag(String logonFlag) {
        this.logonFlag = logonFlag == null ? null : logonFlag.trim();
    }

    public String getExceptionReason() {
        return exceptionReason;
    }

    public void setExceptionReason(String exceptionReason) {
        this.exceptionReason = exceptionReason == null ? null : exceptionReason.trim();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId == null ? null : sessionId.trim();
    }
}