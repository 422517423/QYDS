package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.ErpStore;
import net.dlyt.qyds.common.dto.OrdDispatch;

/**
 * Created by YiLian on 2016/10/10.
 */
public class DispatchStoreForm extends ErpStore{

    //分页信息
    private String  sEcho;

    //开始页面
    private int iDisplayStart;

    //每一页显示的项目
    private int iDisplayLength;

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public int getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

}
