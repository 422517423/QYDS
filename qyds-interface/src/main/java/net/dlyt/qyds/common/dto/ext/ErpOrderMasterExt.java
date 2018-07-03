package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.ErpOrderMaster;
import net.dlyt.qyds.common.dto.ErpOrderSub;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ErpOrderMasterExt extends ErpOrderMaster implements Serializable {

    private List<ErpOrderSub> subList;

    public List<ErpOrderSub> getSubList() {
        return subList;
    }

    public void setSubList(List<ErpOrderSub> subList) {
        this.subList = subList;
    }
}