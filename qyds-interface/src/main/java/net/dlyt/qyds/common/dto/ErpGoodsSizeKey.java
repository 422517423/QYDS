package net.dlyt.qyds.common.dto;

import java.io.Serializable;

public class ErpGoodsSizeKey implements Serializable {
    private String sizeTypeCode;

    private String sizeCode;

    private static final long serialVersionUID = 1L;

    public String getSizeTypeCode() {
        return sizeTypeCode;
    }

    public void setSizeTypeCode(String sizeTypeCode) {
        this.sizeTypeCode = sizeTypeCode == null ? null : sizeTypeCode.trim();
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode == null ? null : sizeCode.trim();
    }
}