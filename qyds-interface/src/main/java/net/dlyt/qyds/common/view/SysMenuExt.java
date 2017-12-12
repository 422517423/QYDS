package net.dlyt.qyds.common.view;

import net.dlyt.qyds.common.dto.SysMenu;

/**
 * Created by panda on 16/7/7.
 */
public class SysMenuExt extends SysMenu {

    //是否选中
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
