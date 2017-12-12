package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.CouponMasterExt;
import net.dlyt.qyds.common.dto.ext.CouponMemberExt;
import net.dlyt.qyds.common.form.ActGoodsForm;
import net.dlyt.qyds.common.form.ActMasterForm;
import net.dlyt.qyds.common.form.CouponGoodsForm;
import net.dlyt.qyds.dao.CouponGoodsMapper;
import net.dlyt.qyds.dao.CouponMasterMapper;
import net.dlyt.qyds.dao.GdsBrandMapper;
import net.dlyt.qyds.dao.GdsTypeMapper;
import net.dlyt.qyds.dao.ext.CouponGoodsMapperExt;
import net.dlyt.qyds.dao.ext.CouponMasterMapperExt;
import net.dlyt.qyds.dao.ext.ErpGoodsMapperExt;
import net.dlyt.qyds.dao.ext.GdsMasterMapperExt;
import net.dlyt.qyds.web.service.CouponMasterService;
import net.dlyt.qyds.web.service.common.ComCode;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.ErpSendUtil;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by cjk on 16/8/6.
 */
@Service("couponMasterService")
public class CouponMasterServiceImpl implements CouponMasterService {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
    protected final Logger log = LoggerFactory.getLogger(CouponMasterServiceImpl.class);
    @Autowired
    CouponMasterMapper couponMasterMapper;
    @Autowired
    CouponMasterMapperExt couponMasterMapperExt;
    @Autowired
    CouponGoodsMapperExt couponGoodsMapperExt;
    @Autowired
    CouponGoodsMapper couponGoodsMapper;
    @Autowired
    private GdsBrandMapper gdsBrandMapper;
    @Autowired
    private GdsTypeMapper gdsTypeMapper;
    @Autowired
    private GdsMasterMapperExt gdsMasterMapperExt;
    @Autowired
    private ErpGoodsMapperExt erpGoodsMapperExt;

    public JSONObject getList(CouponMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            //数据库检索 -- 过滤数据
            form.setDeleted(Constants.DELETED_NO);
            List<CouponMaster> list = couponMasterMapperExt.select(form);
            int allCount = couponMasterMapperExt.selectCount(form);
            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
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

    public JSONObject getApproveList(CouponMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            //数据库检索 -- 过滤数据
            form.setDeleted(Constants.DELETED_NO);
            List<CouponMaster> list = couponMasterMapperExt.selectApproveList(form);
            int allCount = couponMasterMapperExt.selectApproveCount(form);
            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
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

    public JSONObject getDetail(CouponMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getCouponId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            CouponMasterExt coupon = couponMasterMapperExt.selectById(form);
            if (coupon == null) {
                throw new ExceptionErrorData("活动优惠券不存在");
            }
            List<CouponGoodsForm> goodsList = null;
            CouponGoods goods = new CouponGoods();
            goods.setCouponId(form.getCouponId());
            if (ComCode.ActivityGoodsType.ALL.equals(coupon.getGoodsType())) {
                //全部商品

            } else if (ComCode.ActivityGoodsType.TYPE.equals(coupon.getGoodsType())) {
                // 按分类
                goodsList = couponGoodsMapperExt.selectGoodsTypeByCouponId(goods);
            } else if (ComCode.ActivityGoodsType.BRAND.equals(coupon.getGoodsType())) {
                // 按品牌
                goodsList = couponGoodsMapperExt.selectGoodsBrandByCouponId(goods);
            } else if (ComCode.ActivityGoodsType.GOODS.equals(coupon.getGoodsType())) {
                // 按商品
                goodsList = couponGoodsMapperExt.selectGoodsByCouponId(goods);
            } else if (ComCode.ActivityGoodsType.SKU.equals(coupon.getGoodsType())) {
                // 按SKU
                goodsList = couponGoodsMapperExt.selectSkuByCouponId(goods);
            }
            coupon.setGoodsList(goodsList);
            json.put("data", coupon);
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


    public JSONObject delete(CouponMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getCouponId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            CouponMaster record = new CouponMaster();
            record.setCouponId(form.getCouponId());
            record.setDeleted(Constants.DELETED_YES);
            couponMasterMapper.updateByPrimaryKeySelective(record);
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
    public JSONObject add(CouponMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getCouponId())) {
                throw new ExceptionErrorParam("缺少参数：优惠劵ID");
            }
            if (StringUtil.isEmpty(form.getCouponName())) {
                throw new ExceptionErrorParam("缺少参数：优惠劵名称");
            }
            if (StringUtil.isEmpty(form.getCouponType())) {
                throw new ExceptionErrorParam("缺少参数：优惠劵类型");
            }
            if (StringUtil.isEmpty(form.getCouponStyle())) {
                throw new ExceptionErrorParam("缺少参数：优惠方式");
            }
            if (form.getPerMaxCount() == null) {
                throw new ExceptionErrorParam("缺少参数：每人最大领取数量");
            }
            if ("0".equals(form.getCouponStyle())) {
                //抵值
                if (form.getWorth() == null) {
                    throw new ExceptionErrorParam("缺少参数：优惠劵价值");
                }
            } else if ("1".equals(form.getCouponStyle())) {
                // 折扣
                if (form.getDiscount() == null) {
                    throw new ExceptionErrorParam("缺少参数：优惠劵折扣");
                }
                if (form.getDiscount().compareTo(new BigDecimal(9.9)) > 0
                        || form.getDiscount().compareTo(new BigDecimal(0.1)) < 0) {
                    throw new ExceptionErrorParam("优惠劵折扣超出范围");
                }

            }
            if (ComCode.CouponType.BIRTHDAY_SEND.equals(form.getCouponType())) {
                // 生日劵
                if (StringUtil.isEmpty(form.getMemberLevel())) {
                    throw new ExceptionErrorParam("缺少参数：限制会员级别");
                }
            }

            // 生日劵或注册劵必须限制使用天数
            if (ComCode.CouponType.BIRTHDAY_SEND.equals(form.getCouponType())
                    || ComCode.CouponType.REGIST_SEND.equals(form.getCouponType())) {
//                if (StringUtil.isEmpty(form.getStartTimeStr())) {
//                    throw new ExceptionErrorParam("缺少参数：开始时间");
//                }
//                if (StringUtil.isEmpty(form.getEndTimeStr())) {
//                    throw new ExceptionErrorParam("缺少参数：结束时间");
//                }
                if (form.getValidDays() == null) {
                    throw new ExceptionErrorParam("缺少参数:有效天数");
                }
            } else {
                // 其他劵既可以限制天数也可以限制期限，但必须二选一
                if (form.getValidDays() == null &&
                        (StringUtil.isEmpty(form.getStartTimeStr()) || StringUtil.isEmpty(form.getEndTimeStr()))) {
                    throw new ExceptionErrorParam("缺少参数:有效天数或生效时间和失效时间");
                }
            }
            if (StringUtil.isEmpty(form.getSendStartTimeStr())) {
                throw new ExceptionErrorParam("缺少参数：开始发放时间");
            }
            if (StringUtil.isEmpty(form.getSendEndTimeStr())) {
                throw new ExceptionErrorParam("缺少参数：结束发放时间");
            }
            // 判断优惠券名称是否重复
            CouponMaster form1 = new CouponMaster();
            form1.setCouponName(form.getCouponName());
            int count = couponMasterMapperExt.checkExistByCouponName(form1);
            if (count > 0) {
                throw new ExceptionErrorData("已经存在相同名称的优惠券");
            }
            // 插入优惠券主表
            Date date = new Date();
            String id = form.getCouponId();
            CouponMaster at = new CouponMaster();
            at.setCouponId(id);
            at.setDeleted(Constants.DELETED_NO);
            at.setCouponName(form.getCouponName());
            at.setCouponType(form.getCouponType());
            at.setDistributeType(form.getDistributeType());
            at.setCouponCode(form.getCouponCode());
            at.setCouponImage(form.getCouponImage());
            at.setWorth(form.getWorth());
            at.setGoodsType(form.getGoodsType());
            at.setIsOriginPrice(form.getIsOriginPrice());
            at.setMinGoodsCount(form.getMinGoodsCount());
            at.setCouponScope(form.getCouponScope());
            at.setMinOrderPrice(form.getMinOrderPrice());
            at.setMaxCount(form.getMaxCount());
            at.setApplyContent(form.getApplyContent());
            at.setApproveStatus(ComCode.ApproveStatus.APPROVE_STATUS_NOT_APPLY);
            at.setShopId(form.getShopId());
            at.setComment(form.getComment());
            at.setPerMaxCount(form.getPerMaxCount());
            at.setPrice(form.getPrice());
            if (!StringUtil.isEmpty(form.getStartTimeStr())) {
                at.setStartTime(sdf.parse(form.getStartTimeStr() + " 00:00:00"));
            }
            if (!StringUtil.isEmpty(form.getEndTimeStr())) {
                at.setEndTime(sdf.parse(form.getEndTimeStr() + " 23:59:59"));
            }
            at.setSendStartTime(sdf.parse(form.getSendStartTimeStr() + " 00:00:00"));
            at.setSendEndTime(sdf.parse(form.getSendEndTimeStr() + " 23:59:59"));
            at.setInsertUserId(form.getInsertUserId());
            at.setInsertTime(date);
            at.setExchangePoint(form.getExchangePoint());
            at.setMemberLevel(form.getMemberLevel());
            at.setCouponStyle(form.getCouponStyle());
            at.setDiscount(form.getDiscount());
            if (form.getValidDays() == null) {
                at.setValidDays(0);
            } else {
                at.setValidDays(form.getValidDays());
            }
            at.setIsValid(form.getIsValid());
            if("60".equals(form.getGoodsType())){
                at.setGoodsTypeValue(form.getActivityGoodsSellYear().concat("_").concat(form.getActivityGoodsSeasonCode()));
            }else if("70".equals(form.getGoodsType())){
                at.setGoodsTypeValue(form.getActivityGoodsErpBrand());
            }else if("80".equals(form.getGoodsType())){
                at.setGoodsTypeValue(form.getActivityGoodsLineCode());
            }
            couponMasterMapper.insertSelective(at);
            form.setCouponId(id);
            // 年/季节
            if(!StringUtil.isEmpty(form.getActivityGoodsSellYear())){
                //处理年份还有季节
                //按照年份查询sku
                List<Map>  list = couponGoodsMapperExt.selectSKUByYearAndSeason(form);
                //添加sku信息
                for(Map map:list){
                    String sku = (String) map.get("sku");
                    String goodsId = (String) map.get("goods_id");

                    CouponGoods record = new CouponGoods();
                    record.setCouponGoodsId(UUID.randomUUID().toString());
                    record.setShopId("00000000");
                    record.setCouponId(form.getCouponId());
                    record.setGoodsType("50");
                    record.setGoodsId(goodsId);
                    record.setSkuId(sku);
                    record.setDeleted("0");
                    record.setUpdateTime(date);
                    record.setGoodsTypeValue(form.getGoodsType());
                    record.setUpdateUserId(form.getUpdateUserId());
                    record.setInsertTime(date);
                    record.setInsertUserId(form.getUpdateUserId());
                    couponGoodsMapper.insertSelective(record);
                }
            }

            if(!StringUtil.isEmpty(form.getActivityGoodsErpBrand())){
                //处理ERP品牌
                List<Map>  list = couponGoodsMapperExt.selectSKUByErpBrand(form);

                //添加sku信息
                for(Map map:list){
                    String sku = (String) map.get("sku");
                    String goodsId = (String) map.get("goods_id");

                    CouponGoods record = new CouponGoods();
                    record.setCouponGoodsId(UUID.randomUUID().toString());
                    record.setShopId("00000000");
                    record.setCouponId(form.getCouponId());
                    record.setGoodsType("50");
                    record.setGoodsId(goodsId);
                    record.setSkuId(sku);
                    record.setDeleted("0");
                    record.setUpdateTime(date);
                    record.setGoodsTypeValue(form.getGoodsType());
                    record.setUpdateUserId(form.getUpdateUserId());
                    record.setInsertTime(date);
                    record.setInsertUserId(form.getUpdateUserId());
                    couponGoodsMapper.insertSelective(record);
                }
            }

            if(!StringUtil.isEmpty(form.getActivityGoodsLineCode())){
                //处理ERP品牌
                List<Map>  list = couponGoodsMapperExt.selectSKUByLineCode(form);

                //添加sku信息
                for(Map map:list){
                    String sku = (String) map.get("sku");
                    String goodsId = (String) map.get("goods_id");

                    CouponGoods record = new CouponGoods();
                    record.setCouponGoodsId(UUID.randomUUID().toString());
                    record.setShopId("00000000");
                    record.setCouponId(form.getCouponId());
                    record.setGoodsType("50");
                    record.setGoodsId(goodsId);
                    record.setSkuId(sku);
                    record.setDeleted("0");
                    record.setUpdateTime(date);
                    record.setGoodsTypeValue(form.getGoodsType());
                    record.setUpdateUserId(form.getUpdateUserId());
                    record.setInsertTime(date);
                    record.setInsertUserId(form.getUpdateUserId());
                    couponGoodsMapper.insertSelective(record);
                }
            }
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
    public JSONObject edit(CouponMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getCouponName())) {
                throw new ExceptionErrorParam("缺少参数：优惠劵名称");
            }
            if (StringUtil.isEmpty(form.getCouponType())) {
                throw new ExceptionErrorParam("缺少参数：优惠劵类型");
            }
            if (StringUtil.isEmpty(form.getCouponStyle())) {
                throw new ExceptionErrorParam("缺少参数：优惠方式");
            }
            if (form.getPerMaxCount() == null) {
                throw new ExceptionErrorParam("缺少参数：每人最大领取数量");
            }
            if ("0".equals(form.getCouponStyle())) {
                //抵值
                if (form.getWorth() == null) {
                    throw new ExceptionErrorParam("缺少参数：优惠劵价值");
                }
            } else if ("1".equals(form.getCouponStyle())) {
                // 折扣
                if (form.getDiscount() == null) {
                    throw new ExceptionErrorParam("缺少参数：优惠劵折扣");
                }
                if (form.getDiscount().compareTo(new BigDecimal(9.9)) > 0
                        || form.getDiscount().compareTo(new BigDecimal(0.1)) < 0) {
                    throw new ExceptionErrorParam("优惠劵折扣超出范围");
                }
            }
            if (ComCode.CouponType.BIRTHDAY_SEND.equals(form.getCouponType())) {
                // 生日劵
                if (StringUtil.isEmpty(form.getMemberLevel())) {
                    throw new ExceptionErrorParam("缺少参数：限制会员级别");
                }
            }
            // 生日劵或注册劵必须限制使用天数
            if (ComCode.CouponType.BIRTHDAY_SEND.equals(form.getCouponType())
                    || ComCode.CouponType.REGIST_SEND.equals(form.getCouponType())) {
//                if (StringUtil.isEmpty(form.getStartTimeStr())) {
//                    throw new ExceptionErrorParam("缺少参数：开始时间");
//                }
//                if (StringUtil.isEmpty(form.getEndTimeStr())) {
//                    throw new ExceptionErrorParam("缺少参数：结束时间");
//                }
                if (form.getValidDays() == null) {
                    throw new ExceptionErrorParam("缺少参数:有效天数");
                }
            } else {
                // 其他劵既可以限制天数也可以限制期限，但必须二选一
                if (form.getValidDays() == null &&
                        (StringUtil.isEmpty(form.getStartTimeStr()) || StringUtil.isEmpty(form.getEndTimeStr()))) {
                    throw new ExceptionErrorParam("缺少参数:有效天数或生效时间和失效时间");
                }
            }
            if (StringUtil.isEmpty(form.getSendStartTimeStr())) {
                throw new ExceptionErrorParam("缺少参数：开始发放时间");
            }
            if (StringUtil.isEmpty(form.getSendEndTimeStr())) {
                throw new ExceptionErrorParam("缺少参数：结束发放时间");
            }
            // 检索出原来的数据
            CouponMaster oldData = couponMasterMapper.selectByPrimaryKey(form.getCouponId());
            if (oldData == null || Constants.DELETED_YES.equals(oldData.getDeleted())) {
                throw new ExceptionErrorData("优惠券不存在");
            }
            if (!oldData.getCouponName().equals(form.getCouponName())) {
                // 优惠券名称发生变化
                // 判断优惠券名称是否重复
                CouponMaster form1 = new CouponMaster();
                form1.setCouponName(form.getCouponName());
                int count = couponMasterMapperExt.checkExistByCouponName(form1);
                if (count > 0) {
                    throw new ExceptionErrorData("已经存在相同名称的优惠券");
                }
            }

            // 更新优惠券主表
            Date date = new Date();
            form.setUpdateUserId(form.getUpdateUserId());
            form.setUpdateTime(date);
            if (!StringUtil.isEmpty(form.getStartTimeStr())) {
                form.setStartTime(sdf.parse(form.getStartTimeStr() + " 00:00:00"));
            }
            if (!StringUtil.isEmpty(form.getEndTimeStr())) {
                form.setEndTime(sdf.parse(form.getEndTimeStr() + " 23:59:59"));
            }
            form.setSendStartTime(sdf.parse(form.getSendStartTimeStr() + " 00:00:00"));
            form.setSendEndTime(sdf.parse(form.getSendEndTimeStr() + " 23:59:59"));
            if (form.getValidDays() == null) {
                form.setValidDays(0);
            }
            // 优惠劵经编辑之后状态编成待审批,未启用
            form.setApproveStatus(ComCode.ApproveStatus.APPROVE_STATUS_NOT_APPLY);
            form.setIsValid("0");
            if("60".equals(form.getGoodsType())){
                form.setGoodsTypeValue(form.getActivityGoodsSellYear().concat("_").concat(form.getActivityGoodsSeasonCode()));
            }else if("70".equals(form.getGoodsType())){
                form.setGoodsTypeValue(form.getActivityGoodsErpBrand());
            }else if("80".equals(form.getGoodsType())){
                form.setGoodsTypeValue(form.getActivityGoodsLineCode());
            }
            couponMasterMapper.updateByPrimaryKeySelective(form);

            // 年/季节
            if(!StringUtil.isEmpty(form.getActivityGoodsSellYear())){
                //处理年份还有季节
                //按照年份查询sku
                List<Map>  list = couponGoodsMapperExt.selectSKUByYearAndSeason(form);
                //删除对应的sku
                CouponGoods couponGoods = new CouponGoods();
                couponGoods.setCouponId(form.getCouponId());
                couponGoodsMapperExt.deleteByCouponId(couponGoods);
                //添加sku信息
                for(Map map:list){
                    String sku = (String) map.get("sku");
                    String goodsId = (String) map.get("goods_id");

                    CouponGoods record = new CouponGoods();
                    record.setCouponGoodsId(UUID.randomUUID().toString());
                    record.setShopId("00000000");
                    record.setCouponId(form.getCouponId());
                    record.setGoodsType("50");
                    record.setGoodsId(goodsId);
                    record.setSkuId(sku);
                    record.setDeleted("0");
                    record.setUpdateTime(date);
                    record.setGoodsTypeValue(form.getGoodsType());
                    record.setUpdateUserId(form.getUpdateUserId());
                    record.setInsertTime(date);
                    record.setInsertUserId(form.getUpdateUserId());
                    couponGoodsMapper.insertSelective(record);
                }
            }

            if(!StringUtil.isEmpty(form.getActivityGoodsErpBrand())){
                //处理ERP品牌
                List<Map>  list = couponGoodsMapperExt.selectSKUByErpBrand(form);
                //删除对应的sku
                CouponGoods couponGoods = new CouponGoods();
                couponGoods.setCouponId(form.getCouponId());
                couponGoodsMapperExt.deleteByCouponId(couponGoods);
                //添加sku信息
                for(Map map:list){
                    String sku = (String) map.get("sku");
                    String goodsId = (String) map.get("goods_id");

                    CouponGoods record = new CouponGoods();
                    record.setCouponGoodsId(UUID.randomUUID().toString());
                    record.setShopId("00000000");
                    record.setCouponId(form.getCouponId());
                    record.setGoodsType("50");
                    record.setGoodsId(goodsId);
                    record.setSkuId(sku);
                    record.setDeleted("0");
                    record.setUpdateTime(date);
                    record.setGoodsTypeValue(form.getGoodsType());
                    record.setUpdateUserId(form.getUpdateUserId());
                    record.setInsertTime(date);
                    record.setInsertUserId(form.getUpdateUserId());
                    couponGoodsMapper.insertSelective(record);
                }
            }

            if(!StringUtil.isEmpty(form.getActivityGoodsLineCode())){
                //处理ERP品牌
                List<Map>  list = couponGoodsMapperExt.selectSKUByLineCode(form);
                //删除对应的sku
                CouponGoods couponGoods = new CouponGoods();
                couponGoods.setCouponId(form.getCouponId());
                couponGoodsMapperExt.deleteByCouponId(couponGoods);
                //添加sku信息
                for(Map map:list){
                    String sku = (String) map.get("sku");
                    String goodsId = (String) map.get("goods_id");

                    CouponGoods record = new CouponGoods();
                    record.setCouponGoodsId(UUID.randomUUID().toString());
                    record.setShopId("00000000");
                    record.setCouponId(form.getCouponId());
                    record.setGoodsType("50");
                    record.setGoodsId(goodsId);
                    record.setSkuId(sku);
                    record.setDeleted("0");
                    record.setUpdateTime(date);
                    record.setGoodsTypeValue(form.getGoodsType());
                    record.setUpdateUserId(form.getUpdateUserId());
                    record.setInsertTime(date);
                    record.setInsertUserId(form.getUpdateUserId());
                    couponGoodsMapper.insertSelective(record);
                }
            }

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

    /**
     * 申请审批
     *
     * @param form
     * @
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject apply(CouponMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getCouponId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 获取当前审批状态
            CouponMaster coupon = couponMasterMapper.selectByPrimaryKey(form.getCouponId());
            if (coupon == null) {
                throw new ExceptionErrorData("优惠券不存在");
            }
            if (!ComCode.ApproveStatus.APPROVE_STATUS_NOT_APPLY.equals(coupon.getApproveStatus())) {
                throw new ExceptionErrorData("审批状态不正确");
            }
            coupon.setApplyContent(form.getApplyContent());
            coupon.setApproveStatus(ComCode.ApproveStatus.APPROVE_STATUS_APPROVING);
            coupon.setApplyUserId(form.getApplyUserId());
            coupon.setApplyTime(new Date());
            couponMasterMapper.updateByPrimaryKeySelective(coupon);
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

    /**
     * 审批通过
     *
     * @param form
     * @
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject approve(CouponMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getCouponId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 获取当前审批状态
            CouponMaster coupon = couponMasterMapper.selectByPrimaryKey(form.getCouponId());
            if (coupon == null) {
                throw new ExceptionErrorData("优惠券不存在");
            }
            if (!ComCode.ApproveStatus.APPROVE_STATUS_APPROVING.equals(coupon.getApproveStatus())) {
                throw new ExceptionErrorData("审批状态不正确");
            }
            // 如果是生日劵,要顶掉之前的生日劵
            // 如果是注册劵,要顶掉之前的注册劵
            // 将所有的生日劵变成deleted
            if (ComCode.CouponType.BIRTHDAY_SEND.equals(coupon.getCouponType())
                    || ComCode.CouponType.REGIST_SEND.equals(coupon.getCouponType())) {
                couponMasterMapperExt.setOnlyCoupon(coupon);
            }
            coupon.setApproveContent(form.getApproveContent());
            coupon.setApproveStatus(ComCode.ApproveStatus.APPROVE_STATUS_APPROVED);
            coupon.setApproveUserId(form.getApproveUserId());
            coupon.setApproveTime(new Date());
            couponMasterMapper.updateByPrimaryKeySelective(coupon);
            // 同步到ERP
//            if(coupon.getCouponScope().equals("10")) ErpSendUtil.getInstance().CouponissueUpdateById(coupon.getCouponId());
            if (coupon.getCouponScope().equals("10")) ErpSendUtil.CouponissueUpdateById(
                    coupon.getCouponId(), couponMasterMapperExt, couponGoodsMapperExt, gdsTypeMapper, gdsBrandMapper, gdsMasterMapperExt, couponMasterMapper);
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

    /**
     * 审批拒绝
     *
     * @param form
     * @
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject reject(CouponMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getCouponId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 获取当前审批状态
            CouponMaster coupon = couponMasterMapper.selectByPrimaryKey(form.getCouponId());
            if (coupon == null) {
                throw new ExceptionErrorData("优惠券不存在");
            }
            if (!ComCode.ApproveStatus.APPROVE_STATUS_APPROVING.equals(coupon.getApproveStatus())) {
                throw new ExceptionErrorData("审批状态不正确");
            }
            coupon.setApproveContent(form.getApproveContent());
            coupon.setApproveStatus(ComCode.ApproveStatus.APPROVE_STATUS_REJECT);
            coupon.setApproveUserId(form.getApproveUserId());
            coupon.setApproveTime(new Date());
            couponMasterMapper.updateByPrimaryKeySelective(coupon);
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

    /**
     * 获取PC购物券
     *
     * @param data
     * @return
     */
    public JSONObject getPcCouponList(String data) {
        JSONObject json = new JSONObject();
        try {
            //数据库检索 -- 过滤数据
            JSONObject obj = JSON.parseObject(data);
//            CouponMasterExt form = JSON.parseObject(data, CouponMasterExt.class);
//            CouponMasterExt form = new CouponMasterExt();
            CouponMemberExt form = new CouponMemberExt();
            String memberId = (String) obj.get("memberId");
            int pageSize = (Integer) obj.get("pageSize");
            if (obj.containsKey("currentPage")) {
                int currentPage = (Integer) obj.get("currentPage");
                int iDisplayStart = (currentPage - 1) * pageSize;
                form.setiDisplayStart(iDisplayStart);
                json.put("currentPage", currentPage);
            }
            if (obj.containsKey("currentPagePhone")) {
                int currentPhonePage = (Integer) obj.get("currentPagePhone");
                int iDisplayPhoneStart = (currentPhonePage - 1) * pageSize;
                form.setiDisplayStart(iDisplayPhoneStart);
            }


            form.setiDisplayLength(pageSize);
            form.setMemberId(memberId);
            form.setStatus(obj.getString("status"));
            form.setDeleted(Constants.DELETED_NO);
            List<CouponMasterExt> list = couponMasterMapperExt.getMyCouponsForPc(form);
            form.setStatus("");
            int allCount = couponMasterMapperExt.getMyCouponsCountForPc(form);
            //已使用
            form.setStatus("20");
            int usedCount = couponMasterMapperExt.getMyCouponsCountForPc(form);
            //未使用
            form.setStatus("10");
            int notUsedCount = couponMasterMapperExt.getMyCouponsCountForPc(form);
            json.put("data", list);
            json.put("pageSize", pageSize);
            json.put("iTotalRecords", allCount);
            json.put("usedCount", usedCount);
            json.put("notUsedCount", notUsedCount);
            json.put("totalPage", (allCount % pageSize) == 0 ? allCount / pageSize : (allCount / pageSize + 1));
            json.put("iTotalDisplayRecords", allCount);
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

    /**
     * 获取PC购物券
     *
     * @param data
     * @return
     */
    public JSONObject getPcCoupon(String data) {
        JSONObject json = new JSONObject();
        try {
            //数据库检索 -- 过滤数据
            JSONObject obj = JSON.parseObject(data);
            CouponMemberExt form = new CouponMemberExt();
            String memberId = (String) obj.get("memberId");
            form.setMemberId(memberId);
            form.setDeleted(Constants.DELETED_NO);
            int allCount = couponMasterMapperExt.getMyCouponsCountForPc(form);
            json.put("results", allCount);
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

    /**
     * 发送指定的优惠券
     */
//    private void sendCouponById(String id) {
//        if (StringUtil.isEmpty(id)) throw new ExceptionErrorParam("优惠券ID未指定");
//        CouponMaster master = couponMasterMapper.selectByPrimaryKey(id);
//        if (master == null) {
//            throw new ExceptionErrorData("指定优惠券不存在");
//        }
//        sendCoupon(master);
//    }
//
//    private void sendCoupon(CouponMaster master) {
//        CouponMaster couponMaster = new CouponMaster();
//        couponMaster.setCouponId(master.getCouponId());
//        couponMaster.setErpSendStatus("10");
//        try {
//            //订单主表
//            CouponIssue couponIssue = new CouponIssue();
//            couponIssue.setCouponId(master.getCouponId());
//            couponIssue.setCouponName(master.getCouponName());
//            couponIssue.setCouponStyle(master.getCouponStyle());
//            couponIssue.setCouponType(master.getCouponType());
//            couponIssue.setGoodsType(master.getGoodsType());
//            couponIssue.setDistributeType(master.getDistributeType());
//            couponIssue.setIsOriginPrice(master.getIsOriginPrice());
//            couponIssue.setMinGoodsCount(master.getMinGoodsCount());
//            couponIssue.setMinOrderPrice(master.getMinOrderPrice());
//            couponIssue.setWorth(master.getWorth() == null ? "" : master.getWorth().toString());
//            couponIssue.setDiscount(master.getDiscount() == null ? "" : master.getDiscount().toString());
//            couponIssue.setSendStartTime(DataUtils.formatTimeStampToYMD(master.getSendStartTime()));
//            couponIssue.setSendEndTime(DataUtils.formatTimeStampToYMD(master.getSendEndTime()));
//            couponIssue.setStartTime(DataUtils.formatTimeStampToYMD(master.getStartTime()));
//            couponIssue.setEndTime(DataUtils.formatTimeStampToYMD(master.getEndTime()));
//            couponIssue.setMaxCount(master.getMaxCount() == null ? "" : master.getMaxCount().toString());
//            couponIssue.setValidDays(master.getValidDays() == null ? "" : master.getValidDays().toString());
//            couponIssue.setDistributedCount(master.getDistributedCount() == null ? "" : master.getDistributedCount().toString());
//            couponIssue.setComment(master.getComment());
//
//            //商品绑定
//            List<CouponGoods> list = couponGoodsMapperExt.selectByCouponIdS(master.getCouponId());
//            List<CouponSku> skuList = new ArrayList<CouponSku>();
//            //没有商品绑定,继续
//            if (list != null || list.size() > 0) {
//                for (CouponGoods goods : list) {
//                    CouponSku sku = new CouponSku();
//                    sku.setCouponId(goods.getCouponId());
//                    String goodsType = goods.getGoodsType();
//                    //GoodsId
//                    String goodsId = goods.getGoodsId();
//                    if (goodsType.equals("10")) {
//                        //全部
//                    } else if (goodsType.equals("20")) {
//                        //商品分类
//                        GdsType type = gdsTypeMapper.selectByPrimaryKey(goodsId);
//                        if (type == null || !type.getType().equals("10")) continue;
//                        goodsId = type.getGoodsTypeNameCn();
//                    } else if (goodsType.equals("30")) {
//                        //品牌
//                        GdsBrand brand = gdsBrandMapper.selectByPrimaryKey(goodsId);
//                        if (brand == null || !brand.getType().equals("10")) continue;
//                    } else if (goodsType.equals("40")) {
//                        //商品
//                        GdsMaster m = gdsMasterMapperExt.getErpGoodsCodeByGoodsId(goodsId);
//                        if (m == null || StringUtil.isEmpty(m.getErpGoodsCode())) continue;
//                        goodsId = m.getErpGoodsCode();
//                    } else if (goodsType.equals("50")) {
//                        //SKU
//                        if (StringUtil.isEmpty(goods.getSkuId())) continue;
//                    } else {
//                        continue;
//                    }
//                    sku.setGoodsType(goodsType);
//                    sku.setGoodsId(goodsId);
//                    sku.setSkuId(goods.getSkuId());
//                    skuList.add(sku);
//                }
//            }
//            Coupon coupon = new Coupon();
//            coupon.setCouponIssue(couponIssue);
//            coupon.setCouponSku(new ArrayOfCouponSku());
//            coupon.getCouponSku().setCouponSku(skuList);
//
//            String result = soap.couponissueUpdate(getKeyCouponissueUpdate(coupon), coupon);
//            log.debug("ERP couponissueUpdate result code:" + result + ",param:" + JSON.toJSONString(coupon));
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
//            couponMaster.setErpSendStatus("20");
//        } finally {
//            couponMasterMapperExt.updateSendById(couponMaster);
//        }
//    }
    public JSONObject getSendList() {
        JSONObject json = new JSONObject();
        try {
            //数据库检索可补发的优惠券
            List<CouponMasterExt> list = couponMasterMapperExt.getSendList();
            json.put("data", list);
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


    public JSONObject setSort(CouponMaster form) {
        JSONObject json = new JSONObject();
        try {
            //数据库检索 -- 过滤数据
            int count = couponMasterMapper.updateByPrimaryKeySelective(form);
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
    public JSONObject setValid(CouponMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getCouponId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            CouponMaster record = new CouponMaster();
            record.setCouponId(form.getCouponId());
            record.setIsValid("1");
            couponMasterMapper.updateByPrimaryKeySelective(record);
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
    public JSONObject setInvalid(CouponMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getCouponId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            CouponMaster record = new CouponMaster();
            record.setCouponId(form.getCouponId());
            record.setIsValid("0");
            couponMasterMapper.updateByPrimaryKeySelective(record);
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

    public JSONObject getSkuListById(String id) {
        JSONObject json = new JSONObject();
        try {
            //数据库检索
            List<ErpGoods> list = erpGoodsMapperExt.getCouponSkuListById(id);
            json.put("data", list);
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

    /**
     * 批量更新活动SKU信息
     *
     * @param loginId
     * @param couponId
     * @param skuList
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject importSkuList(String loginId, String couponId, List<String> skuList) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(couponId)) {
                throw new ExceptionErrorParam("缺少参数");
            }

            // 优惠券检查
            CouponMaster master = couponMasterMapper.selectByPrimaryKey(couponId);
            if (master == null || Constants.DELETED_YES.equals(master.getDeleted())) {
                throw new ExceptionErrorData("优惠券不存在");
            }
            if (!ComCode.ActivityGoodsType.SKU.equals(master.getGoodsType())) {
                throw new ExceptionErrorData("优惠券商品类型不正确");
            }

            if (!ComCode.ApproveStatus.APPROVE_STATUS_APPROVING.equals(master.getApproveStatus())
                    && !ComCode.ApproveStatus.APPROVE_STATUS_NOT_APPLY.equals(master.getApproveStatus())) {
                throw new ExceptionErrorData("优惠券状态不正确");
            }

            // 更新优惠券主表
//            Date date = new Date();
//            master.setUpdateUserId(loginId);
//            master.setUpdateTime(date);
//            couponMasterMapper.updateByPrimaryKeySelective(master);

            // 处理优惠券商品表
            CouponGoods couponGoods = new CouponGoods();
            couponGoods.setCouponId(couponId);
            couponGoods.setShopId(master.getShopId());
            couponGoods.setGoodsType(ComCode.ActivityGoodsType.SKU);
            couponGoods.setInsertUserId(loginId);
            couponGoods.setUpdateUserId(loginId);
            // 删除原数据
            CouponGoods del = new CouponGoods();
            del.setCouponId(couponId);
            couponGoodsMapperExt.deleteByCouponId(del);
            for (String sku : skuList) {
                // 商品已存在
//                if (StringUtil.isEmpty(sku)) continue;
//                CouponGoods goods = couponGoodsMapper.selectByPrimaryKey(sku);
//                if (goods != null) continue;
                couponGoods.setCouponGoodsId(UUID.randomUUID().toString());
                couponGoods.setSkuId(sku);
                // 取得商品ID
                String goodsId = gdsMasterMapperExt.getIdBySku(sku);
                if (StringUtil.isEmpty(goodsId)) continue;
                couponGoods.setGoodsId(goodsId);
                couponGoodsMapper.insertSelective(couponGoods);
            }
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

    /**
     * 添加活动相关商品
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject addGoods(CouponMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getCouponId())) {
                throw new ExceptionBusiness("缺少参数");
            }
            if (StringUtil.isEmpty(form.getGoodsType())) {
                throw new ExceptionBusiness("缺少参数");
            }

            if (form.getGoodsList() == null || form.getGoodsList().size() == 0) {
                throw new ExceptionBusiness("缺少参数");
            }
            for (int i = 0; i < form.getGoodsList().size(); i++) {
                CouponGoods goods = new CouponGoods();
                goods.setGoodsId(form.getGoodsList().get(i).getGoodsId());
                goods.setInsertUserId(form.getInsertUserId());
                goods.setShopId(Constants.ORGID);
                goods.setInsertTime(new Date());
                goods.setCouponId(form.getCouponId());
                goods.setGoodsType(form.getGoodsType());
                goods.setDeleted(Constants.DELETED_NO);
                goods.setSkuId(form.getGoodsList().get(i).getSkuId());
                goods.setCouponGoodsId(UUID.randomUUID().toString());
                couponGoodsMapper.insertSelective(goods);
            }
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

    /**
     * 删除活动相关商品
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject deleteGoods(CouponMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            if (form.getGoodsList() == null || form.getGoodsList().size() == 0) {
                throw new ExceptionBusiness("缺少参数");
            }
            for (int i = 0; i < form.getGoodsList().size(); i++) {
                couponGoodsMapper.deleteByPrimaryKey(form.getGoodsList().get(i).getCouponGoodsId());
            }
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
    public JSONObject getGoodsList(CouponMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getCouponId())) {
                throw new ExceptionBusiness("缺少参数");
            }
            if (StringUtil.isEmpty(form.getGoodsType())) {
                throw new ExceptionBusiness("缺少参数");
            }
            List<CouponGoodsForm> goodsList = null;
            CouponGoods goods = new CouponGoods();
            goods.setCouponId(form.getCouponId());
            if (ComCode.ActivityGoodsType.ALL.equals(form.getGoodsType())) {
                //全部商品

            } else if (ComCode.ActivityGoodsType.TYPE.equals(form.getGoodsType())) {
                // 按分类
                goodsList = couponGoodsMapperExt.selectGoodsTypeByCouponId(goods);
            } else if (ComCode.ActivityGoodsType.BRAND.equals(form.getGoodsType())) {
                // 按品牌
                goodsList = couponGoodsMapperExt.selectGoodsBrandByCouponId(goods);
            } else if (ComCode.ActivityGoodsType.GOODS.equals(form.getGoodsType())) {
                // 按商品
                goodsList = couponGoodsMapperExt.selectGoodsByCouponId(goods);
            } else if (ComCode.ActivityGoodsType.SKU.equals(form.getGoodsType())) {
                // 按SKU
                goodsList = couponGoodsMapperExt.selectSkuByCouponId(goods);
            }

            json.put("resultCode", Constants.NORMAL);
            json.put("result", goodsList);
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
