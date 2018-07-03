package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ComCode;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.dao.*;
import net.dlyt.qyds.dao.ext.*;
import net.dlyt.qyds.web.service.ErpCouponService;
import net.dlyt.qyds.web.service.common.*;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Created by zlh on 2016/9/4.
 */
@Service("erpCouponService")
@Transactional(readOnly = true)
public class ErpCouponServiceImpl implements ErpCouponService {

    @Resource
    private CouponMasterMapper couponMasterMapper;

    @Resource
    private CouponMasterMapperExt couponMasterMapperExt;

    @Resource
    private CouponGoodsMapper couponGoodsMapper;

    @Resource
    private CouponMemberMapper couponMemberMapper;

    @Resource
    private CouponMemberMapperExt couponMemberMapperExt;

   @Resource
    private GdsMasterMapperExt gdsMasterMapperExt;

    @Resource
    private MmbMasterMapperExt mmbMasterMapperExt;

    @Autowired
    private GdsBrandMapper gdsBrandMapper;

    @Autowired
    private GdsTypeMapperExt gdsTypeMapperExt;

    /**
     * 接受ERP的优惠券
     *
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject receiveCoupon(String data) {
        JSONObject map = new JSONObject();
        try{
            CouponMaster record = JSON.parseObject(data, CouponMaster.class);
            if (StringUtil.isEmpty(record.getCouponName())) {
                throw new ExceptionErrorParam("缺少参数优惠券名称");
            }
            if (StringUtil.isEmpty(record.getCouponStyle())) {
                throw new ExceptionErrorParam("缺少参数优惠方式");
            }
            if (StringUtil.isEmpty(record.getCouponType())) {
                throw new ExceptionErrorParam("缺少参数优惠券类型");
            }
            //金额
            if (record.getCouponStyle().equals("10")) {
                if(record.getWorth()==null || record.getWorth()==0){
                    throw new ExceptionErrorParam("缺少参数优惠金额");
                }
            }
            //折扣
            if (record.getCouponStyle().equals("20")) {
                if(record.getDiscount()==null || record.getDiscount().equals(0)){
                    throw new ExceptionErrorParam("缺少参数折扣率");
                }
                if(record.getDiscount().compareTo(new BigDecimal(0.1))<0
                        || record.getDiscount().compareTo(new BigDecimal(9.9))>0){
                    throw new ExceptionErrorParam("参数折扣率不正确(0.1-9.9)");
                }
            }
            if (!net.dlyt.qyds.web.service.common.ComCode.CouponType.BIRTHDAY_SEND.equals(record.getCouponType())
                    && !net.dlyt.qyds.web.service.common.ComCode.CouponType.REGIST_SEND.equals(record.getCouponType())) {
                if (record.getStartTime()==null) {
                    throw new ExceptionErrorParam("缺少参数开始时间");
                }
                if (record.getEndTime()==null) {
                    throw new ExceptionErrorParam("缺少参数结束时间");
                }
            }
            if (record.getSendStartTime()==null) {
                throw new ExceptionErrorParam("缺少参数发放开始时间");
            }
            if (record.getSendEndTime()==null) {
                throw new ExceptionErrorParam("缺少参数结束时间");
            }
            // 判断优惠券名称是否重复
            CouponMaster form1 = new CouponMaster();
            form1.setCouponName(record.getCouponName());
            int count = couponMasterMapperExt.checkExistByCouponName(form1);
            if (count > 0) {
                throw new ExceptionErrorData("优惠券名称重复");
            }
//            record.setCouponId(UUID.randomUUID().toString());
            record.setShopId("00000000");
//            record.setCouponType("10");//代金券10代金券,20生日券,30注册送券
            record.setCouponScope("10");//通用
            record.setApproveStatus("20");//审批通过
            record.setDistributedCount(0);
            record.setInsertUserId("ERP");
            record.setUpdateUserId("ERP");
            couponMasterMapper.insertSelective(record);
            map.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 接受ERP的优惠券绑定SKU
     *
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject receiveCouponSku(String data) {
        JSONObject map = new JSONObject();
        try{
            List<CouponGoods> list = JSON.parseArray(data,CouponGoods.class);
            for (CouponGoods record : list) {
//                if (StringUtil.isEmpty(record.getCouponGoodsId())) throw new ExceptionErrorData("CouponGoodsId未指定");
                record.setCouponGoodsId(UUID.randomUUID().toString());
                record.setShopId("00000000");
                //goodsType,10.全部,20.商品分类,30.品牌,40.商品,50.SKU
                String goodsType = record.getGoodsType();
                if (StringUtil.isEmpty(goodsType)) throw new ExceptionErrorData("商品分类未指定");
                //全部
                if (goodsType.equals("10")) {
                } else if (goodsType.equals("20") || goodsType.equals("30") || goodsType.equals("40")) {
                    String goodsId = record.getGoodsId();
                    if (StringUtil.isEmpty(goodsId)) throw new ExceptionErrorData("商品ID未指定");
                    //商品分类
                    if (goodsType.equals("20")) {
                        GdsType type = gdsTypeMapperExt.selectErpByName(goodsId);
                        if (type == null) {
                            throw new ExceptionErrorData(goodsId + "商品分类不存在");
                        }
                        record.setGoodsId(type.getGoodsTypeId());
                    }
                    //品牌
                    if (goodsType.equals("30")) {
                        GdsBrand brand = gdsBrandMapper.selectByPrimaryKey(goodsId);
                        if(brand == null || !brand.getType().equals("10")) {
                            throw new ExceptionErrorData(goodsId + "商品品牌不存在");
                        }
                    }
                    //商品
                    if (goodsType.equals("40")) {
                        GdsMaster goods = gdsMasterMapperExt.selectErpByCode(goodsId);
                        //取不到goodId
                        if (goods == null) {
                            throw new ExceptionErrorData(goodsId + "商品CODE未维护或者不存在");
                        }
                        record.setGoodsId(goods.getGoodsId());
                    }
                } else if (goodsType.equals("50")) {
                    //SKU
                    if (StringUtil.isEmpty(record.getSkuId())) throw new ExceptionErrorData("SKU未指定");
                } else {
                    //错误分类
                    throw new ExceptionErrorData("商品分类指定错误");
                }
                record.setInsertUserId("ERP");
                record.setUpdateUserId("ERP");
                couponGoodsMapper.insertSelective(record);
            }
            map.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 接受ERP的会员优惠券
     *
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject receiveCouponMember(String data) {
        JSONObject map = new JSONObject();
        try{
            List<CouponMember> list = JSON.parseArray(data, CouponMember.class);
            for (CouponMember record : list) {
                record.setShopId("00000000");
                //memberId
                record.setMemberId(getMemberIdByCode(record.getMemberId()));
                record.setStatus("10");
                record.setInsertUserId("ERP");
                record.setUpdateUserId("ERP");
                couponMemberMapper.insertSelective(record);
                //更新已发行数量
                couponMasterMapperExt.addOneDistributedById(record.getCouponId());
            }
            map.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 接受ERP的已使用优惠券
     *
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject receiveCouponUsed(String data) {
        JSONObject map = new JSONObject();
        try{
            List<CouponMember> list = JSON.parseArray(data, CouponMember.class);
            for (CouponMember record : list) {
                //memberId
//                record.setStatus("20");
//                record.setCouponId(null);
//                record.setMemberId(getMemberIdByCode(record.getMemberId()));
//                record.setMemberId(null);
//                record.setOrderId(null);
                record.setUpdateUserId("ERP");
                int count = couponMemberMapperExt.updateStatusUsedById(record);
                if (count == 0) throw new ExceptionErrorData(record.getCouponMemberId()+"优惠券会员ID已使用过或者不正确");
            }
            map.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    private String getMemberIdByCode(String code) {
        if (StringUtil.isEmpty(code)) throw new ExceptionErrorData("会员代码未指定");
        MmbMasterExt user = new MmbMasterExt();
        user.setTelephone(code);
        MmbMaster member = mmbMasterMapperExt.selectBySelective(user);
        if (member == null) {
            throw new ExceptionErrorData(code + "会员不存在");
        }
        return member.getMemberId();
    }

    /**
     * 验证ERP的要使用优惠券
     *
     */
    public JSONObject checkCoupon(String data) {
        JSONObject map = new JSONObject();
        try{
            List<CouponMember> list = JSON.parseArray(data, CouponMember.class);
            if (list==null || list.size()==0) {
                map.put("resultCode", "10");
                map.put("message", "没有传数据");
                return map;
            }
            CouponMember record = list.get(0);
            String couponMemberId = record.getCouponMemberId();
            if (StringUtil.isEmpty(couponMemberId) ) {
                map.put("resultCode", "11");
                map.put("message", "优惠券ID未指定");
                return map;
            }
            String memberCode = record.getMemberId();
            if (StringUtil.isEmpty(memberCode)) {
                map.put("resultCode", "12");
                map.put("message", "会员ID未指定");
                return map;
            }
            CouponMember couponMember = couponMemberMapper.selectByPrimaryKey(couponMemberId);
            if (couponMember == null) {
                map.put("resultCode", "21");
                map.put("message", "指定优惠券不存在");
                return map;
            }
            String mmbId = getMemberIdByCode(memberCode);
            if (!couponMember.getMemberId().equals(mmbId)) {
                map.put("resultCode", "22");
                map.put("message", "指定优惠券不属于该会员");
                return map;
            }
            if (couponMember.getStatus().equals("20")) {
                map.put("resultCode", "23");
                map.put("message", "指定优惠券已使用");
                return map;
            }
            map.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }
}
