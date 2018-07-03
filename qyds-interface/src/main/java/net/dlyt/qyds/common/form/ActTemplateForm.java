package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.ActTempParam;
import net.dlyt.qyds.common.dto.ActTemplate;

import java.util.List;

/**
 * Created by cjk on 16/7/27.
 */
public class ActTemplateForm extends ActTemplate {
    private String applyUserName = null;
    private String approveUserName = null;
    private String actitionTypeCn = null;
    private String actitionTypeEn = null;
    private String approveStatusCn = null;
    private String approveStatusEn = null;
    private List<ActTempParam> paramList;
    private List<SkuForm> skuList = null;

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

    public List<ActTempParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<ActTempParam> paramList) {
        this.paramList = paramList;
    }

    public String getApproveStatusEn() {
        return approveStatusEn;
    }

    public void setApproveStatusEn(String approveStatusEn) {
        this.approveStatusEn = approveStatusEn;
    }

    public String getActitionTypeCn() {
        return actitionTypeCn;
    }

    public void setActitionTypeCn(String actitionTypeCn) {
        this.actitionTypeCn = actitionTypeCn;
    }

    public String getActitionTypeEn() {
        return actitionTypeEn;
    }

    public void setActitionTypeEn(String actitionTypeEn) {
        this.actitionTypeEn = actitionTypeEn;
    }

    public String getApproveStatusCn() {
        return approveStatusCn;
    }

    public void setApproveStatusCn(String approveStatusCn) {
        this.approveStatusCn = approveStatusCn;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getApproveUserName() {
        return approveUserName;
    }

    public void setApproveUserName(String approveUserName) {
        this.approveUserName = approveUserName;
    }

    public List<SkuForm> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<SkuForm> skuList) {
        this.skuList = skuList;
    }
}
