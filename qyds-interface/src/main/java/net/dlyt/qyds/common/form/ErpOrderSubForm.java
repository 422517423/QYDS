package net.dlyt.qyds.common.form;


import net.dlyt.qyds.common.dto.ErpOrderSub;

/**
 * Created by zlh on 2016/7/26.
 */
public class ErpOrderSubForm extends ErpOrderSub {

    //分页信息
    private int  sEcho;
    //开始页面
    private int iDisplayStart;
    //每一页显示的项目
    private int iDisplayLength;

    public int getIDisplayStart() {
        return iDisplayStart;
    }

    public void setIDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public int getIDisplayLength() {
        return iDisplayLength;
    }

    public void setIDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public int getSEcho() {
        return sEcho;
    }

    public void setSEcho(int sEcho) {
        this.sEcho = sEcho;
    }
}