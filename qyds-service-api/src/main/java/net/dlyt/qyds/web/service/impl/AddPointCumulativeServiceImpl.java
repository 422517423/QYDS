package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbMaster;
import net.dlyt.qyds.common.dto.MmbPointRecord;
import net.dlyt.qyds.common.dto.OrdMaster;
import net.dlyt.qyds.common.dto.ext.MmbPointRecordExt;
import net.dlyt.qyds.common.form.MmbLevelManagerForm;
import net.dlyt.qyds.common.form.MmbPointRecordForm;
import net.dlyt.qyds.dao.MmbMasterMapper;
import net.dlyt.qyds.dao.ext.MmbLevelRuleMapperExt;
import net.dlyt.qyds.dao.ext.MmbMasterMapperExt;
import net.dlyt.qyds.dao.ext.MmbPointRecordMapperExt;
import net.dlyt.qyds.dao.ext.OrdMasterMapperExt;
import net.dlyt.qyds.web.service.AddPointCumulativeService;
import net.dlyt.qyds.web.service.MmbPointRecordService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.ErpSendUtil;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("addToPoint")
public class AddPointCumulativeServiceImpl implements AddPointCumulativeService {
    @Autowired
    MmbMasterMapper mmbMasterMapper;

    @Autowired
    MmbPointRecordMapperExt mmbPointRecordMapperExt;

    @Autowired
    MmbPointRecordService mmbPointRecordService;

    @Autowired
    OrdMasterMapperExt ordMasterMapperExt;

    @Autowired
    MmbLevelRuleMapperExt mmbLevelRuleMapperExt;

    @Autowired
    MmbMasterMapperExt mmbMasterMapperExt;

    @Override
    public void addToPoint() {
        //将所有会员的积分放入到累计消费中
        List<MmbMaster> mmbMasters = mmbMasterMapper.selectAllMaster();
        System.out.println("-----------------------START-------------------------");
        for (MmbMaster mmbMaster:mmbMasters) {
            if (mmbMaster!=null){
                //获取这个会员的两年内的累计消费
                BigDecimal allPoint = mmbMasterMapper.selectAllPoint(mmbMaster.getMemberId());
                if (allPoint!=null){
                    mmbMaster.setPointCumulative(allPoint.intValue());
                }else {
                    mmbMaster.setPointCumulative(0);
                }
                mmbMasterMapper.updateByPrimaryKeySelective(mmbMaster);
                System.out.println("累计积分："+mmbMaster.getPointCumulative());
            }
        }
        System.out.println("-------------------------END--------------------------");
    }

    @Override
    public void addAllPoint() {
//        //1.查询2018年所有订单
//        List<OrdMaster> ordMasters = ordMasterMapperExt.selectThisYear();
//        //2.判断是否有积分履历
//        for (OrdMaster ordMaster:ordMasters) {
//            //1.如果没有：添加积分履历
//            List<MmbPointRecord> mmbPointRecords = mmbPointRecordMapperExt.selectByMemberIdYear(ordMaster.getMemberId());
//            if (mmbPointRecords==null||mmbPointRecords.size()==0){
//                //付款增加积分信息
//                MmbPointRecordForm mmbPointRecordForm = new MmbPointRecordForm();
//                mmbPointRecordForm.setMemberId(ordMaster.getMemberId());
//                //积分规则 80:退货
//                mmbPointRecordForm.setRuleId("10");
//                //实际现金金额
//                mmbPointRecordForm.setCash(ordMaster.getPayInfact());
//                //订单ID
//                mmbPointRecordForm.setScoreSource(ordMaster.getOrderId());
//                // 需要扣除的积分
//                if (ordMaster.getExchangePointCount() != null && ordMaster.getExchangePointCount() > 0) {
//                    mmbPointRecordForm.setExchangeId("60");
//                    mmbPointRecordForm.setExchangePoint(ordMaster.getExchangePointCount());
//                }
//                //积分处理
//                mmbPointRecordService.add(mmbPointRecordForm);
//            }
//        }
            JSONObject json = new JSONObject();
            try {
                Date now = new Date();
                List<MmbPointRecordExt> list = mmbPointRecordMapperExt.countSubPointOneTime(now);
                for (MmbPointRecordExt item : list) {
                    if (item.getPointSurplus() > 0 && !StringUtil.isEmpty(item.getMemberId())) {
                        // 更新会员主表信息
                        MmbMaster master = mmbMasterMapper.selectByPrimaryKey(item.getMemberId());
                        if (master != null && "0".equals(master.getDeleted())) {
                            master.setPoint(master.getPoint() - item.getPointSurplus());
                            master.setUpdateTime(new Date());
                            master.setPointCumulative(0);
                            mmbMasterMapper.updateByPrimaryKeySelective(master);
                        }
                    }
                }
                // 清空所有会员的到期积分
                mmbPointRecordMapperExt.clearSubPointOneTime(now);
                //2016.11.23,确认ERP自己处理清除过期积分
//            for (MmbPointRecordExt item : list) {
//                if (item.getPointSurplus() > 0 && !StringUtil.isEmpty(item.getMemberId())) {
//                    // 更新会员主表信息
//                    MmbMaster master = mmbMasterMapper.selectByPrimaryKey(item.getMemberId());
//                    if (master != null && "0".equals(master.getDeleted())) {
//                        //调用ERP的积分接口
////                        clearPointRecord(item);
//                        ErpSendUtil.VIPPointUpdate(item,mmbPointRecordMapper,mmbMasterMapper);
//                    }
//                }
//            }
                List<MmbLevelManagerForm> list1 = mmbLevelRuleMapperExt.selectRelegationMemberListOneTime();
                for (MmbLevelManagerForm item1 : list1) {
                    MmbMaster master = mmbMasterMapper.selectByPrimaryKey(item1.getMemberId());
                    if (null != master || "0".equals(master.getDeleted())) {
                        master.setMemberLevelId("10");
                        master.setUpdateTime(new Date());
                        mmbMasterMapper.updateByPrimaryKeySelective(master);

                        // erp接口调用会员降级
//                    updateMasterToERP(master.getMemberId());
//                    ErpSendUtil.getInstance().VIPUpdateById(master.getMemberId());
                        ErpSendUtil.VIPUpdateById(master.getMemberId(),mmbMasterMapperExt,mmbMasterMapper);
                    }
                }


            } catch (ExceptionBusiness e) {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", e.getMessage());
            } catch (Exception e) {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", Constants.FAIL_MESSAGE);
            }


    }
}
