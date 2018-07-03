package net.dlyt.qyds.common.dto;

/**
 * Created by C_Nagai on 2016/7/13.
 */
public class PamsProductTaskDtoExt extends PamsProductTask {

    private String productName;

    private String flowName;

    private String nodeName;

    private String statusName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
