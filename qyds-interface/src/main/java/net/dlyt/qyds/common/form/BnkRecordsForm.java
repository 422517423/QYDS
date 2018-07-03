package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.BnkRecords;

/**
 * Created by wy on 2016/8/3.
 */
public class BnkRecordsForm  extends BnkRecords{
    //分页信息
    private String  sEcho;
    //开始页面
    private String iDisplayStart;

    //每一页显示的项目
    private String iDisplayLength;
    //商品ID
    private String goods_type_id;

    public String getGoods_type_id() {
        return goods_type_id;
    }

    public void setGoods_type_id(String goods_type_id) {
        this.goods_type_id = goods_type_id;
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

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }
}
