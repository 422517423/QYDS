package net.dlyt.qyds.common.form;

/**
 * Created by YiLian on 16/8/8.
 */
public class MmbLevelManagerForm {

    private String memberId;

    private String memberName;

    private String currentLevelId;

    private String currentLevelName;

    private String approvalLevelId;

    private String approvalLevelName;

    private Integer allPoint;

    private Integer pointSingle;

    private String telephone;

    // TODO: 2018/1/15 查询年份标识(0.当年1.当年和前一年)

    private String yearNum;

    public String getYearNum() {
        return yearNum;
    }

    public void setYearNum(String yearNum) {
        this.yearNum = yearNum;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getCurrentLevelId() {
        return currentLevelId;
    }

    public void setCurrentLevelId(String currentLevelId) {
        this.currentLevelId = currentLevelId;
    }

    public String getCurrentLevelName() {
        return currentLevelName;
    }

    public void setCurrentLevelName(String currentLevelName) {
        this.currentLevelName = currentLevelName;
    }

    public String getApprovalLevelId() {
        return approvalLevelId;
    }

    public void setApprovalLevelId(String approvalLevelId) {
        this.approvalLevelId = approvalLevelId;
    }

    public String getApprovalLevelName() {
        return approvalLevelName;
    }

    public void setApprovalLevelName(String approvalLevelName) {
        this.approvalLevelName = approvalLevelName;
    }

    public Integer getAllPoint() {
        return allPoint;
    }

    public void setAllPoint(Integer allPoint) {
        this.allPoint = allPoint;
    }

    public Integer getPointSingle() {
        return pointSingle;
    }

    public void setPointSingle(Integer pointSingle) {
        this.pointSingle = pointSingle;
    }


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
