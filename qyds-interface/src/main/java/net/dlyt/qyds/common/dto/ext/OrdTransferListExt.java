package net.dlyt.qyds.common.dto.ext;


import net.dlyt.qyds.common.dto.OrdTransferList;

/**
 * Created by zlh on 2016/12/24.
 */
public class OrdTransferListExt extends OrdTransferList {

    //分页信息
    private int  sEcho;
    //开始页面
    private int iDisplayStart;
    //每一页显示的项目
    private int iDisplayLength;
    //调货状态名称
    private String transferStatusName;

    // TODO: 2017/12/18 添加了圆通字段
    //商品名称
    private String goodsName;
    //收货人姓名
    private String applyContactor;
    //收货人省份
    private String applyPname;
    //收货城市
    private String applyCname;
    //收货人区域
    private String applyDname;
    //收货人地址
    private String applyAddress1;
    //发货人省份
    private String dispatchPname;
    //发货人城市
    private String dispatchCname;
    //发货人区域
    private String dispatchDname;
    //发货人地址
    private String dispatchAddress1;

    @Override
    public String getGoodsName() {
        return goodsName;
    }

    @Override
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @Override
    public String getApplyContactor() {
        return applyContactor;
    }

    @Override
    public void setApplyContactor(String applyContactor) {
        this.applyContactor = applyContactor;
    }

    public String getApplyPname() {
        return applyPname;
    }

    public void setApplyPname(String applyPname) {
        this.applyPname = applyPname;
    }

    public String getApplyCname() {
        return applyCname;
    }

    public void setApplyCname(String applyCname) {
        this.applyCname = applyCname;
    }

    public String getApplyDname() {
        return applyDname;
    }

    public void setApplyDname(String applyDname) {
        this.applyDname = applyDname;
    }

    public String getApplyAddress1() {
        return applyAddress1;
    }

    public void setApplyAddress1(String applyAddress1) {
        this.applyAddress1 = applyAddress1;
    }

    public String getDispatchPname() {
        return dispatchPname;
    }

    public void setDispatchPname(String dispatchPname) {
        this.dispatchPname = dispatchPname;
    }

    public String getDispatchCname() {
        return dispatchCname;
    }

    public void setDispatchCname(String dispatchCname) {
        this.dispatchCname = dispatchCname;
    }

    public String getDispatchDname() {
        return dispatchDname;
    }

    public void setDispatchDname(String dispatchDname) {
        this.dispatchDname = dispatchDname;
    }

    public String getDispatchAddress1() {
        return dispatchAddress1;
    }

    public void setDispatchAddress1(String dispatchAddress1) {
        this.dispatchAddress1 = dispatchAddress1;
    }

    public int getsEcho() {
        return sEcho;
    }

    public void setsEcho(int sEcho) {
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

    public String getTransferStatusName() {
        return transferStatusName;
    }

    public void setTransferStatusName(String transferStatusName) {
        this.transferStatusName = transferStatusName;
    }
}