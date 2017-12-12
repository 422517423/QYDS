package net.dlyt.qyds.common.dto;

import java.io.Serializable;

public class ErpProvince implements Serializable {
    private String pcode;

    private String pname;

    private static final long serialVersionUID = 1L;

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode == null ? null : pcode.trim();
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname == null ? null : pname.trim();
    }
}