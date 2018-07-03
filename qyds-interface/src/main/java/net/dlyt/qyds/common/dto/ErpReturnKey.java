package net.dlyt.qyds.common.dto;

import java.io.Serializable;

public class ErpReturnKey implements Serializable {
    private String formNo;

    private String ticketNo;

    public String getFormNo() {
        return formNo;
    }

    public void setFormNo(String formNo) {
        this.formNo = formNo == null ? null : formNo.trim();
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo == null ? null : ticketNo.trim();
    }
}