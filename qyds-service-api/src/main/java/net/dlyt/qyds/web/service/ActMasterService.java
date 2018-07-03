package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ActGoods;
import net.dlyt.qyds.common.dto.ActMaster;
import net.dlyt.qyds.common.dto.OrdConfirmGoodsExt;
import net.dlyt.qyds.common.dto.ViewOnsellSku;
import net.dlyt.qyds.common.dto.ext.MmbShoppingBagExt;
import net.dlyt.qyds.common.form.ActMasterForm;
import net.dlyt.qyds.common.form.SkuForm;

import java.util.List;
import java.util.Map;

/**
 * Created by cjk on 16/8/1.
 */
public interface ActMasterService {

    JSONObject getAllList();

    JSONObject getList(ActMasterForm form);

    JSONObject delete(ActMasterForm form);

    JSONObject add(ActMasterForm form);

    JSONObject edit(ActMasterForm form);

    JSONObject apply(ActMasterForm form);

    JSONObject getDetail(ActMasterForm form);

    JSONObject getSellerYears();

    JSONObject getSellerSeasons(String data);

    JSONObject getErpBrands();

    JSONObject getErpLineCode();

    JSONObject getApproveList(ActMasterForm form);

    JSONObject approve(ActMasterForm form);

    JSONObject reject(ActMasterForm form);

    JSONObject bindActivityForShopingBag(List<MmbShoppingBagExt> goodsList, String memberId);

    List<OrdConfirmGoodsExt> bindActivityForOrderConfirm(List<OrdConfirmGoodsExt> goodsList, String memberId,String memberPhone,boolean isFromShoppingBag);

    List<ActMasterForm> getOrderActivity(String memberId,List<OrdConfirmGoodsExt> goodsInfo);

    ActMasterForm getOrderActivityById(String memberId,String activityId, List<OrdConfirmGoodsExt> goodsInfo);

    JSONObject getNewPricesByActivity(List<ActMasterForm> activityIds, String memberId, String goodsId);

    JSONObject filterActivityByMember(List<ActMasterForm> activityList, String memberId);

    JSONObject getNewPricesBySku(SkuForm activityIds);

    /**
     * 获取满送优惠券的Id
     * 
     * @param activityId
     * @return
     */
    String getSendCouponIdByActivity(String activityId);

    /**
     * 根据活动ID获取赠送的积分
     *
     * @param activityId
     * @return
     */
    String getSendPointsByActivity(String activityId);

    JSONObject setSort(ActMaster form);

    /**
     * 批量更新活动SKU信息
     *
     * @param loginId
     * @param activityId
     * @param skuList
     */
    JSONObject uploadSKUInfo(String loginId, String activityId, List<String> skuList);

    /**
     * 秒杀活动商品信息导入
     *
     * @param skuList
     * @return
     */
    JSONObject uploadSecKillGoods(List<ActGoods> skuList);

    /**
     * 取得活动的SKU列表
     * @param activityId
     * @return
     */
    JSONObject getActivitySKUList(String activityId);

    JSONObject setValid(ActMasterForm form);

    JSONObject setInvalid(ActMasterForm form);

    void checkSecKillActivity(OrdConfirmGoodsExt goodsInfo, String memberId);

    void checkBindActivityForOrder(List<OrdConfirmGoodsExt> list, String memberId);

    JSONObject addGoods(ActMasterForm form);

    JSONObject deleteGoods(ActMasterForm form);

    JSONObject editGoods(ActMasterForm form);

    JSONObject getGoodsList(ActMasterForm form);

    List<ViewOnsellSku> getNewPricesBySku(Map<String,SkuForm> skuFormMap, List<ViewOnsellSku> viewOnsellSkuList);
}
