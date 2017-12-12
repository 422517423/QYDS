package net.dlyt.qyds.common.dto;

public class GdsBrandExt  extends GdsBrand {

    //品牌类型 10.ERP品牌，20.电商品牌
    private String typeName;

    //登录用户的名字
    private String loginUserName;

    //需要多少行
    private int needColumns;

    //起点位置
    private int startPoint;

    //总行数
    private int count;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

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
}