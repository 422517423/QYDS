package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class AllController implements Serializable {
    private String allControllerId;

    private String type;

    private String name;

    private String paramOne;

    private String paramTwo;

    private String paramThree;

    private String paramFour;

    private String paramFive;

    private String createUserId;

    private String updateUserId;

    private String comment;

    private Date createTime;

    private Date updateTime;

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private static final long serialVersionUID = 1L;

    public String getAllControllerId() {
        return allControllerId;
    }

    public void setAllControllerId(String allControllerId) {
        this.allControllerId = allControllerId == null ? null : allControllerId.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getParamOne() {
        return paramOne;
    }

    public void setParamOne(String paramOne) {
        this.paramOne = paramOne == null ? null : paramOne.trim();
    }

    public String getParamTwo() {
        return paramTwo;
    }

    public void setParamTwo(String paramTwo) {
        this.paramTwo = paramTwo == null ? null : paramTwo.trim();
    }

    public String getParamThree() {
        return paramThree;
    }

    public void setParamThree(String paramThree) {
        this.paramThree = paramThree == null ? null : paramThree.trim();
    }

    public String getParamFour() {
        return paramFour;
    }

    public void setParamFour(String paramFour) {
        this.paramFour = paramFour == null ? null : paramFour.trim();
    }

    public String getParamFive() {
        return paramFive;
    }

    public void setParamFive(String paramFive) {
        this.paramFive = paramFive == null ? null : paramFive.trim();
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId == null ? null : updateUserId.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}