package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.MmbContact;

/**
 * Created by Congky on 16/8/8.
 */
public class MmbContactExt extends MmbContact {

    //分页信息
    private String sEcho;

    //开始页面
    private int iDisplayStart;

    //每一页显示的项目
    private int iDisplayLength;


    public int getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }
}
