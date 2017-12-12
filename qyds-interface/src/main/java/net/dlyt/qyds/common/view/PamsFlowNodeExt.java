package net.dlyt.qyds.common.view;

import net.dlyt.qyds.common.dto.PamsFlowNode;

/**
 * Created by panda on 16/7/7.
 */
public class PamsFlowNodeExt extends PamsFlowNode{

    //节点名称
    private String nodeName;

    //标准工时
    private int standardDay;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public int getStandardDay() {
        return standardDay;
    }

    public void setStandardDay(int standardDay) {
        this.standardDay = standardDay;
    }
}
