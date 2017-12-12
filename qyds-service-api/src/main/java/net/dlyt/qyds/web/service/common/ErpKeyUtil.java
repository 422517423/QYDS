package net.dlyt.qyds.web.service.common;

import net.dlyt.qyds.web.service.erp.*;

import java.util.Map;

/**
 * Created by ZLH on 2016/9/1.
 */
public class ErpKeyUtil {
    //ERP加密
    static final String ERP_TOKEN_KEY = "bb5683c5-7916-4e79-9dd6-d2292530871a";

    /**
     * 生成VipUpdate密钥
     * @param data 需要加密数据
     * @return
     */
    public static String getKeyVIPUpdate(BaseDate data) throws Exception {
        Vip vip = data.getVip();
        vip.setMemberCode(changStringNull(vip.getMemberCode()));
        vip.setMemberName(changStringNull(vip.getMemberName()));
        vip.setSexName(changStringNull(vip.getSexName()));
        vip.setMobil(changStringNull(vip.getMobil()));
        vip.setBirthday(changStringNull(vip.getBirthday()));
        vip.setProvinceName(changStringNull(vip.getProvinceName()));
        vip.setCityName(changStringNull(vip.getCityName()));
        vip.setDistrictName(changStringNull(vip.getDistrictName()));
        vip.setEmail(changStringNull(vip.getEmail()));
        vip.setStoreCode(changStringNull(vip.getStoreCode()));
        vip.setStoreName(changStringNull(vip.getStoreName()));
        vip.setSellerName(changStringNull(vip.getSellerName()));
        vip.setAddress(changStringNull(vip.getAddress()));
        vip.setPostCode(changStringNull(vip.getPostCode()));
        vip.setIncome(changStringNull(vip.getIncome()));
        vip.setRegistTime(changStringNull(vip.getRegistTime()));
        vip.setMemberGrade(changStringNull(vip.getMemberGrade()));
        vip.setMemberCodeAlter(changStringNull(vip.getMemberCodeAlter()));

        String key = "";
        key += vip.getMemberCode();
        key += vip.getMemberName();
        key += vip.getSexName();
        key += vip.getMobil();
        key += vip.getBirthday();
        key += vip.getProvinceName();
        key += vip.getCityName();
        key += vip.getDistrictName();
        key += vip.getEmail();
        key += vip.getStoreCode();
        key += vip.getStoreName();
        key += vip.getSellerName();
        key += vip.getAddress();
        key += vip.getPostCode();
        key += vip.getIncome();
        key += vip.getRegistTime();
        key += vip.getMemberGrade();
        key += vip.getMemberCodeAlter();

        return getErpKey(key);
    }

    /**
     * 生成GoodsUpdate密钥
     * @param data 需要加密数据
     * @return
     */
    public static String getKeyGoodsUpdate(BaseDate data) throws Exception {
        Goods goods = data.getGoods();
        goods.setGoodsCode(changStringNull(goods.getGoodsCode()));
        goods.setGoodstype1(changStringNull(goods.getGoodstype1()));
        goods.setGoodstype2(changStringNull(goods.getGoodstype2()));
        goods.setGoodstype3(changStringNull(goods.getGoodstype3()));

        String key = "";
        key += goods.getGoodsCode();
        key += goods.getGoodstype1();
        key += goods.getGoodstype2();
        key += goods.getGoodstype3();

        return getErpKey(key);
    }

    /**
     * 生成VIPPointUpdate密钥
     * @param data 需要加密数据
     * @return
     */
    public static String getKeyVIPPointUpdate(BaseDate data) throws Exception {
        VipPoint vipPoint = data.getVipPoint();
        vipPoint.setMemberCode(changStringNull(vipPoint.getMemberCode()));
        vipPoint.setPoint(changStringNull(vipPoint.getPoint()));
        vipPoint.setPointTime(changStringNull(vipPoint.getPointTime()));
        //追加消费标记
        String used = vipPoint.getUsed();
        vipPoint.setUsed(StringUtil.isEmpty(used)?"0":used);

        String key = "";
        key += vipPoint.getMemberCode();
        key += vipPoint.getPoint();
        key += vipPoint.getPointTime();
        key += vipPoint.getUsed();

        return getErpKey(key);
    }

    /**
     * 生成SaleInput和ReturnInput密钥
     * @param data 需要加密数据
     * @return
     */
    public static String getKeyOrderInput(Orders data) throws Exception {
        SaleMaster saleMaster = data.getSaleMaster();
        saleMaster.setOrderId(changStringNull(saleMaster.getOrderId()));
        saleMaster.setMemberId(changStringNull(saleMaster.getMemberId()));
        saleMaster.setMemberName(changStringNull(saleMaster.getMemberName()));
        saleMaster.setAmountTotle(changStringNull(saleMaster.getAmountTotle()));
        saleMaster.setAmountDiscount(changStringNull(saleMaster.getAmountDiscount()));
        saleMaster.setPayTime(changStringNull(saleMaster.getPayTime()));
        saleMaster.setQuantity(changStringNull(saleMaster.getQuantity()));
//        saleMaster.setPreOrderId(changStringNull(saleMaster.getPreOrderId()));
        saleMaster.setMessage(changStringNull(saleMaster.getMessage()));
        saleMaster.setDeliveryFree(changStringNull(saleMaster.getDeliveryFree()));
        saleMaster.setDeliceryFee(changStringNull(saleMaster.getDeliceryFee()));
        saleMaster.setPayDeliveryType(changStringNull(saleMaster.getPayDeliveryType()));
        saleMaster.setPayType(changStringNull(saleMaster.getPayType()));
//        saleMaster.setPayStatus(changStringNull(saleMaster.getPayStatus()));
        saleMaster.setDeliveryType(changStringNull(saleMaster.getDeliveryType()));
//        saleMaster.setDeliveryPostcode(changStringNull(saleMaster.getDeliveryPostcode()));
//        saleMaster.setDistrictidProvince(changStringNull(saleMaster.getDistrictidProvince()));
//        saleMaster.setDistrictidCity(changStringNull(saleMaster.getDistrictidCity()));
//        saleMaster.setDistrictidDistrict(changStringNull(saleMaster.getDistrictidDistrict()));
//        saleMaster.setDeliveryAddress(changStringNull(saleMaster.getDeliveryAddress()));
//        saleMaster.setDeliveryContactor(changStringNull(saleMaster.getDeliveryContactor()));
//        saleMaster.setDeliveryPhone(changStringNull(saleMaster.getDeliveryPhone()));
//        saleMaster.setErpStoreId(changStringNull(saleMaster.getErpStoreId()));
//        saleMaster.setStoreName(changStringNull(saleMaster.getStoreName()));
        saleMaster.setSalerID(changStringNull(saleMaster.getSalerID()));

        String key = "";
        key += saleMaster.getOrderId();
        key += saleMaster.getMemberId();
        key += saleMaster.getMemberName();
        key += saleMaster.getAmountTotle();
        key += saleMaster.getAmountDiscount();
        key += saleMaster.getPayTime();
        key += saleMaster.getQuantity();
        key += saleMaster.getMessage();
        key += saleMaster.getDeliveryFree();
        key += saleMaster.getDeliceryFee();
        key += saleMaster.getPayDeliveryType();
        key += saleMaster.getPayType();
        key += saleMaster.getDeliveryType();
        key += saleMaster.getSalerID();

        if(data.getSaleList() != null && data.getSaleList().getSaleList() != null) {
            for (SaleList sub : data.getSaleList().getSaleList()) {
                sub.setOrderId(changStringNull(sub.getOrderId()));
                sub.setSubOrderId(changStringNull(sub.getSubOrderId()));
                sub.setErpSku(changStringNull(sub.getErpSku()));
                sub.setPrice(changStringNull(sub.getPrice()));
                sub.setPriceDiscount(changStringNull(sub.getPriceDiscount()));

                //子表暂不加入KEY
//                key += sub.getOrderId();
//                key += sub.getSubOrderId();
//                key += sub.getErpSku();
//                key += sub.getPrice();
//                key += sub.getPriceDiscount();
            }
        }

        return getErpKey(key);
    }

    /**
     * 生成CouponissueUpdate密钥
     * @param data 需要加密数据
     * @return
     */
    public static String getKeyCouponissueUpdate(Coupon data) throws Exception {
        CouponIssue couponIssue = data.getCouponIssue();
        couponIssue.setCouponId(changStringNull(couponIssue.getCouponId()));
        couponIssue.setCouponName(changStringNull(couponIssue.getCouponName()));
        couponIssue.setCouponStyle(changStringNull(couponIssue.getCouponStyle()));
        couponIssue.setCouponType(changStringNull(couponIssue.getCouponType()));
        couponIssue.setGoodsType(changStringNull(couponIssue.getGoodsType()));
        couponIssue.setDistributeType(changStringNull(couponIssue.getDistributeType()));
        couponIssue.setIsOriginPrice(changStringNull(couponIssue.getIsOriginPrice()));
        couponIssue.setMinGoodsCount(changStringNull(couponIssue.getMinGoodsCount()));
        couponIssue.setMinOrderPrice(changStringNull(couponIssue.getMinOrderPrice()));
        couponIssue.setWorth(changStringNull(couponIssue.getWorth()));
        couponIssue.setDiscount(changStringNull(couponIssue.getDiscount()));
        couponIssue.setSendStartTime(changStringNull(couponIssue.getSendStartTime()));
        couponIssue.setSendEndTime(changStringNull(couponIssue.getSendEndTime()));
        couponIssue.setStartTime(changStringNull(couponIssue.getStartTime()));
        couponIssue.setEndTime(changStringNull(couponIssue.getEndTime()));
        couponIssue.setMaxCount(changStringNull(couponIssue.getMaxCount()));
        couponIssue.setValidDays(changStringNull(couponIssue.getValidDays()));
        couponIssue.setDistributedCount(changStringNull(couponIssue.getDistributedCount()));
        couponIssue.setComment(changStringNull(couponIssue.getComment()));

        String key = "";
        key += couponIssue.getCouponId();
        key += couponIssue.getCouponName();
        key += couponIssue.getCouponStyle();
        key += couponIssue.getCouponType();
        key += couponIssue.getGoodsType();
        key += couponIssue.getDistributeType();
        key += couponIssue.getIsOriginPrice();
        key += couponIssue.getMinGoodsCount();
        key += couponIssue.getMinOrderPrice();
        key += couponIssue.getWorth();
        key += couponIssue.getDiscount();
        key += couponIssue.getSendStartTime();
        key += couponIssue.getSendEndTime();
        key += couponIssue.getStartTime();
        key += couponIssue.getEndTime();
        key += couponIssue.getMaxCount();
        key += couponIssue.getValidDays();
        key += couponIssue.getDistributedCount();
        key += couponIssue.getComment();

        if(data.getCouponSku() != null && data.getCouponSku().getCouponSku() != null) {
            for (CouponSku sku : data.getCouponSku().getCouponSku()) {
                sku.setCouponId(changStringNull(sku.getCouponId()));
                sku.setGoodsType(changStringNull(sku.getGoodsType()));
                sku.setGoodsId(changStringNull(sku.getGoodsId()));
                sku.setSkuId(changStringNull(sku.getSkuId()));
                //子表暂不加入KEY
//                key += sku.getCouponId();
//                key += sku.getGoodsType();
//                key += sku.getGoodsId();
//                key += sku.getSkuId();
            }
        }

        return getErpKey(key);
    }

    /**
     * 生成CouponSendUpdate密钥
     * @param data 需要加密数据
     * @return
     */
    public static String getKeyCouponSendUpdate(Coupon data) throws Exception {
        CouponSend couponSend = data.getCouponSend();
        couponSend.setCouponMemberId(changStringNull(couponSend.getCouponMemberId()));
        couponSend.setCouponId(changStringNull(couponSend.getCouponId()));
        couponSend.setMemberId(changStringNull(couponSend.getMemberId()));
        couponSend.setStatus(changStringNull(couponSend.getStatus()));
        couponSend.setStartTime(changStringNull(couponSend.getStartTime()));
        couponSend.setEndTime(changStringNull(couponSend.getEndTime()));
        couponSend.setSendTime(changStringNull(couponSend.getSendTime()));

        String key = "";
        key += couponSend.getCouponMemberId();
        key += couponSend.getCouponId();
        key += couponSend.getMemberId();
        key += couponSend.getStatus();
        key += couponSend.getStartTime();
        key += couponSend.getEndTime();
        key += couponSend.getSendTime();

        return getErpKey(key);
    }

    /**
     * 生成CouponUsedUpdate密钥
     * @param data 需要加密数据
     * @return
     */
    public static String getKeyCouponUsedUpdate(Coupon data) throws Exception {
        CouponUsed couponUsed = data.getCouponUsed();
        couponUsed.setCouponMemberId(changStringNull(couponUsed.getCouponMemberId()));
        couponUsed.setCouponId(changStringNull(couponUsed.getCouponId()));
        couponUsed.setMemberId(changStringNull(couponUsed.getMemberId()));
        couponUsed.setOrderId(changStringNull(couponUsed.getOrderId()));
        couponUsed.setStatus(changStringNull(couponUsed.getStatus()));
        couponUsed.setUsedTime(changStringNull(couponUsed.getUsedTime()));

        String key = "";
        key += couponUsed.getCouponMemberId();
        key += couponUsed.getCouponId();
        key += couponUsed.getMemberId();
        key += couponUsed.getOrderId();
        key += couponUsed.getStatus();
        key += couponUsed.getUsedTime();

        return getErpKey(key);
    }

    /**
     * 生成CouponSendUpdate密钥
     * @param data 需要加密数据
     * @return
     */
    public static String getKeyBankUpdate(Bank data) throws Exception {
        data.setSendStoreId(changStringNull(data.getSendStoreId()));
        data.setTakeStoreId(changStringNull(data.getTakeStoreId()));
        data.setBankTime(changStringNull(data.getBankTime()));
        data.setSalerID(changStringNull(data.getSalerID()));
        data.setErpSku(changStringNull(data.getErpSku()));
        data.setQuantity(changStringNull(data.getQuantity()));
        data.setStoreKind(changStringNull(data.getStoreKind()));

        String key = "";
        key += data.getSendStoreId();
        key += data.getTakeStoreId();
        key += data.getBankTime();
        key += data.getSalerID();
        key += data.getErpSku();
        key += data.getQuantity();
        key += data.getStoreKind();

        return getErpKey(key);
    }

    /**
     * 密钥验证并还原数据
     * @param data 已加密的数据
     * @return
     */
    public static String getErpData(String data,String key) throws Exception {
        try {
            data = EncryptUtil.decrypt(data);
            String key1 = getErpKey(data);
            if (!key1.equals(key)) {
                throw new Exception("密钥错误");
            }
            return data;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 生成密钥
     * @param data 需要加密的数据
     * @return
     */
    public static String getErpKey(String data) throws Exception{
        System.out.println(EncryptUtil.encodeMD5(ERP_TOKEN_KEY + data.toLowerCase()).toString());
        return EncryptUtil.encrypt(EncryptUtil.encodeMD5(ERP_TOKEN_KEY + data.toLowerCase()).toString());
    }

    /**
     * 转换Null为空
     * @param data 需要转换的字符串
     * @return
     */
    public static String changStringNull(String data) {
        return data == null ? "" : data;
    }

    /**
     * 生成VIPUsedUpdate密钥
     * @param data 需要加密数据
     * @return
     */
    public static String getKeyVIPUsedUpdate(BaseDate data) throws Exception {
        Vip vip = data.getVip();
        vip.setMemberCode(changStringNull(vip.getMemberCode()));
        vip.setUnUsedTime(changStringNull(vip.getUnUsedTime()));

        String key = "";
        key += vip.getMemberCode();
        key += vip.getUnUsed();
        key += vip.getUnUsedTime();

        return getErpKey(key);
    }

    /**
     * 20171207
     * 生成YTOUpdate密钥s
     * @param m 需要加密数据
     * @return
     */
    public static String getYTOUpdate(YTO m) throws Exception {
        YTOStates ytoPushInfo = m.getYtoPushInfo();
        String key = "";
        key += ytoPushInfo.getTxLogisticID();
        key += ytoPushInfo.getMailNo();
        key += ytoPushInfo.getInfoContent();
        key += ytoPushInfo.getRemark();
        key += ytoPushInfo.getSignedName();
        key += ytoPushInfo.getDeliveryName();
        key += ytoPushInfo.getAcceptTime();
        key += ytoPushInfo.getContactInfo();
        System.out.print(getErpKey(key));
        return getErpKey(key);
    }
}
