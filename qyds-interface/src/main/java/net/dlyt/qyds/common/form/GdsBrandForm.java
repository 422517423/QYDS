package net.dlyt.qyds.common.form;


import net.dlyt.qyds.common.dto.GdsBrand;

public class GdsBrandForm extends GdsBrand {

    //分页信息
    private String  sEcho;
    //开始页面
    private String iDisplayStart;

    //每一页显示的项目
    private String iDisplayLength;

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

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }
}