package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.ErpMember;

/**
 * Created by zlh on 2016/7/28.
 */
public class ErpMemberExt extends ErpMember {

    // 更新方式,10新增,20修改,30删除
    private String style;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
