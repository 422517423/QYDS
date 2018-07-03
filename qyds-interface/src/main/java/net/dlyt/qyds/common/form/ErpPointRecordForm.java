package net.dlyt.qyds.common.form;


import net.dlyt.qyds.common.dto.ErpPointRecord;
import net.dlyt.qyds.common.dto.ext.ErpBankRecordExt;

/**
 * Created by zlh on 2016/8/22.
 */
public class ErpPointRecordForm extends ErpPointRecord {

    //分页信息
    private int  sEcho;
    //开始页面
    private int iDisplayStart;
    //每一页显示的项目
    private int iDisplayLength;
    //开始日期
    private String startDate;
    //结束日期
    private String endDate;

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}