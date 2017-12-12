package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class PamsProductExt extends PamsProduct {

    private String flowName;

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }
}