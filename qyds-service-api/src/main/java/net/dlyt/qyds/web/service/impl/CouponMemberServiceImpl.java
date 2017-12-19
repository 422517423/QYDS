package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.CouponMasterExt;
import net.dlyt.qyds.common.dto.ext.CouponMemberExt;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.common.form.CouponGoodsForm;
import net.dlyt.qyds.common.form.MmbPointRecordForm;
import net.dlyt.qyds.dao.*;
import net.dlyt.qyds.dao.ext.CouponGoodsMapperExt;
import net.dlyt.qyds.dao.ext.CouponMasterMapperExt;
import net.dlyt.qyds.dao.ext.CouponMemberMapperExt;
import net.dlyt.qyds.dao.ext.MmbMasterMapperExt;
import net.dlyt.qyds.web.service.CouponMemberService;
import net.dlyt.qyds.web.service.MmbPointRecordService;
import net.dlyt.qyds.web.service.common.ComCode;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.ErpSendUtil;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by cjk on 16/8/9.
 */
@Service("couponMemberService")
public class CouponMemberServiceImpl implements CouponMemberService {

    protected final Logger log = LoggerFactory.getLogger(CouponMemberServiceImpl.class);
    @Autowired
    private CouponMasterMapperExt couponMasterMapperExt = null;
    @Autowired
    private CouponMasterMapper couponMasterMapper = null;
    @Autowired
    private CouponMemberMapper couponMemberMapper = null;
    @Autowired
    private CouponMemberMapperExt couponMemberMapperExt = null;
    @Autowired
    private CouponGoodsMapperExt couponGoodsMapperExt = null;
    @Autowired
    private GdsMasterMapper gdsMasterMapper = null;
    @Autowired
    private MmbMasterMapper mmbMasterMapper = null;
    @Autowired
    private MmbSalerMapper mmbSalerMapper = null;
    @Autowired
    private MmbPointRecordService mmbPointRecordService = null;
    @Autowired
    private MmbMasterMapperExt mmbMasterMapperExt = null;
    @Autowired
    private CouponOrderMapper couponOrderMapper = null;

    /**
     * 获取所有手动领取和现金购买的优惠劵
     *
     * @param form
     * @return
     */
    public JSONObject getAllCoupons(CouponMemberExt form) {
        JSONObject json = new JSONObject();
        try {
            //数据库检索 -- 过滤数据
            List<CouponMasterExt> list = couponMasterMapperExt.getAllCoupons(form);
            json.put("results", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    public JSONObject getPointExchangeCoupons(CouponMemberExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getMemberId())) {
                List<CouponMasterExt> list = new ArrayList<CouponMasterExt>();
                json.put("results", list);
                json.put("resultCode", Constants.NORMAL);
            } else {
                MmbMaster master = mmbMasterMapper.selectByPrimaryKey(form.getMemberId());
                if(master == null){
                    MmbSaler mmbSaler = mmbSalerMapper.selectByPrimaryKey(form.getMemberId());
                    master = new MmbMaster();
                    BeanUtils.copyProperties(mmbSaler,master);
                }

                form.setMemberLevel(master.getMemberLevelId());
                // memberLevel = 00 为不限制级别
                //数据库检索 -- 过滤数据
                List<CouponMasterExt> list = couponMasterMapperExt.getPointExchangeCoupons(form);
                json.put("results", list);
                json.put("resultCode", Constants.NORMAL);
            }
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    public JSONObject getMyCoupons(CouponMemberExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            //数据库检索 -- 过滤数据
            List<CouponMasterExt> list = couponMasterMapperExt.getMyCoupons(form);
            json.put("results", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addCouponsForUser(CouponMemberExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getCouponId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 更新优惠券主表
            CouponMaster coupon = couponMasterMapper.selectByPrimaryKey(form.getCouponId());
            if (coupon == null) {
                throw new ExceptionErrorData("优惠券不存在!");
            }
            if (Constants.DELETED_YES.equals(coupon.getDeleted())) {
                throw new ExceptionErrorData("优惠券已失效!");
            }
            int distributedCount = coupon.getDistributedCount() == null ? 0 : coupon.getDistributedCount();
            if (coupon.getMaxCount() != null) {
                int maxCount = coupon.getMaxCount() == null ? 0 : coupon.getMaxCount();
                if (maxCount <= distributedCount) {
                    throw new ExceptionErrorData("优惠劵已经被领光了!");
                }
            }
            // 获取该优惠券已经领取的个数
            int count = couponMemberMapperExt.checkCouponGeted(form);
            if (count >= coupon.getPerMaxCount()) {
                throw new ExceptionBusiness("该优惠劵最多只能领取" + coupon.getPerMaxCount() + "个!");
            }
            // 判断是否需要积分
            if (coupon.getExchangePoint() != null && coupon.getExchangePoint() > 0) {
                // 需要积分,获取该用户的积分数量
                MmbMaster rMmbMaster = mmbMasterMapper.selectByPrimaryKey(form.getMemberId());
                if (rMmbMaster == null) {
                    throw new ExceptionBusiness("积分信息获取失败,请稍后重试!");
                }
                if (rMmbMaster.getPoint() < coupon.getExchangePoint()) {
                    throw new ExceptionBusiness("积分不足!");
                }
            }

            if (ComCode.CouponDistributeType.BUY.equals(coupon.getDistributeType())) {
                // 需要现金购买的，生成订单返回orderId
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                CouponOrder couponOrder = new CouponOrder();
                // COUPON+14位日期+12位随机数
                String orderId = "COUPON" + sdf.format(new Date()) + StringUtil.getRandomNum(12);
                couponOrder.setOrderId(orderId);
                couponOrder.setCanReturn("1");
                couponOrder.setMemberId(form.getMemberId());
                couponOrder.setCouponId(form.getCouponId());
                couponOrder.setDeleted(Constants.DELETED_NO);
                // 10:未发送，20：已发送
                couponOrder.setDeliverStatus("10");
                couponOrder.setInsertTime(new Date());
                couponOrder.setInsertUserId(form.getMemberId());
                couponOrder.setOrderTime(new Date());
                // 10:未支付，20：已支付
                couponOrder.setPayStatus("10");
                couponOrder.setUpdateTime(new Date());
                couponOrder.setUpdateUserId(form.getMemberId());
                couponOrderMapper.insertSelective(couponOrder);
                json.put("result", orderId);
            } else {
                CouponMember cm = new CouponMember();
                cm.setMemberId(form.getMemberId());
                cm.setCouponId(form.getCouponId());
                cm.setCouponMemberId(UUID.randomUUID().toString());
                cm.setDeleted(Constants.DELETED_NO);
                cm.setInsertTime(new Date());
                cm.setStatus(ComCode.CouponStatus.UNUSE);
                cm.setSendTime(new Date());
                if (ComCode.CouponType.NORMAL.equals(coupon.getCouponType())) {
                    // 普通优惠券
                    // 如果有使用期限天数限制，优先级高于开始时间和结束时间
                    if(coupon.getValidDays()!=null&&coupon.getValidDays()>0){
                        // 开始日期:今天
                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.HOUR_OF_DAY, 0);
                        startTime.set(Calendar.MINUTE, 0);
                        startTime.set(Calendar.SECOND, 0);
                        startTime.set(Calendar.MILLISECOND, 0);
                        cm.setStartTime(startTime.getTime());

                        // 结束日期
                        Calendar endTime = Calendar.getInstance();
                        endTime.setTime(startTime.getTime());
                        endTime.add(Calendar.DAY_OF_MONTH, coupon.getValidDays());
                        cm.setEndTime(endTime.getTime());
                    }else{
                        cm.setStartTime(coupon.getStartTime());
                        cm.setEndTime(coupon.getEndTime());
                    }
                } else if (ComCode.CouponType.BIRTHDAY_SEND.equals(coupon.getCouponType())) {
                    // 生日劵
                    cm.setStartTime(new Date());
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, coupon.getValidDays());
                    cm.setEndTime(c.getTime());
                } else if (ComCode.CouponType.REGIST_SEND.equals(coupon.getCouponType())) {
                    // 注册劵
                    cm.setStartTime(new Date());
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, coupon.getValidDays());
                    cm.setEndTime(c.getTime());
                }
                couponMemberMapper.insertSelective(cm);
                // 更新领取个数
                coupon.setDistributedCount(distributedCount + 1);
                couponMasterMapper.updateByPrimaryKeySelective(coupon);
                if (coupon.getExchangePoint() != null && coupon.getExchangePoint() > 0) {
                    // 需要积分,调用扣除积分的接口支付成功
                    //付款增加积分信息
                    MmbPointRecordForm mmbPointRecordForm = new MmbPointRecordForm();
                    mmbPointRecordForm.setMemberId(form.getMemberId());
                    mmbPointRecordForm.setExchangeId("60");
                    mmbPointRecordForm.setExchangePoint(coupon.getExchangePoint());
                    mmbPointRecordForm.setScoreSource(cm.getCouponMemberId());
                    //积分处理
                    JSONObject result = mmbPointRecordService.add(mmbPointRecordForm);
                    if (!"00".equals(result.getString("resultCode"))) {
                        throw new ExceptionBusiness(result.getString("resultMessage"));
                    }
                }
                // 同步到ERP
//                if(coupon.getCouponScope().equals("10")) ErpSendUtil.getInstance().CouponSendUpdateById(cm.getCouponMemberId());
                if (coupon.getCouponScope().equals("10")) {
                    ErpSendUtil.CouponSendUpdateById(cm.getCouponMemberId(), couponMemberMapper, mmbMasterMapper);
                }
            }

            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    public JSONObject useCoupon(CouponMemberExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getCouponMemberId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getOrderId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            CouponMember cm = new CouponMember();
            cm.setCouponMemberId(form.getCouponMemberId());
            cm.setStatus(ComCode.CouponStatus.USED);
            cm.setOrderId(form.getOrderId());
            cm.setUpdateTime(new Date());
            cm.setUsedTime(new Date());
            couponMemberMapper.updateByPrimaryKeySelective(cm);
            // 同步到ERP
//            ErpSendUtil.getInstance().CouponUsedUpdateById(cm.getCouponMemberId());
            ErpSendUtil.CouponUsedUpdateById(cm.getCouponMemberId(), couponMemberMapper);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    @Override
    public JSONObject returnCoupon(CouponMemberExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getCouponMemberId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            CouponMember cm = new CouponMember();
            cm.setCouponMemberId(form.getCouponMemberId());
            cm.setStatus(ComCode.CouponStatus.UNUSE);
            cm.setOrderId(null);
            cm.setUpdateTime(new Date());
            //数据库检索 -- 过滤数据
            couponMemberMapper.updateByPrimaryKeySelective(cm);
            //ERP同步
            CouponMember couponMember = couponMemberMapper.selectByPrimaryKey(form.getCouponMemberId());
            if (couponMember == null) {
                throw new ExceptionErrorParam("优惠券不存在");
            }
            CouponMaster coupon = couponMasterMapper.selectByPrimaryKey(couponMember.getCouponId());
            if (coupon == null) {
                throw new ExceptionErrorParam("优惠券不存在");
            }
//            if(coupon.getCouponScope().equals("10"))ErpSendUtil.getInstance().CouponUsedUpdateById(cm.getCouponMemberId());
            if (coupon.getCouponScope().equals("10"))
                ErpSendUtil.CouponUsedUpdateById(cm.getCouponMemberId(), couponMemberMapper);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    public JSONObject getOrderCoupons(CouponMemberExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数");
            }

            if (StringUtil.isEmpty(form.getHasOrderActivity())) {
                throw new ExceptionErrorParam("缺少参数");
            }

            if (!StringUtil.isEmpty(form.getTelephone())) {
                MmbMasterExt ext = new MmbMasterExt();
                ext.setTelephone(form.getTelephone());
                List<MmbMasterExt> mmbMaster = mmbMasterMapperExt.selectAllByTel(ext);
                if (mmbMaster == null || mmbMaster.size() == 0) {
                    throw new ExceptionErrorParam("用户不存在");
                }
                form.setMemberId(mmbMaster.get(0).getMemberId());
            }

            //数据库检索 -- 过滤数据
            List<CouponMasterExt> list = couponMasterMapperExt.getOrderCoupons(form);
            List<CouponMasterExt> validCouponList = new ArrayList<CouponMasterExt>();
            List<OrdConfirmGoodsExt> validGoods1 = new ArrayList<OrdConfirmGoodsExt>();
            for (int i = 0; i < list.size(); i++) {
                // 如果限制正价
                // 1.过滤无效的商品

                // 1.1 通过要求正价或折扣过滤
                if (ComCode.CouponOriginPriceType.MUST_ORIGIN_PRICE.equals(list.get(i).getIsOriginPrice())) {
                    // 要求商品全部是
                    if (!isAllOriginPrice(form)) {
                        continue;
                    }
                    // 要求正价参与计算
                    validGoods1 = getGoodsInOriginPrice(form.getGoodsInfo());
                } else if (ComCode.CouponOriginPriceType.MUST_DISCOUNT_PRICE.equals(list.get(i).getIsOriginPrice())) {
                    // 要求折扣价参与计算
                    validGoods1 = getGoodsInDiscountPrice(form.getGoodsInfo());
                } else {
                    validGoods1 = form.getGoodsInfo();
                }
                // 1.2 通过要求优惠商品分类过滤
                List<OrdConfirmGoodsExt> validGoods2 = new ArrayList<OrdConfirmGoodsExt>();
                List<CouponGoodsForm> goodsList = null;
                CouponGoods goods = new CouponGoods();
                goods.setCouponId(list.get(i).getCouponId());
                // 是否有指定的商品类型
                if (ComCode.ActivityGoodsType.ALL.equals(list.get(i).getGoodsType())) {
                    //全部商品
                    validGoods2 = validGoods1;
                } else if (ComCode.ActivityGoodsType.TYPE.equals(list.get(i).getGoodsType())) {
                    // 按分类
                    goodsList = couponGoodsMapperExt.selectGoodsTypeByCouponId(goods);
                    validGoods2 = getGoodsInType(validGoods1, goodsList);
                } else if (ComCode.ActivityGoodsType.BRAND.equals(list.get(i).getGoodsType())) {
                    // 按品牌
                    goodsList = couponGoodsMapperExt.selectGoodsBrandByCouponId(goods);
                    validGoods2 = getGoodsInBrand(validGoods1, goodsList);
                } else if (ComCode.ActivityGoodsType.GOODS.equals(list.get(i).getGoodsType())) {
                    // 按商品
                    goodsList = couponGoodsMapperExt.selectGoodsByCouponId(goods);
                    validGoods2 = getGoodsInGoods(validGoods1, goodsList);
                } else if (ComCode.ActivityGoodsType.SKU.equals(list.get(i).getGoodsType())) {
                    // 按SKU
                    goodsList = couponGoodsMapperExt.selectSkuByCouponId(goods);
                    validGoods2 = getGoodsInSku(validGoods1, goodsList);
                } else {
                    // 其他种情况
                    goodsList = couponGoodsMapperExt.selectInfoByCouponId(goods);
                    validGoods2 = getGoodsInSku(validGoods1, goodsList);
                }

                // 2.判断是否要求最少商品件数
                if (!StringUtil.isEmpty(list.get(i).getMinGoodsCount())) {
                    int needCount = Integer.parseInt(list.get(i).getMinGoodsCount());
                    int goodsCount = getGoodsCount(validGoods2);
                    if (goodsCount < needCount) {
                        continue;
                    }
                }

                // 3.判断是否要求最少订单金额
                if (!StringUtil.isEmpty(list.get(i).getMinOrderPrice())) {
                    float minOrderPrice = Float.parseFloat(list.get(i).getMinOrderPrice());
                    float goodsTotalPrice = getGoodsTotalPrice(validGoods2);
                    if (goodsTotalPrice < minOrderPrice) {
                        continue;
                    }
                }
                validCouponList.add(list.get(i));
            }

            // 判断是否满minOrderPrice元
            json.put("results", validCouponList);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    private boolean isAllOriginPrice(CouponMemberExt form) {
        List<OrdConfirmGoodsExt> goodsInfo = form.getGoodsInfo();
        boolean isAllOriginPrice = true;
        if ("1".equals(form.getHasOrderActivity())) {
            return false;
        }
        // 判断是否有单品活动
        for (int j = 0; j < goodsInfo.size(); j++) {
            if (!StringUtil.isEmpty(goodsInfo.get(j).getActGoodsId())) {
                isAllOriginPrice = false;
                break;
            }
        }
        return isAllOriginPrice;
    }

    private int getGoodsCount(List<OrdConfirmGoodsExt> validGoods) {
        int goodsCount = 0;
        for (int i = 0; i < validGoods.size(); i++) {
            goodsCount += validGoods.get(i).getQuantity();
        }
        return goodsCount;
    }

    private float getGoodsTotalPrice(List<OrdConfirmGoodsExt> validGoods) {
        int goodsTotalPrice = 0;
        for (int i = 0; i < validGoods.size(); i++) {
            float goodsPrice = Float.parseFloat(validGoods.get(i).getOrdConfirmOrderUnitExtList().get(0).getPrice());
            if (!StringUtil.isEmpty(validGoods.get(i).getActGoodsId())) {
                goodsPrice = validGoods.get(i).getActivity().getNewPrice();
            }
            goodsTotalPrice += goodsPrice * validGoods.get(i).getQuantity();
        }
        return goodsTotalPrice;
    }


    private List<OrdConfirmGoodsExt> getGoodsInOriginPrice(List<OrdConfirmGoodsExt> goodsInfo) {
        List<OrdConfirmGoodsExt> validGoods = new ArrayList<OrdConfirmGoodsExt>();
        for (int j = 0; j < goodsInfo.size(); j++) {

            if (goodsInfo.get(j).getActivity() != null
                    && goodsInfo.get(j).getActivity().getActivityId() != null
                    && goodsInfo.get(j).getActGoodsId() != null) {
                // 有活动,就认为不是正价
                continue;
            } else {
                validGoods.add(goodsInfo.get(j));
            }
        }
        return validGoods;
    }

    private List<OrdConfirmGoodsExt> getGoodsInDiscountPrice(List<OrdConfirmGoodsExt> goodsInfo) {
        List<OrdConfirmGoodsExt> validGoods = new ArrayList<OrdConfirmGoodsExt>();
        for (int j = 0; j < goodsInfo.size(); j++) {
            // 没有活动,就认为是正价
            if (goodsInfo.get(j).getActivity() == null
                    || goodsInfo.get(j).getActivity().getActivityId() == null
                    || goodsInfo.get(j).getActGoodsId() == null) {
                continue;
            } else {
                validGoods.add(goodsInfo.get(j));
            }
        }
        return validGoods;
    }

    private List<OrdConfirmGoodsExt> getGoodsInType(List<OrdConfirmGoodsExt> goodsInfo, List<CouponGoodsForm> goodsTypeList) {
        List<OrdConfirmGoodsExt> validGoods = new ArrayList<OrdConfirmGoodsExt>();
        for (int i = 0; i < goodsInfo.size(); i++) {
            GdsMaster goodsDetail = gdsMasterMapper.selectByPrimaryKey(goodsInfo.get(i).getGoodsId());
            boolean isGoodsInType = false;
            for (int j = 0; j < goodsTypeList.size(); j++) {
                if (goodsDetail.getGoodsTypeIdPath() != null && goodsDetail.getGoodsTypeIdPath().contains(goodsTypeList.get(j).getGoodsId())) {
                    isGoodsInType = true;
                    break;
                }
            }
            if (isGoodsInType) {
                validGoods.add(goodsInfo.get(i));
            }
        }
        return validGoods;
    }

    private List<OrdConfirmGoodsExt> getGoodsInBrand(List<OrdConfirmGoodsExt> goodsInfo, List<CouponGoodsForm> goodsBrandList) {
        List<OrdConfirmGoodsExt> validGoods = new ArrayList<OrdConfirmGoodsExt>();
        for (int i = 0; i < goodsInfo.size(); i++) {
            GdsMaster goodsDetail = gdsMasterMapper.selectByPrimaryKey(goodsInfo.get(i).getGoodsId());
            boolean isGoodsInBrand = false;
            for (int j = 0; j < goodsBrandList.size(); j++) {
                if (goodsBrandList.get(j).getGoodsId().equals(goodsDetail.getBrandId())) {
                    isGoodsInBrand = true;
                    break;
                }
            }
            if (isGoodsInBrand) {
                validGoods.add(goodsInfo.get(i));
            }
        }
        return validGoods;
    }

    private List<OrdConfirmGoodsExt> getGoodsInGoods(List<OrdConfirmGoodsExt> goodsInfo, List<CouponGoodsForm> goodsList) {
        List<OrdConfirmGoodsExt> validGoods = new ArrayList<OrdConfirmGoodsExt>();
        for (int i = 0; i < goodsInfo.size(); i++) {
            boolean isGoodsInGoods = false;
            for (int j = 0; j < goodsList.size(); j++) {
                if (goodsList.get(j).getGoodsId().equals(goodsInfo.get(i).getGoodsId())) {
                    isGoodsInGoods = true;
                    break;
                }
            }
            if (isGoodsInGoods) {
                validGoods.add(goodsInfo.get(i));
            }
        }
        return validGoods;
    }

    private List<OrdConfirmGoodsExt> getGoodsInSku(List<OrdConfirmGoodsExt> goodsInfo, List<CouponGoodsForm> goodsList) {
        List<OrdConfirmGoodsExt> validGoods = new ArrayList<OrdConfirmGoodsExt>();
        for (int i = 0; i < goodsInfo.size(); i++) {
            boolean isGoodsInGoods = false;
            // 套装不计算
            if (!"30".equals(goodsInfo.get(i).getType())) {
                for (int j = 0; j < goodsList.size(); j++) {
                    if (goodsList.get(j).getGoodsId().equals(goodsInfo.get(i).getGoodsId())
                            && goodsList.get(j).getSkuId().equals(goodsInfo.get(i).getOrdConfirmOrderUnitExtList().get(0).getSkuId())) {
                        isGoodsInGoods = true;
                        break;
                    }
                }
            }
            if (isGoodsInGoods) {
                validGoods.add(goodsInfo.get(i));
            }
        }
        return validGoods;
    }

    public CouponMasterExt getOrderCouponById(String memberId, String couponMemberId, float orderPrice) {
        CouponMemberExt form = new CouponMemberExt();
        form.setMemberId(memberId);
        form.setOrderPrice(orderPrice);
        form.setCouponMemberId(couponMemberId);
        List<CouponMasterExt> list = couponMasterMapperExt.getOrderCoupons(form);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }

    }

    @Override
    public JSONObject distributeCoupon(CouponMasterExt form) {
        // 批量发放优惠券
        JSONObject json = new JSONObject();
        try {
            // 检验参数的完整性
            if (StringUtil.isEmpty(form.getCouponId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 获取优惠券主表
            CouponMaster coupon = couponMasterMapper.selectByPrimaryKey(form.getCouponId());
            if (coupon == null) {
                throw new ExceptionErrorData("优惠券不存在!");
            }
            if (Constants.DELETED_YES.equals(coupon.getDeleted())) {
                throw new ExceptionErrorData("优惠券已失效!");
            }
            int distributedCount = coupon.getDistributedCount() == null ? 0 : coupon.getDistributedCount();
            // 获取所有没有此优惠券的用户
            List<String> userList = couponMemberMapperExt.getUserForDistribute(form.getCouponId());
            if (coupon.getMaxCount() != null) {
                int maxCount = coupon.getMaxCount() == null ? 0 : coupon.getMaxCount();
                if (maxCount <= distributedCount) {
                    throw new ExceptionErrorData("优惠劵已经被领光了!");
                }
                if (distributedCount + userList.size() > maxCount) {
                    throw new ExceptionErrorData("要发放的优惠券数量大于优惠券剩余数量!");
                }
            }
            int index = 0;
            for (int i = 0; i < userList.size(); i++) {
                CouponMember cm = new CouponMember();
                cm.setMemberId(userList.get(i));

                if(!StringUtil.isEmpty(coupon.getMemberLevel()) && !"00".equals(coupon.getMemberLevel())){
                    MmbMaster mmbMaster = mmbMasterMapper.selectByPrimaryKey(userList.get(i));
                    if(!coupon.getMemberLevel().equals(mmbMaster.getMemberLevelId())){
                        continue;
                    }
                }
                index++;
                cm.setCouponId(form.getCouponId());
                cm.setCouponMemberId(UUID.randomUUID().toString());
                // 如果有使用期限天数限制，优先级高于开始时间和结束时间
                if(coupon.getValidDays()!=null&&coupon.getValidDays()>0){
                    // 开始日期:今天
                    Calendar startTime = Calendar.getInstance();
                       startTime.set(Calendar.MINUTE, 0);
                    startTime.set(Calendar.SECOND, 0);
                    startTime.set(Calendar.MILLISECOND, 0);
                    cm.setStartTime(startTime.getTime());

                    // 结束日期
                    Calendar endTime = Calendar.getInstance();
                    endTime.setTime(startTime.getTime());
                    endTime.add(Calendar.DAY_OF_MONTH, coupon.getValidDays());
                    cm.setEndTime(endTime.getTime());
                }else{
                    cm.setStartTime(coupon.getStartTime());
                    cm.setEndTime(coupon.getEndTime());
                }
                cm.setDeleted(Constants.DELETED_NO);
                cm.setInsertTime(new Date());
                cm.setStatus(ComCode.CouponStatus.UNUSE);
                couponMemberMapper.insertSelective(cm);
//                if(coupon.getCouponScope().equals("10"))ErpSendUtil.getInstance().CouponSendUpdateById(cm.getCouponMemberId());
                if (coupon.getCouponScope().equals("10"))
                    ErpSendUtil.CouponSendUpdateById(cm.getCouponMemberId(), couponMemberMapper, mmbMasterMapper);
            }
            // 更新领取个数
            if(!StringUtil.isEmpty(coupon.getMemberLevel()) && !"00".equals(coupon.getMemberLevel())){
                coupon.setDistributedCount(distributedCount + index);
                json.put("results", index);
            }else{
                coupon.setDistributedCount(distributedCount + userList.size());
                json.put("results", userList.size());
            }
            couponMasterMapper.updateByPrimaryKeySelective(coupon);

            //json.put("results", userList.size());
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

//    private void sendCouponMemberById(String id) {
//        if (StringUtil.isEmpty(id)) throw new ExceptionErrorParam("优惠券ID未指定");
//        CouponMember master = couponMemberMapper.selectByPrimaryKey(id);
//        if (master == null) {
//            throw new ExceptionErrorData("指定优惠券不存在");
//        }
//        sendCouponMember(master);
//    }

//    private void sendCouponMember(CouponMember record) {
//        CouponMember record1 = new CouponMember();
//        record1.setCouponMemberId(record.getCouponMemberId());
//        record1.setErpSendStatus("10");
//        try {
//            MmbMaster mmb = mmbMasterMapper.selectByPrimaryKey(record.getMemberId());
//            CouponSend send = new CouponSend();
//            send.setCouponMemberId(record.getCouponMemberId());
//            send.setCouponId(record.getCouponId());
//            send.setMemberId(mmb.getTelephone());
//            send.setStatus("10");
//            send.setStartTime(DataUtils.formatTimeStampToYMD(record.getStartTime()));
//            send.setEndTime(DataUtils.formatTimeStampToYMD(record.getEndTime()));
//            send.setSendTime(DataUtils.formatTimeStampToYMD(record.getSendTime()));
//
//            Coupon coupon = new Coupon();
//            coupon.setCouponSend(send);
//
//            String result = soap.couponSendUpdate(getKeyCouponSendUpdate(coupon), coupon);
//            log.debug("ERP couponSendUpdate result code:"+result+",param:"+ JSON.toJSONString(coupon));
//            if (result.equals("00")) {
//            } else if (result.equals("11")) {
//                throw new ExceptionNoPower("ERP数据库连接失败");
//            } else if (result.equals("12")) {
//                throw new ExceptionBusiness("ERP数据库更新失败");
//            } else if (result.equals("21")) {
//                throw new ExceptionErrorParam("ERP验证失败");
//            } else {
//                throw new ExceptionBusiness("ERP未知错误");
//            }
//        } catch (Exception e) {
//            // 若通讯失败,记录失败原因及通讯信息
//            record1.setErpSendStatus("20");
//        } finally {
//            record1.setUpdateTime(new Date());
//            couponMemberMapper.updateByPrimaryKeySelective(record1);
//        }
//    }

//    private void sendCouponUsedById(String id) {
//        if (StringUtil.isEmpty(id)) throw new ExceptionErrorParam("优惠券ID未指定");
//        CouponMember master = couponMemberMapper.selectByPrimaryKey(id);
//        if (master == null) {
//            throw new ExceptionErrorData("指定优惠券不存在");
//        }
//        sendCouponUsed(master);
//    }

//    private void sendCouponUsed(CouponMember record) {
//        CouponMember record1 = new CouponMember();
//        record1.setCouponMemberId(record.getCouponMemberId());
//        record1.setErpSendUsedStatus("10");
//        try {
//            MmbMaster mmb = mmbMasterMapper.selectByPrimaryKey(record.getMemberId());
//            CouponUsed send = new CouponUsed();
//            send.setCouponMemberId(record.getCouponMemberId());
//            send.setCouponId(record.getCouponId());
//            send.setMemberId(mmb.getTelephone());
//            send.setOrderId(record.getOrderId());
//            send.setStatus(record.getStatus());
//            send.setUsedTime(DataUtils.formatTimeStampToYMD(record.getUsedTime()));
//
//            Coupon coupon = new Coupon();
//            coupon.setCouponUsed(send);
//
//            String result = soap.couponUsedUpdate(getKeyCouponUsedUpdate(coupon), coupon);
//            log.debug("ERP couponUsedUpdate result code:"+result+",param:"+ JSON.toJSONString(coupon));
//            if (result.equals("00")) {
//            } else if (result.equals("11")) {
//                throw new ExceptionNoPower("ERP数据库连接失败");
//            } else if (result.equals("12")) {
//                throw new ExceptionBusiness("ERP数据库更新失败");
//            } else if (result.equals("21")) {
//                throw new ExceptionErrorParam("ERP验证失败");
//            } else {
//                throw new ExceptionBusiness("ERP未知错误");
//            }
//        } catch (Exception e) {
//            // 若通讯失败,记录失败原因及通讯信息
//            record1.setErpSendStatus("20");
//        } finally {
//            record1.setUpdateTime(new Date());
//            couponMemberMapper.updateByPrimaryKeySelective(record1);
//        }
//    }

    /**
     * 分发生日优惠券
     *
     * @return
     */
    @Override
    public JSONObject distributeBirthdayCoupon() {
        JSONObject json = new JSONObject();
        try {
            log.debug("开始发放生日劵!");
            log.debug("查询生日劵...");
            // 获取生日劵
            List<CouponMaster> couponList = couponMasterMapperExt.selectBirthdayCouponList();
            if (couponList == null || couponList.size() == 0) {
                throw new ExceptionErrorData("生日券不存在!");
            }
            log.debug("查询到" + couponList.size() + "个生日劵！");
            for (CouponMaster coupon : couponList) {
                log.debug("开始发放ID为 " + coupon.getCouponId() + " 的生日劵:" + coupon.getCouponName());
                if (coupon.getSendStartTime().compareTo(new Date()) > 0 || coupon.getSendEndTime().compareTo(new Date()) < 0) {
                    log.debug("当前不在发放期间内.!");
                    continue;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String birthdayStart = sdf.format(coupon.getSendStartTime());
                // 获取需要发劵的用户
                log.debug("查询符合条件的用户...");
                MmbMasterExt mme = new MmbMasterExt();
                mme.setBirthdayStart(birthdayStart);
                mme.setMemberLevelId(coupon.getMemberLevel());
                List<MmbMaster> mmbMasterList = mmbMasterMapperExt.queryForDistributeBirthdayCoupon(mme);
                log.debug("查询到" + mmbMasterList.size() + "个用户！");
                // 发放生日优惠券
                for (int i = 0; i < mmbMasterList.size(); i++) {
                    CouponMemberExt form = new CouponMemberExt();
                    form.setMemberId(mmbMasterList.get(i).getMemberId());
                    Calendar c = Calendar.getInstance();
                    int nowYear = c.get(Calendar.YEAR);
                    c.setTime(mmbMasterList.get(i).getBirthdate());
                    c.set(Calendar.YEAR, nowYear);
                    form.setStartTime(c.getTime());
                    try {
                        addBirthdayCouponsForUser(form, coupon);
                    } catch (Exception e) {
                        log.debug("用户生日劵发放失败!");
                        log.debug("用户MemberID:!" + form.getMemberId());
                        log.debug("生日劵ID:!" + coupon.getCouponId());
                        log.debug("失败原因：" + e.getMessage());
                    }
                }
            }
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            log.debug("生日劵发放失败，原因：" + e.getMessage());
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addBirthdayCouponsForUser(CouponMemberExt form, CouponMaster coupon) throws Exception {
        if (StringUtil.isEmpty(form.getMemberId())) {
            throw new ExceptionErrorParam("缺少参数:memberId");
        }
        if (form.getStartTime() == null) {
            throw new ExceptionErrorParam("缺少参数:startTime");
        }

        int distributedCount = coupon.getDistributedCount() == null ? 0 : coupon.getDistributedCount();
        if (coupon.getMaxCount() != null) {
            int maxCount = coupon.getMaxCount();
            if (maxCount <= distributedCount) {
                throw new ExceptionErrorData("优惠劵已经被领光了!");
            }
        }
        CouponMember cm = new CouponMember();
        cm.setMemberId(form.getMemberId());
        cm.setCouponId(coupon.getCouponId());
        cm.setCouponMemberId(UUID.randomUUID().toString());
        cm.setDeleted(Constants.DELETED_NO);
        cm.setInsertTime(new Date());
        cm.setStatus(ComCode.CouponStatus.UNUSE);
        cm.setSendTime(new Date());
        // 开始日期
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(form.getStartTime());
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        cm.setStartTime(startTime.getTime());

        // 结束日期
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(startTime.getTime());
        endTime.add(Calendar.DAY_OF_MONTH, coupon.getValidDays());
        cm.setEndTime(endTime.getTime());
        couponMemberMapper.insertSelective(cm);
        // 更新领取个数
        coupon.setDistributedCount(distributedCount + 2);
        couponMasterMapper.updateByPrimaryKeySelective(coupon);
        // 同步到ERP
//        if(coupon.getCouponScope().equals("10")) ErpSendUtil.getInstance().CouponSendUpdateById(cm.getCouponMemberId());
        if (coupon.getCouponScope().equals("10"))
            ErpSendUtil.CouponSendUpdateById(cm.getCouponMemberId(), couponMemberMapper, mmbMasterMapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addRegisterCouponsForUser(CouponMemberExt form, CouponMaster coupon) throws Exception {
        if (StringUtil.isEmpty(form.getMemberId())) {
            throw new ExceptionErrorParam("缺少参数");
        }

        int distributedCount = coupon.getDistributedCount() == null ? 0 : coupon.getDistributedCount();
        if (coupon.getMaxCount() != null) {
            int maxCount = coupon.getMaxCount();
            if (maxCount <= distributedCount) {
                throw new ExceptionErrorData("优惠劵已经被领光了!");
            }
        }
        CouponMember cm = new CouponMember();
        cm.setMemberId(form.getMemberId());
        cm.setCouponId(coupon.getCouponId());
        cm.setCouponMemberId(UUID.randomUUID().toString());
        cm.setDeleted(Constants.DELETED_NO);
        cm.setInsertTime(new Date());
        cm.setStatus(ComCode.CouponStatus.UNUSE);
        cm.setSendTime(new Date());
        // 开始日期
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        cm.setStartTime(startTime.getTime());

        // 结束日期
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(startTime.getTime());
        endTime.add(Calendar.DAY_OF_MONTH, coupon.getValidDays());
        cm.setEndTime(endTime.getTime());
        couponMemberMapper.insertSelective(cm);
        // 更新领取个数
        coupon.setDistributedCount(distributedCount + 1);
        couponMasterMapper.updateByPrimaryKeySelective(coupon);
        // 同步到ERP
//        if(coupon.getCouponScope().equals("10"))ErpSendUtil.getInstance().CouponSendUpdateById(cm.getCouponMemberId());
        if (coupon.getCouponScope().equals("10"))
            ErpSendUtil.CouponSendUpdateById(cm.getCouponMemberId(), couponMemberMapper, mmbMasterMapper);
    }

    /**
     * 根据条件获取会员优惠券列表
     *
     * @param form
     * @return
     */
    public JSONObject selectRecordByPage(CouponMemberExt form) {
        JSONObject json = new JSONObject();
        try {
            List<CouponMemberExt> list = (List<CouponMemberExt>) couponMemberMapperExt.selectRecordByPage(form);
            int countAll = couponMemberMapperExt.getRecordCountByPage(form);
            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", countAll);
            json.put("iTotalDisplayRecords", countAll);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject sendToMember(CouponMember form) {
        // 补发优惠券
        JSONObject json = new JSONObject();
        try {
            // 检验参数的完整性
            if (StringUtil.isEmpty(form.getCouponId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 获取优惠券主表
            CouponMaster coupon = couponMasterMapper.selectByPrimaryKey(form.getCouponId());
            if (coupon == null) {
                throw new ExceptionErrorData("优惠券不存在!");
            }
            if (Constants.DELETED_YES.equals(coupon.getDeleted())) {
                throw new ExceptionErrorData("优惠券已失效!");
            }
            int distributedCount = coupon.getDistributedCount() == null ? 0 : coupon.getDistributedCount();

            //插入优惠券会员表
            //ID
            form.setCouponMemberId(UUID.randomUUID().toString());
            //会员ID优惠券ID,更新插入用户已传入
            // 开始日期结束日期
            //TODO优惠券
            // 如果有使用期限天数限制，优先级高于开始时间和结束时间
            if(coupon.getValidDays()!=null&&coupon.getValidDays()>0){
                // 开始日期:今天
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, 0);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.SECOND, 0);
                startTime.set(Calendar.MILLISECOND, 0);
                form.setStartTime(startTime.getTime());

                // 结束日期
                Calendar endTime = Calendar.getInstance();
                endTime.setTime(startTime.getTime());
                endTime.add(Calendar.DAY_OF_MONTH, coupon.getValidDays());
                form.setEndTime(endTime.getTime());
            }else{
                form.setStartTime(coupon.getStartTime());
                form.setEndTime(coupon.getEndTime());
            }
            //状态未使用
            form.setStatus(ComCode.CouponStatus.UNUSE);
            //取数据库默认值
            form.setShopId(Constants.ORGID);
//            form.setSendTime(new Date());
//            form.setDeleted(Constants.DELETED_NO);
//            form.setInsertTime(new Date());
//            form.setUpdateTime(new Date());
            couponMemberMapper.insertSelective(form);

            // 更新领取个数
            CouponMaster master = new CouponMaster();
            master.setCouponId(coupon.getCouponId());
            master.setDistributedCount(distributedCount + 1);
            master.setUpdateTime(new Date());
            couponMasterMapper.updateByPrimaryKeySelective(master);
            //ERP发送
//            sendCouponMember(form);
//            if(coupon.getCouponScope().equals("10"))ErpSendUtil.getInstance().CouponSendUpdateById(form.getCouponMemberId());
            if (coupon.getCouponScope().equals("10"))
                ErpSendUtil.CouponSendUpdateById(form.getCouponMemberId(), couponMemberMapper, mmbMasterMapper);

            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

}
