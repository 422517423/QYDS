package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrdListExt extends OrdList {


    private List<OrdSkuInfoExt> skuInfo;

    public List<OrdSkuInfoExt> getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(List<OrdSkuInfoExt> skuInfo) {
        this.skuInfo = skuInfo;
    }
}