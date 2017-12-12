package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.ShpStroeDetail;

/**
 * Created by wy on 2016/8/10.
 */
public class ShpStroeDetailExt  extends ShpStroeDetail{
   private String loginId;
    private String userName;
    //需要多少行
    private int needColumns;
    //起点位置
    private int startPoint;
    //总行数
    private int count;

    public int getNeedColumns() {
        return needColumns;
    }

    public void setNeedColumns(int needColumns) {
        this.needColumns = needColumns;
    }

    public int getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(int startPoint) {
        this.startPoint = startPoint;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
