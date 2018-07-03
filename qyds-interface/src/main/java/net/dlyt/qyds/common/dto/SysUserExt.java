package net.dlyt.qyds.common.dto;

/**
 * Created by C_Nagai on 2016/7/6.
 */
public class SysUserExt extends SysUser {

    private Integer roleId;

    private String roleName;

    private String jobId;

    private String jobName;

    private String[] jobIdArray;

    private String orgId;

    private String orgName;

    public String getOrgName() {
        return orgName;
    }
    public void setOrgName(String orgName) {this.orgName = orgName;}

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String[] getJobIdArray() {
        return jobIdArray;
    }

    public void setJobIdArray(String[] jobIdArray) {
        this.jobIdArray = jobIdArray;
    }

    //分页信息
    private String sEcho;

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
