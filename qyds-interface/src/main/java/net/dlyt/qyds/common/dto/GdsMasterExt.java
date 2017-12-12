package net.dlyt.qyds.common.dto;

import net.dlyt.qyds.common.form.ActMasterForm;

import java.util.Date;
import java.util.List;

public class GdsMasterExt extends GdsMaster {

    //商品类型 10.ERP单品，20.电商单品，30.套装
    private String typeName;
    //维护状态 10.未维护，20.未完成，30.已完成
    private String maintainStatusName;

    //登录用户的名字
    private String loginUserName;

    //需要多少行
    private int needColumns;

    //起点位置
    private int startPoint;

    //总行数
    private int count;

    //商品品牌
    private String brandName;

    //商品分类
    private String goodsTypeName;

    //检索主key
    private String searchKey;

    //属性
    private String propertyJson;

    //介绍
    private String introduceHtml;

    //描述
    private String description;

    //尺码描述
    private String sizeDescription;

    private String suitId;

    //ERP品牌名称
    private String erpBrandName;

    //商品SKUID
    private String goodsSkuId;

    // 商品最低单价
    private String minPrice;

    // 商品最高单价
    private String maxPrice;

    // 商品单价区间
    private String minAndMaxPrice;

    // 活动tag
    private List<String> activityTags;

    // 活动赠送的颜色Id
    private List<String> activityColorCodeList;

    // 商品图片
    private String imageUrlJson;
    // 商品图片PC
    private String[] imageUrlJsonPc;

    //总库存数
    private String totalStore;

    //优惠劵号码
    private Integer collectNo;

    // 商品单价区间(优惠后)
    private String minAndMaxPriceActivity;

    //sku信息
    private List<ViewOnsellSku> skulist;

    //颜色列表
    private List<SkuNameKeyValue> colorList;

    //尺寸列表
    private List<SkuNameKeyValue> sizeList;

    //套装的场合用商品列表
    private List<GdsMasterExt> goodsList;

    // 活动ID
    private String activityId;

    // 活动ID
    private String activityIds;

    // 商品分类一级菜单
    private String firstGoodsTypeId;

    // 品牌系列
    private String firstGoodsBrandTypeId;

    //from价格区间
    private int from;

    //to价格区间
    private int to;

    //排序 按照价格 和 时间
    private int sortByPrice;
    private int sortByTime;

    //会员ID
    private String memberId;

    //是否加入心愿单的标记
    private String isInWishlist;

    // 售后服务富文本内容
    private String goodsServeHtml;

    // 活动名称
    private String activityName;

    // 当前页码
    private int currentPage;
    // 全部件数
    private int totalPage;
    // 一个页面显示的件数
    private int pageSize;

    // 当前currentpage*pagesize
    private int displayItemCount;

    //相关推荐
    private List<GdsMasterExt> recommend_list = null;

    //配套商品
    private List<GdsMasterExt> mating_list = null;

    //活动search条件
    private String activitySearchKey;

    private String activityType;

    // 尺码类型code名字  尺码code名字
    private String sizeTypeCode;

    private String sizeCode;

    private String sizeName;

    private String sizeTypeMame;


    //预约姓名
    private String userName;
    //预约电话
    private String telephone;

    private String goodsIds;

    //cmsId
    private  String cmsId;
    //插入时间cms
    private Short insertTimeGds;
    //cmsGdsId --- cms
    private String cmsGdsId;
    //sort ----cms
    private Short sort;

    // 商品活动信息
    private ActGoods actInfo;

    //秒杀活动的活动信息
    private ActMasterForm activityInfo;

    //系统时间
    private Date nowTime;

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

    public String getMaintainStatusName() {
        return maintainStatusName;
    }

    public void setMaintainStatusName(String maintainStatusName) {
        this.maintainStatusName = maintainStatusName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getPropertyJson() {
        return propertyJson;
    }

    public void setPropertyJson(String propertyJson) {
        this.propertyJson = propertyJson;
    }

    public String getIntroduceHtml() {
        return introduceHtml;
    }

    public void setIntroduceHtml(String introduceHtml) {
        this.introduceHtml = introduceHtml;
    }

    public String getSuitId() {
        return suitId;
    }

    public void setSuitId(String suitId) {
        this.suitId = suitId;
    }

    public String getErpBrandName() {
        return erpBrandName;
    }

    public void setErpBrandName(String erpBrandName) {
        this.erpBrandName = erpBrandName;
    }

    public String getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(String goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinAndMaxPrice() {
        return minAndMaxPrice;
    }

    public void setMinAndMaxPrice(String minAndMaxPrice) {
        this.minAndMaxPrice = minAndMaxPrice;
    }

    public List<String> getActivityTags() {
        return activityTags;
    }

    public void setActivityTags(List<String> activityTags) {
        this.activityTags = activityTags;
    }

    public String getImageUrlJson() {
        return imageUrlJson;
    }

    public void setImageUrlJson(String imageUrlJson) {
        this.imageUrlJson = imageUrlJson;
    }

    public String getTotalStore() {
        return totalStore;
    }

    public void setTotalStore(String totalStore) {
        this.totalStore = totalStore;
    }

    public String getMinAndMaxPriceActivity() {
        return minAndMaxPriceActivity;
    }

    public void setMinAndMaxPriceActivity(String minAndMaxPriceActivity) {
        this.minAndMaxPriceActivity = minAndMaxPriceActivity;
    }

    public List<ViewOnsellSku> getSkulist() {
        return skulist;
    }

    public void setSkulist(List<ViewOnsellSku> skulist) {
        this.skulist = skulist;
    }

    public List<GdsMasterExt> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GdsMasterExt> goodsList) {
        this.goodsList = goodsList;
    }


    public List<SkuNameKeyValue> getColorList() {
        return colorList;
    }

    public void setColorList(List<SkuNameKeyValue> colorList) {
        this.colorList = colorList;
    }

    public List<SkuNameKeyValue> getSizeList() {
        return sizeList;
    }

    public void setSizeList(List<SkuNameKeyValue> sizeList) {
        this.sizeList = sizeList;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getFirstGoodsTypeId() {
        return firstGoodsTypeId;
    }

    public void setFirstGoodsTypeId(String firstGoodsTypeId) {
        this.firstGoodsTypeId = firstGoodsTypeId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getIsInWishlist() {
        return isInWishlist;
    }

    public void setIsInWishlist(String isInWishlist) {
        this.isInWishlist = isInWishlist;
    }

    public String getGoodsServeHtml() {
        return goodsServeHtml;
    }

    public void setGoodsServeHtml(String goodsServeHtml) {
        this.goodsServeHtml = goodsServeHtml;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getDisplayItemCount() {
        return displayItemCount;
    }

    public void setDisplayItemCount(int displayItemCount) {
        this.displayItemCount = displayItemCount;
    }

    public String[] getImageUrlJsonPc() {
        return imageUrlJsonPc;
    }

    public void setImageUrlJsonPc(String[] imageUrlJsonPc) {
        this.imageUrlJsonPc = imageUrlJsonPc;
    }

    public Integer getCollectNo() {
        return collectNo;
    }

    public void setCollectNo(Integer collectNo) {
        this.collectNo = collectNo;
    }

    public List<GdsMasterExt> getRecommend_list() {
        return recommend_list;
    }

    public void setRecommend_list(List<GdsMasterExt> recommend_list) {
        this.recommend_list = recommend_list;
    }

    public List<GdsMasterExt> getMating_list() {
        return mating_list;
    }

    public void setMating_list(List<GdsMasterExt> mating_list) {
        this.mating_list = mating_list;
    }

    public String getFirstGoodsBrandTypeId() {
        return firstGoodsBrandTypeId;
    }

    public void setFirstGoodsBrandTypeId(String firstGoodsBrandTypeId) {
        this.firstGoodsBrandTypeId = firstGoodsBrandTypeId;
    }

    public int getSortByPrice() {
        return sortByPrice;
    }

    public void setSortByPrice(int sortByPrice) {
        this.sortByPrice = sortByPrice;
    }

    public int getSortByTime() {
        return sortByTime;
    }

    public void setSortByTime(int sortByTime) {
        this.sortByTime = sortByTime;
    }

    public String getActivitySearchKey() {
        return activitySearchKey;
    }

    public void setActivitySearchKey(String activitySearchKey) {
        this.activitySearchKey = activitySearchKey;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getSizeTypeCode() {
        return sizeTypeCode;
    }

    public void setSizeTypeCode(String sizeTypeCode) {
        this.sizeTypeCode = sizeTypeCode;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getSizeTypeMame() {
        return sizeTypeMame;
    }

    public void setSizeTypeMame(String sizeTypeMame) {
        this.sizeTypeMame = sizeTypeMame;
    }

    public List<String> getActivityColorCodeList() {
        return activityColorCodeList;
    }

    public void setActivityColorCodeList(List<String> activityColorCodeList) {
        this.activityColorCodeList = activityColorCodeList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getGoodsIds() {
        return goodsIds;
    }

    public void setGoodsIds(String goodsIds) {
        this.goodsIds = goodsIds;
    }

    public String getCmsId() {
        return cmsId;
    }

    public void setCmsId(String cmsId) {
        this.cmsId = cmsId;
    }

    public Short getInsertTimeGds() {
        return insertTimeGds;
    }

    public void setInsertTimeGds(Short insertTimeGds) {
        this.insertTimeGds = insertTimeGds;
    }

    public String getCmsGdsId() {
        return cmsGdsId;
    }

    public void setCmsGdsId(String cmsGdsId) {
        this.cmsGdsId = cmsGdsId;
    }

    public Short getSort() {
        return sort;
    }

    public void setSort(Short sort) {
        this.sort = sort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSizeDescription() {
        return sizeDescription;
    }

    public void setSizeDescription(String sizeDescription) {
        this.sizeDescription = sizeDescription;
    }

    public ActMasterForm getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(ActMasterForm activityInfo) {
        this.activityInfo = activityInfo;
    }

    public Date getNowTime() {
        return nowTime;
    }

    public void setNowTime(Date nowTime) {
        this.nowTime = nowTime;
    }

    public ActGoods getActInfo() {
        return actInfo;
    }

    public void setActInfo(ActGoods actInfo) {
        this.actInfo = actInfo;
    }

    public String getActivityIds() {
        return activityIds;
    }

    public void setActivityIds(String activityIds) {
        this.activityIds = activityIds;
    }
}