package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.util.StringUtils;
import net.dlyt.qyds.common.dto.ComSetting;
import net.dlyt.qyds.common.dto.MmbMaster;
import net.dlyt.qyds.common.dto.OrdHistory;
import net.dlyt.qyds.common.dto.OrdMaster;
import net.dlyt.qyds.common.dto.ext.CouponMemberExt;
import net.dlyt.qyds.common.form.MmbLevelManagerForm;
import net.dlyt.qyds.common.form.MmbPointRecordForm;
import net.dlyt.qyds.dao.ComSettingMapper;
import net.dlyt.qyds.dao.MmbMasterMapper;
import net.dlyt.qyds.dao.OrdMasterMapper;
import net.dlyt.qyds.dao.ext.MmbLevelRuleMapperExt;
import net.dlyt.qyds.dao.ext.MmbMasterMapperExt;
import net.dlyt.qyds.dao.ext.OrdHistoryMapperExt;
import net.dlyt.qyds.dao.ext.OrdMasterMapperExt;
import net.dlyt.qyds.web.service.*;
import net.dlyt.qyds.web.service.common.ComCode;
import net.dlyt.qyds.web.service.common.ErpSendUtil;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by wenxuechao on 16/8/20.
 */
@Service("wechatPayNotfyService")
public class WechatPayNotfyServiceImpl implements WechatPayNotfyService {

    private static final org.slf4j.Logger Log = LoggerFactory.getLogger(WechatPayNotfyServiceImpl.class);

    @Autowired
    private OrdMasterMapperExt ordMasterMapperExt;

    @Autowired
    private OrdHistoryMapperExt ordHistoryMapperExt;

    @Autowired
    private MmbPointRecordService mmbPointRecordService;

    @Autowired
    private BnkMasterService bnkMasterService;

    @Autowired
    private ActMasterService actMasterService;

    @Autowired
    private CouponMemberService couponMemberService;

    @Autowired
    private OrdMasterMapper ordMasterMapper;

    @Autowired
    private ComSettingMapper comSettingMapper;

    @Autowired
    private PrizeDrawService prizeDrawService;


    @Autowired
    private MmbMasterMapper mmbMasterMapper;

    @Autowired
    private MmbMasterMapperExt mmbMasterMapperExt;

    @Autowired
    private MmbLevelRuleMapperExt mmbLevelRuleMapperExt;


    @Override
    public void paySuccess(Map<String, String> mParam) {
        // 订单编号
        String strOrderCode = mParam.get("orderCode");
        // 支付总价
        String payTotal = mParam.get("total_fee");//单位‘分’
        //获取订单信息
        OrdMaster ordMaster = ordMasterMapperExt.getOrderInfoByCode(strOrderCode);
        //替换用户
        if (!ordMaster.getUpdateUserId().equals(ordMaster.getInsertUserId())) {
            ordMaster.setMemberId(ordMaster.getUpdateUserId());
            ordMasterMapper.updateByPrimaryKeySelective(ordMaster);
        }
        Log.debug("OrderCode = " + ordMaster.getOrderCode() + "== PayStatus =" + ordMaster.getPayStatus());
        System.out.println("OrderCode = " + ordMaster.getOrderCode() + "== PayStatus =" + ordMaster.getPayStatus());
        if ("20".equals(ordMaster.getPayStatus())) {
            Log.debug("PaySuccess return && OrderCode =  " + ordMaster.getOrderCode());
            System.out.println("PaySuccess return && OrderCode =  " + ordMaster.getOrderCode());
            return;

        }
        //金额校验
        if (ordMaster.getPayInfact().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_FLOOR).intValue() > Integer.parseInt(payTotal)) {
            return;
        }

        //更新订单付款状态为付款成功 20
        ordMaster.setPayStatus("20");
        ordMaster.setPayType("20");
        //更新订单数据
        ordMasterMapperExt.paySuccess(ordMaster);
        //更新订单历史表
        OrdHistory ordHistory = new OrdHistory();
        BeanUtils.copyProperties(ordMaster, ordHistory);
        ordHistory.setSeqOrderId(UUID.randomUUID().toString());
        ordHistory.setInsertUserId(ordHistory.getMemberId());
        //主订单操作历史信息插入
        ordHistoryMapperExt.insertSelective(ordHistory);
        //会员自动升级
        MmbMaster master = mmbMasterMapper.selectByPrimaryKey(ordMaster.getMemberId());
        if (null != master && "0".equals(master.getDeleted())) {
            if (!StringUtil.isEmpty(master.getTelephone()) &&
                    !StringUtil.isEmpty(master.getMemberName()) &&
                    !master.getMemberName().contains("先生")&&
                    !master.getMemberName().contains("女士")&&
                    !master.getMemberName().contains("小姐")&&
                    !StringUtil.isEmpty(master.getSex()) &&
                    !StringUtil.isEmpty(String.valueOf(master.getBirthdate())) &&
                    !StringUtil.isEmpty(master.getAddress()) &&
                    !StringUtil.isEmpty(master.getProvinceCode()) &&
                    !StringUtil.isEmpty(master.getCityCode()) &&
                    !StringUtil.isEmpty(master.getDistrictCode())
                    ) {
                MmbMaster masterForApproval = mmbMasterMapper.selectByPrimaryKey(ordMaster.getMemberId());
                MmbLevelManagerForm form = new MmbLevelManagerForm();
                form.setTelephone(masterForApproval.getTelephone());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss     ");
                String dDate = "01-15";
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String str = formatter.format(curDate);
                if (StringUtils.isNotBlank(str) && StringUtils.isNotBlank(str.substring(5, 10))) {
                    int result = str.substring(5, 10).compareTo(dDate);
                    if (result < 0) {
                        System.out.println("小于");
                        // 查询当年和前一年的数据
                        form.setYearNum("1");
                    } else {
                        System.out.println("大于等于");
                        // 查询当年的数据
                        form.setYearNum("0");
                    }
                    ;
                }
                List<MmbLevelManagerForm> list1 = mmbLevelRuleMapperExt.selectApprovalUpMemberListInTwoForAuto(form);
                if (list1 != null && list1.size() != 0) {
                    masterForApproval.setMemberLevelId("30");
                    masterForApproval.setUpdateTime(new Date());

                    mmbMasterMapper.updateByPrimaryKeySelective(masterForApproval);

                    //erp接口调用
                    ErpSendUtil.VIPUpdateById(masterForApproval.getMemberId(), mmbMasterMapperExt, mmbMasterMapper);
                }
            }
        }

        //付款增加积分信息
        MmbPointRecordForm mmbPointRecordForm = new MmbPointRecordForm();
        mmbPointRecordForm.setMemberId(ordMaster.getMemberId());
        //积分规则 80:退货
        mmbPointRecordForm.setRuleId("10");
        //实际现金金额
        mmbPointRecordForm.setCash(ordMaster.getPayInfact());
        //订单ID
        mmbPointRecordForm.setScoreSource(ordMaster.getOrderId());
        // 需要扣除的积分
        if (ordMaster.getExchangePointCount() != null && ordMaster.getExchangePointCount() > 0) {
            mmbPointRecordForm.setExchangeId("60");
            mmbPointRecordForm.setExchangePoint(ordMaster.getExchangePointCount());
        }
        //积分处理
        mmbPointRecordService.add(mmbPointRecordForm);

        // 在此处追加满送积分
        try {
            if(!StringUtil.isEmpty(ordMaster.getActionId())){
                String points = actMasterService.getSendPointsByActivity(ordMaster.getActionId());
                if(!StringUtil.isEmpty(points)){
                    //付款增加积分信息
                    mmbPointRecordForm = new MmbPointRecordForm();
                    mmbPointRecordForm.setMemberId(ordMaster.getMemberId());
                    //积分规则 20:
                    mmbPointRecordForm.setRuleId("20");
                    //实际现金金额
                    mmbPointRecordForm.setCash(BigDecimal.ZERO);
                    //订单ID
                    mmbPointRecordForm.setScoreSource(ordMaster.getOrderId());
                    //赠送积分
                    mmbPointRecordForm.setPresentPoint(Integer.parseInt(points));
                    //积分处理
                    mmbPointRecordService.add(mmbPointRecordForm);
                }
            }
        } catch (Exception e) {
            System.out.println("满送积分获取失败,原因:"+e.getMessage());
        }

        // 判断是否有满送的优惠券
        try {
            if (!StringUtil.isEmpty(ordMaster.getActionId())) {
                String couponId = actMasterService.getSendCouponIdByActivity(ordMaster.getActionId());
                if (!StringUtil.isEmpty(couponId)) {
                    CouponMemberExt couponMember = new CouponMemberExt();
                    couponMember.setMemberId(ordMaster.getMemberId());
                    couponMember.setCouponId(couponId);
                    JSONObject json = couponMemberService.addCouponsForUser(couponMember);
                    if (!"00".equals(json.getString("resultCode"))) {
                        throw new ExceptionBusiness(json.getString("resultMessage"));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("发放优惠券失败,原因:" + e.getMessage());
        }
        // 判断该用户是不是首次购买
        try {
            // 获取首次购买的赠送的优惠劵
            OrdMaster ordMaster2 = new OrdMaster();
            ordMaster2.setPayStatus("20");
            ordMaster2.setMemberId(ordMaster.getMemberId());
            int payCount = ordMasterMapperExt.getOrderCount(ordMaster2);
            if (payCount == 1) {
                // 是一次购买,则这个订单活动是无效的，过滤掉
                // 获取第一次购买后的赠送的优惠劵
                // 获取首次购买的活动
                String firstBuyCouponId = null;
                ComSetting comSetting = comSettingMapper.selectByPrimaryKey("QYDS");
                if (comSetting != null) {
                    firstBuyCouponId = comSetting.getFirstBuyCoupon();
                }
                if (!StringUtil.isEmpty(firstBuyCouponId)) {
                    CouponMemberExt couponMember = new CouponMemberExt();
                    couponMember.setMemberId(ordMaster.getMemberId());
                    couponMember.setCouponId(firstBuyCouponId);
                    JSONObject json = couponMemberService.addCouponsForUser(couponMember);
                    if (!"00".equals(json.getString("resultCode"))) {
                        throw new ExceptionBusiness(json.getString("resultMessage"));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("发放优惠券失败,原因:" + e.getMessage());
        }

        // 发放抽奖机会
        try{
            prizeDrawService.addPrizeDrawOppo(ordMaster.getMemberId(), null, ComCode.PrizeDrawOppoType.ORDER, ordMaster.getPayInfact());
        } catch (Exception e) {
            System.out.println("发放抽奖活动失败,原因:" + e.getMessage());
        }
    }
}
