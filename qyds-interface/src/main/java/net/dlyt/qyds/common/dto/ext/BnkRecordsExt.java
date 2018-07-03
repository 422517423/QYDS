package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.BnkRecords;

/**
 * Created by wy on 2016/8/3.
 */
public class BnkRecordsExt extends BnkRecords {
    //需要多少行
    private int needColumns;
    //起点位置
    private int startPoint;
    //总行数
    private int count;

    private String userName;

    private String loginId;

    // 商品类型
    private String goodsTypeName;

    // 商品类型
    private String gdsTypeName;

    // 入出库类型
    private String bankTypeName;

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

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public String getGdsTypeName() {
        return gdsTypeName;
    }

    public void setGdsTypeName(String gdsTypeName) {
        this.gdsTypeName = gdsTypeName;
    }

    public String getBankTypeName() {
        return bankTypeName;
    }

    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
}
