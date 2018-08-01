package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.ext.AllControllerExt;

public class AllControllerForm extends AllControllerExt {
    //分页信息
    private String  sEcho;

    //开始页面
    private String iDisplayStart;

    //每一页显示的项目
    private String iDisplayLength;

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public String getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(String iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public String getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(String iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }
}
