package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.*;

import java.util.List;

/**
 * Created by C_Nagai on 2016/8/6.
 */
public class CmsMasterExt extends CmsMaster {
    //需要多少行
    private int needColumns;

    //起点位置
    private int startPoint;

    //总行数
    private int count;

    //  栏目形式名称
    private String itemTypeName;

    // 创建人
    private String insertUserName;

    // 栏目名称
    private String itemName;

    // 栏目type
    private String cmsItemType;

    // cms管理父节点
    private String cmsIdParent;

    // 图片路径
    private String imageUrl;

    // 图片连接
    private String imageLink;

    // 商品/活动/ID
    private String itemTypeVal;

    // 商品
    private GdsMasterExt gdsMasterExt;

    // 商品列表
    private List<GdsMasterExt> gdsMasterExtList;

    // 栏目code
    private String itemCode;

    // 商品ID
    private String goodsId;

    // 活动ID
    private String actId;

    // 显示商品是单选还是多选的 0:单选 1:多选
    private String goodsChoiceFlag;

    // 层级
    private String level;

    // 活动商品显示 0:显示 1:不现实
    private String actGoodsFlag;

    // 商品分类信息
    private GdsTypeExt gdsType;

    // 商品分类
    private String goodsTypeId;

    // 存放视频
    private String video;

    public GdsMasterExt getGdsMasterExt() {
        return gdsMasterExt;
    }

    public void setGdsMasterExt(GdsMasterExt gdsMasterExt) {
        this.gdsMasterExt = gdsMasterExt;
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

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public String getInsertUserName() {
        return insertUserName;
    }

    public void setInsertUserName(String insertUserName) {
        this.insertUserName = insertUserName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCmsItemType() {
        return cmsItemType;
    }

    public void setCmsItemType(String cmsItemType) {
        this.cmsItemType = cmsItemType;
    }

    public String getCmsIdParent() {
        return cmsIdParent;
    }

    public void setCmsIdParent(String cmsIdParent) {
        this.cmsIdParent = cmsIdParent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getItemTypeVal() {
        return itemTypeVal;
    }

    public void setItemTypeVal(String itemTypeVal) {
        this.itemTypeVal = itemTypeVal;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getGoodsChoiceFlag() {
        return goodsChoiceFlag;
    }

    public void setGoodsChoiceFlag(String goodsChoiceFlag) {
        this.goodsChoiceFlag = goodsChoiceFlag;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getActGoodsFlag() {
        return actGoodsFlag;
    }

    public void setActGoodsFlag(String actGoodsFlag) {
        this.actGoodsFlag = actGoodsFlag;
    }

    public List<GdsMasterExt> getGdsMasterExtList() {
        return gdsMasterExtList;
    }

    public void setGdsMasterExtList(List<GdsMasterExt> gdsMasterExtList) {
        this.gdsMasterExtList = gdsMasterExtList;
    }

    public GdsTypeExt getGdsType() {
        return gdsType;
    }

    public void setGdsType(GdsTypeExt gdsType) {
        this.gdsType = gdsType;
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
