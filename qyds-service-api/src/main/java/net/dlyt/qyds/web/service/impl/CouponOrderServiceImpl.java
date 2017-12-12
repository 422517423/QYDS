package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.CouponMaster;
import net.dlyt.qyds.common.dto.CouponMember;
import net.dlyt.qyds.common.dto.CouponOrder;
import net.dlyt.qyds.common.dto.MmbMaster;
import net.dlyt.qyds.common.dto.ext.CouponOrderExt;
import net.dlyt.qyds.common.form.MmbPointRecordForm;
import net.dlyt.qyds.dao.CouponMasterMapper;
import net.dlyt.qyds.dao.CouponMemberMapper;
import net.dlyt.qyds.dao.CouponOrderMapper;
import net.dlyt.qyds.dao.MmbMasterMapper;
import net.dlyt.qyds.web.service.CouponMemberService;
import net.dlyt.qyds.web.service.CouponOrderService;
import net.dlyt.qyds.web.service.MmbPointRecordService;
import net.dlyt.qyds.web.service.common.ComCode;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.ErpSendUtil;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by cjk on 2016/12/13.
 */
@Service("couponOrderService")
public class CouponOrderServiceImpl implements CouponOrderService {

    @Autowired
    private CouponOrderMapper couponOrderMapper = null;
    @Autowired
    private CouponMasterMapper couponMasterMapper = null;
    @Autowired
    private CouponMemberMapper couponMemberMapper = null;
    @Autowired
    private MmbPointRecordService mmbPointRecordService = null;
    @Autowired
    private MmbMasterMapper mmbMasterMapper = null;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject paySuccess(CouponOrderExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getOrderId())) {
                throw new ExceptionErrorParam("缺少参数:OrderId");
            }
            if (StringUtil.isEmpty(form.getPayType())) {
                throw new ExceptionErrorParam("缺少参数:PayType");
            }
            if (form.getPayInfact() == null) {
                throw new ExceptionErrorParam("缺少参数:PayInfact");
            }
            // 根据OrderID获取订单
            CouponOrder couponOrder = couponOrderMapper.selectByPrimaryKey(form.getOrderId());
            if (couponOrder == null) {
                throw new ExceptionErrorParam("订单不存在");
            }
            //检查支付状态
            if ("20".equals(couponOrder.getPayStatus())) {
                throw new ExceptionErrorParam("已经支付过了");
            }
            // 10:未支付，20：已支付
            couponOrder.setPayStatus("20");
            couponOrder.setPayTime(new Date());
            if ("20".equals(form.getPayType())) {
                // 微信支付单位为:分
                couponOrder.setPayInfact(form.getPayInfact().divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP));
            } else {
                couponOrder.setPayInfact(form.getPayInfact().setScale(2, RoundingMode.HALF_UP));
            }
            couponOrder.setUpdateTime(new Date());
            couponOrder.setUpdateUserId("SYSTEM");
            couponOrderMapper.updateByPrimaryKeySelective(couponOrder);
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

    /**
     * 发送现金购买的优惠劵
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject sendCoupon(CouponOrderExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getOrderId())) {
                throw new ExceptionErrorParam("缺少参数:OrderId");
            }
            // 根据OrderID获取订单
            CouponOrder couponOrder = couponOrderMapper.selectByPrimaryKey(form.getOrderId());
            if (couponOrder == null) {
                throw new ExceptionErrorParam("订单不存在");
            }
            //检查支付状态
            if (!"10".equals(couponOrder.getDeliverStatus())) {
                throw new ExceptionErrorParam("已经发过了");
            }
            // 10:未发送，20：已发送
            couponOrder.setDeliverStatus("20");
            couponOrder.setDeliverTime(new Date());
            couponOrder.setUpdateTime(new Date());
            couponOrder.setUpdateUserId("SYSTEM");
            couponOrderMapper.updateByPrimaryKeySelective(couponOrder);
            //发优惠券
            doSendCoupon(couponOrder.getMemberId(), couponOrder.getCouponId());
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

    private void doSendCoupon(String memberId, String couponId) {
        // 获取优惠劵主表
        CouponMaster coupon = couponMasterMapper.selectByPrimaryKey(couponId);
        CouponMember cm = new CouponMember();
        cm.setMemberId(memberId);
        cm.setCouponId(couponId);
        cm.setCouponMemberId(UUID.randomUUID().toString());
        cm.setDeleted(Constants.DELETED_NO);
        cm.setInsertTime(new Date());
        cm.setStatus(ComCode.CouponStatus.UNUSE);
        cm.setSendTime(new Date());

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
        couponMemberMapper.insertSelective(cm);
        // 更新领取个数
        coupon.setDistributedCount(coupon.getDistributedCount() + 1);
        couponMasterMapper.updateByPrimaryKeySelective(coupon);
        if (coupon.getExchangePoint() != null && coupon.getExchangePoint() > 0) {
            // 需要积分,调用扣除积分的接口支付成功
            //付款增加积分信息
            MmbPointRecordForm mmbPointRecordForm = new MmbPointRecordForm();
            mmbPointRecordForm.setMemberId(memberId);
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
        if (coupon.getCouponScope().equals("10")) {
            ErpSendUtil.CouponSendUpdateById(cm.getCouponMemberId(), couponMemberMapper, mmbMasterMapper);
        }
    }
}
