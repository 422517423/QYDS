package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.util.StringUtils;
import net.dlyt.qyds.common.dto.ComConfig;
import net.dlyt.qyds.common.dto.ComConfigKey;
import net.dlyt.qyds.common.dto.MmbMaster;
import net.dlyt.qyds.common.form.MmbLevelManagerForm;
import net.dlyt.qyds.dao.ComConfigMapper;
import net.dlyt.qyds.dao.MmbMasterMapper;
import net.dlyt.qyds.dao.ext.MmbLevelRuleMapperExt;
import net.dlyt.qyds.dao.ext.MmbMasterMapperExt;
import net.dlyt.qyds.web.service.MmbLevelManagerService;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by YiLian on 16/8/8.
 */
@Service("mmbLevelManagerService")
public class MmbLevelManagerServiceImpl implements MmbLevelManagerService {

    @Autowired
    private MmbLevelRuleMapperExt mmbLevelRuleMapperExt;

    @Autowired
    private MmbMasterMapper mmbMasterMapper;

    @Autowired
    private MmbMasterMapperExt mmbMasterMapperExt;

    @Autowired
    private ComConfigMapper comConfigMapper;

    protected final Logger log = LoggerFactory.getLogger(MmbLevelManagerServiceImpl.class);



    /**
     * 待升级审批会员一览
     *
     * @param form
     * @return
     */
    public JSONObject getList(MmbLevelManagerForm form) {
        JSONObject json = new JSONObject();
        try {

            // List<MmbLevelManagerForm> list = mmbLevelRuleMapperExt.selectApprovalUpMemberList(form);
            // int allCount = mmbLevelRuleMapperExt.countApprovalUpMemberList(form);
            // TODO: 2018/1/15 判断当前月日是否小于1月15，如果小于，则查询普通会员当年和前一年的累积消费和单笔消费；如果大于等于，则查询普通会员当年的累积消费和单笔消费
            SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd   HH:mm:ss     ");
            String dDate = "01-15";
            Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间
            String   str   =   formatter.format(curDate);
            if(StringUtils.isNotBlank(str) && StringUtils.isNotBlank(str.substring(5,10))){
                int result= str.substring(5,10).compareTo(dDate);
                if (result < 0 ) {
                    System.out.println("小于");
                    // 查询当年和前一年的数据
                    form.setYearNum("1");
                }else {
                    System.out.println("大于等于");
                    // 查询当年的数据
                    form.setYearNum("0");
                };
            }
            List<MmbLevelManagerForm> list = mmbLevelRuleMapperExt.selectApprovalUpMemberListInTwo(form);
            int allCount = mmbLevelRuleMapperExt.countApprovalUpMemberListInTwo(form);

            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
            json.put("aaData", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;

    }

    /**
     * 确认升级会员等级
     *
     * @param form
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject approval(MmbLevelManagerForm form, String userId) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }

            if (StringUtil.isEmpty(form.getApprovalLevelId())) {
                throw new ExceptionErrorParam("缺少参数新的会员等级");
            }

            MmbMaster master = mmbMasterMapper.selectByPrimaryKey(form.getMemberId());
            if (null == master || !"0".equals(master.getDeleted())) {
                throw new ExceptionErrorData("会员不存在");
            }

            master.setMemberLevelId(form.getApprovalLevelId());
            master.setUpdateTime(new Date());
            master.setUpdateUserId(userId);

            mmbMasterMapper.updateByPrimaryKeySelective(master);

            //erp接口调用
//            updateMasterToERP(master.getMemberId());
//            ErpSendUtil.getInstance().VIPUpdateById(master.getMemberId());
            ErpSendUtil.VIPUpdateById(master.getMemberId(),mmbMasterMapperExt,mmbMasterMapper);

            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }
//    private JSONObject updateMasterToERP(String memberId) {
//
//        JSONObject json = new JSONObject();
//
//        // 获取信息
//        MmbMaster user = mmbMasterMapper.selectByPrimaryKey(memberId);
//        if (user == null) {
//            json.put("resultCode", Constants.FAIL);
//            json.put("resultMessage", "用户信息不存在");
//        } else {
//            json.put("data", user);
//            json.put("resultCode", Constants.NORMAL);
//        }
//
//        MmbMaster master = new MmbMaster();
//        master.setMemberId(memberId);
//        master.setErpSendStatus("20");
//        try {
//            Vip vip = new Vip();
//
//            vip.setMemberCode(user.getTelephone());
//            vip.setMemberName(user.getMemberName());
//            // 1：男   2：女
//            String sexName = "";
//            if ("1".equals(user.getSex())) {
//                sexName = "男";
//            } else if ("2".equals(user.getSex())) {
//                sexName = "女";
//            }
//            vip.setSexName(sexName);
//            vip.setMobil(user.getTelephone());
//            vip.setBirthday(DataUtils.formatTimeStampToYMD(user.getBirthdate()));
//            vip.setProvinceName(user.getProvinceName());
//            vip.setCityName(user.getCityName());
//            vip.setDistrictName(user.getDistrictName());
//            vip.setEmail(user.getEmail());
////            vip.setStoreCode("");
////            vip.setStoreName("");
//            vip.setSellerName(user.getReferrerId());
//            vip.setAddress(user.getAddress());
//            vip.setPostCode(user.getPostCode());
//            vip.setIncome(user.getIncome());
//            vip.setRegistTime(DataUtils.formatTimeStampToYMD(user.getInsertTime()));
//            vip.setMemberGrade(user.getMemberLevelId());
////            vip.setMemberCodeAlter("");
//
//            BaseDate date = new BaseDate();
//            date.setVip(vip);
//            String result = soap.vipUpdate(getKeyVIPUpdate(date), date);
//            log.debug("ERP vipUpdate result code:"+result+",param:"+ JSON.toJSONString(date));
//            master.setUpdateTime(new Date());
//            if (result.equals("00")) {
//                master.setErpSendStatus("10");
//            }
//            mmbMasterMapper.updateByPrimaryKeySelective(master);
//            if (result.equals("00")) {
//            } else if (result.equals("11")) {
//                throw new ExceptionErrorData("ERP数据库连接失败");
//            } else if (result.equals("12")) {
//                throw new ExceptionErrorData("ERP数据库更新失败");
//            } else if (result.equals("21")) {
//                throw new ExceptionErrorData("ERP验证失败");
//            } else {
//                throw new ExceptionErrorData("ERP未知错误");
//            }
//        } catch (Exception e) {
//            System.out.println("用户信息同步ERP失败,原因:"+e.getMessage());
//        }
//
//        return json;
//    }


    private static final String MEMBER_LEVEL_RATIO_CODE = "MEMBER_LEVEL_RATIO";
    private static final String MEMBER_LEVEL_RATIO_TYPE = "0";

    /**
     * 会员等级降级
     * <p/>
     * ###计算上一自然年度会员消费累计积分是否达到对应级别的80%
     * 修改为
     * 计算上一自然年度会员累计消费金额是否大于等于3000或单笔最大消费是否大于等于1500
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject relegation() {
        JSONObject json = new JSONObject();
        try {


           /* String ratio = "0.8";

            ComConfigKey key = new ComConfigKey();
            key.setCode(MEMBER_LEVEL_RATIO_CODE);
            key.setType(MEMBER_LEVEL_RATIO_TYPE);
            ComConfig comConfig = comConfigMapper.selectByPrimaryKey(key);

            if (comConfig != null && !StringUtil.isEmpty(comConfig.getParam())) {
                ratio = comConfig.getParam();
            }

            List<MmbLevelManagerForm> list = mmbLevelRuleMapperExt.selectRelegationMemberList(Float.valueOf(ratio));

            for (MmbLevelManagerForm item : list) {

                if(item.getApprovalLevelId().equals(item.getCurrentLevelId())){
                    continue;
                }
                
                MmbMaster master = mmbMasterMapper.selectByPrimaryKey(item.getMemberId());
                if (null != master || "0".equals(master.getDeleted())) {
                    master.setMemberLevelId(item.getApprovalLevelId());
                    master.setUpdateTime(new Date());

                    mmbMasterMapper.updateByPrimaryKeySelective(master);

                    // erp接口调用会员降级
//                    updateMasterToERP(master.getMemberId());
//                    ErpSendUtil.getInstance().VIPUpdateById(master.getMemberId());
                    ErpSendUtil.VIPUpdateById(master.getMemberId(),mmbMasterMapperExt,mmbMasterMapper);
                }
            }*/

            List<MmbLevelManagerForm> list = mmbLevelRuleMapperExt.selectRelegationMemberList();
            for (MmbLevelManagerForm item : list) {
                MmbMaster master = mmbMasterMapper.selectByPrimaryKey(item.getMemberId());
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
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }
}
