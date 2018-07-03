package net.dlyt.qyds.common.dto;

import java.io.Serializable;

public class ErpDistrict implements Serializable {
    private String dcode;

    private String ccode;

    private String dname;

    private static final long serialVersionUID = 1L;

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode == null ? null : dcode.trim();
    }

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode == null ? null : ccode.trim();
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname == null ? null : dname.trim();
    }
}