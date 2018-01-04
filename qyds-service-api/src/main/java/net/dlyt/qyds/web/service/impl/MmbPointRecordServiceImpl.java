package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.MmbLevelRuleExt;
import net.dlyt.qyds.common.dto.ext.MmbPointRecordExt;
import net.dlyt.qyds.common.form.ErpPointRecordForm;
import net.dlyt.qyds.common.form.MmbLevelRuleForm;
import net.dlyt.qyds.common.form.MmbPointRecordForm;
import net.dlyt.qyds.dao.MmbLevelRuleMapper;
import net.dlyt.qyds.dao.MmbMasterMapper;
import net.dlyt.qyds.dao.MmbPointRecordMapper;
import net.dlyt.qyds.dao.MmbPointRuleMapper;
import net.dlyt.qyds.dao.ext.MmbLevelRuleMapperExt;
import net.dlyt.qyds.dao.ext.MmbPointRecordMapperExt;
import net.dlyt.qyds.dao.ext.MmbPointRuleMapperExt;
import net.dlyt.qyds.web.service.MmbPointRecordService;
import net.dlyt.qyds.web.service.common.*;
import net.dlyt.qyds.web.service.erp.BaseDate;
import net.dlyt.qyds.web.service.erp.ServiceSoap;
import net.dlyt.qyds.web.service.erp.VipPoint;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import net.dlyt.qyds.web.service.exception.ExceptionNoPower;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static net.dlyt.qyds.web.service.common.ErpKeyUtil.getKeyVIPPointUpdate;

/**
 * Created by YiLian on 16/7/28.
 */

@Service("mmbPointRecordService")
public class MmbPointRecordServiceImpl implements MmbPointRecordService {

    protected final Logger log = LoggerFactory.getLogger(MmbPointRecordServiceImpl.class);
    net.dlyt.qyds.web.service.erp.Service service;// = new net.dlyt.qyds.web.service.erp.Service();
    ServiceSoap soap;// = service.getServiceSoap();
    @Autowired
    private MmbPointRecordMapperExt mmbPointRecordMapperExt;
    @Autowired
    private MmbMasterMapper mmbMasterMapper;
    @Autowired
    private MmbPointRuleMapper mmbPointRuleMapper;
    @Autowired
    private MmbLevelRuleMapper mmbLevelRuleMapper;
    @Autowired
    private MmbPointRecordMapper mmbPointRecordMapper;
    @Autowired
    private MmbLevelRuleMapperExt mmbLevelRuleMapperExt;
    @Autowired
    private MmbPointRuleMapperExt mmbPointRuleMapperExt;

    /**
     * 列表查询
     *
     * @param form memberId:会员ID
     *             currentPage:当前取得页数
     * @return
     */
    public JSONObject getRecordList(MmbPointRecordForm form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }

            MmbMaster master = mmbMasterMapper.selectByPrimaryKey(form.getMemberId());
            if (master == null || !"0".equals(master.getDeleted())) {
                throw new ExceptionErrorData("会员不存在");
            }

            form.setCurrentPage(form.getCurrentPage() * 10);

            List<MmbPointRecordExt> list = mmbPointRecordMapperExt.queryRecordList(form);

            json.put("data", list);
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
     * @param form memberId:会员ID[必须]
     *             rule_id:积分规则Code[添加10]
     *             cash:实际现金数额[添加]
     *             worth_id:积分规则ID[扣除 80][抵值 70]
     *             worthCash:积分抵值金额[扣除][抵值]
     *             worthPoint:抵值积分[扣除][抵值]
     *             exchangeId:积分规则ID[换购]
     *             exchangePoint:换购积分[换购]
     *             score_source:积分来源(订单号)[必须]
     *             update_user_id: 如果是手工添加需要传值,默认为会员自己[扣除]
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject add(MmbPointRecordForm form) {
        //        10	现金消费 --11积分不生效
        //        20	赠送积分 --21积分不生效
        //        90	生日消费 --91积分不生效
        //        60	积分换购
        //        70	积分抵值
        //        80	订单退货
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }

            if (StringUtil.isEmpty(form.getScoreSource())) {
                throw new ExceptionErrorParam("缺少参数积分来源");
            }

            if (!StringUtil.isEmpty(form.getRuleId()) && !"10".equals(form.getRuleId()) && !"20".equals(form.getRuleId())) {
                throw new ExceptionErrorParam("消费积分规则ID不正确");
            }

            if ("10".equals(form.getRuleId()) && null == form.getCash()) {
                throw new ExceptionErrorParam("缺少参数现金数额");
            }

            if ("20".equals(form.getRuleId()) && (null == form.getPresentPoint() || form.getPresentPoint() <= 0)) {
                throw new ExceptionErrorParam("缺少参数赠送积分");
            }

            if (!StringUtil.isEmpty(form.getWorthId()) && !"70".equals(form.getWorthId()) && !"80".equals(form.getWorthId())) {
                throw new ExceptionErrorParam("抵值积分规则ID不正确");
            }

            if (!StringUtil.isEmpty(form.getWorthId()) && form.getWorthCash() == null) {
                throw new ExceptionErrorParam("缺少参数抵值金额");
            }

            if (!StringUtil.isEmpty(form.getExchangeId()) && !"60".equals(form.getExchangeId())) {
                throw new ExceptionErrorParam("换购积分规则ID不正确");
            }


            if (form.getExchangeId() != null && "60".equals(form.getExchangeId())) {
                if( form.getExchangePoint() == null||form.getExchangePoint() == 0){
                    throw new ExceptionErrorParam("缺少参数换购积分");
                }
            }

            // 取得会员主表信息
            MmbMaster master = mmbMasterMapper.selectByPrimaryKey(form.getMemberId());
            if (master == null || !"0".equals(master.getDeleted())) {
                throw new ExceptionErrorData("会员不存在");
            }

            // 手工添加积分的话updateUser是他人处理,其他情况都是会员自动添加
            String userid = StringUtil.isEmpty(form.getUpdateUserId()) ? form.getMemberId() : form.getUpdateUserId();

            Date date = new Date();

            // 抵制积分
            Integer point_sub = 0;

            // 换购积分
            Integer point_exc = 0;

            // 消费积分
            Integer point_add = 0;

            // 赠送积分
            Integer point_present = 0;

            // 退货积分
            Integer point_return = 0;

            /**************************会员使用积分换购商品**************************/
            if ("60".equals(form.getExchangeId())) {

                if (form.getExchangePoint() > 0 && master.getPoint() < form.getExchangePoint()) {
                    throw new ExceptionErrorData("会员可换购积分数量不足");
                }

                if ("70".equals(form.getWorthId()) && form.getWorthPoint() > 0) {
                    if (master.getPoint() < form.getWorthPoint() + form.getExchangePoint()) {
                        throw new ExceptionErrorData("会员可用积分数量不足");
                    }
                }

                // 取得积分规则(消费)
//                MmbPointRule rule_worth = mmbPointRuleMapperExt.selectByRuleCode("60");
//                if (rule_worth == null || !"0".equals(rule_worth.getDeleted())) {
//                    throw new ExceptionErrorData("积分规则不存在");
//                }

                point_exc = form.getExchangePoint();

                // 积分履历
                MmbPointRecord record_exc = new MmbPointRecord();

                // 会员ID
                record_exc.setMemberId(form.getMemberId());
                // 积分类型
                record_exc.setType("60");
                // 积分规则ID
                record_exc.setRuleId("60");
                // 积分值(消费积分为负数)
                record_exc.setPoint(-point_exc);
                // 消费金额
                record_exc.setPointCash(0);
                // 剩余积分值
                record_exc.setPointSurplus(0);
                // 积分时间
                record_exc.setPointTime(date);
                // 积分来源
                record_exc.setScoreSource(form.getScoreSource());
                // 删除标记
                record_exc.setDeleted("0");
                // 修改人
                record_exc.setUpdateUserId(userid);
                // 修改时间
                record_exc.setUpdateTime(date);
                // 创建人
                record_exc.setInsertUserId(userid);
                // 创建时间
                record_exc.setInsertTime(date);

                // 插入积分履历
                mmbPointRecordMapper.insertSelective(record_exc);
                // ERP接口调用
//                sendErpPoint(record_exc);
                ErpSendUtil.VIPPointUpdate(record_exc,mmbPointRecordMapper,mmbMasterMapper);

                // 按照倒叙检索最近的三十六个月积分,进行积分抵值扣除处理
                List<MmbPointRecordExt> sublist = mmbPointRecordMapperExt.selectSubPointList(form);
                int subCount = 0;
                for (MmbPointRecordExt item : sublist) {
                    Integer surplus = item.getPointSurplus();
                    Integer sur_del = point_exc - subCount;
                    if (sur_del <= surplus) {
                        // 扣除<=可抵值数
                        MmbPointRecord record_del = new MmbPointRecord();
                        record_del.setRecordNo(item.getRecordNo());
                        // 剩余积分值
                        record_del.setPointSurplus(surplus - sur_del);
                        // 修改人
                        record_del.setUpdateUserId(userid);
                        // 修改时间
                        record_del.setUpdateTime(date);
                        mmbPointRecordMapper.updateByPrimaryKeySelective(record_del);
                        break;

                    } else {
                        // 扣除 > 可抵值数
                        MmbPointRecord record_del = new MmbPointRecord();
                        record_del.setRecordNo(item.getRecordNo());
                        // 剩余积分值
                        record_del.setPointSurplus(0);
                        // 修改人
                        record_del.setUpdateUserId(userid);
                        // 修改时间
                        record_del.setUpdateTime(date);
                        mmbPointRecordMapper.updateByPrimaryKeySelective(record_del);

                        subCount = subCount + surplus;
                    }
                }
            }
            /**************************会员使用积分抵值现金**************************/

            /**************************会员使用积分抵值现金**************************/
            if ("70".equals(form.getWorthId())) {

                if (form.getWorthPoint() > 0 && master.getPoint() < form.getWorthPoint()) {
                    throw new ExceptionErrorData("会员可抵值积分数量不足");
                }

                if ("60".equals(form.getExchangeId()) && form.getExchangePoint() > 0) {
                    if (master.getPoint() < form.getWorthPoint() + form.getExchangePoint()) {
                        throw new ExceptionErrorData("会员可用积分数量不足");
                    }
                }

                // 取得积分规则(消费)
                MmbPointRule rule_worth = mmbPointRuleMapperExt.selectByRuleCode("70");
                if (rule_worth == null || !"0".equals(rule_worth.getDeleted())) {
                    throw new ExceptionErrorData("积分规则不存在");
                }

                Double point_sub_db = rule_worth.getPoint() * form.getWorthCash().doubleValue();

                point_sub = NumberUtil.formatInteger(point_sub_db);

                // 积分履历
                MmbPointRecord record_sub = new MmbPointRecord();

                // 会员ID
                record_sub.setMemberId(form.getMemberId());
                // 积分类型
                record_sub.setType("70");
                // 积分规则ID
                record_sub.setRuleId("70");
                // 积分值(消费积分为负数)
                record_sub.setPoint(-point_sub);
                // 消费金额
                record_sub.setPointCash(0);
                // 剩余积分值
                record_sub.setPointSurplus(0);
                // 积分时间
                record_sub.setPointTime(date);
                // 积分来源
                record_sub.setScoreSource(form.getScoreSource());
                // 删除标记
                record_sub.setDeleted("0");
                // 修改人
                record_sub.setUpdateUserId(userid);
                // 修改时间
                record_sub.setUpdateTime(date);
                // 创建人
                record_sub.setInsertUserId(userid);
                // 创建时间
                record_sub.setInsertTime(date);

                // 插入积分履历
                mmbPointRecordMapper.insertSelective(record_sub);
                // ERP接口调用
//                sendErpPoint(record_sub);
                ErpSendUtil.VIPPointUpdate(record_sub,mmbPointRecordMapper,mmbMasterMapper);

                // 按照倒叙检索最近的三十六个月积分,进行积分抵值扣除处理
                List<MmbPointRecordExt> sublist = mmbPointRecordMapperExt.selectSubPointList(form);
                int subCount = 0;
                for (MmbPointRecordExt item : sublist) {
                    Integer surplus = item.getPointSurplus();
                    Integer sur_del = point_sub - subCount;
                    if (sur_del <= surplus) {
                        // 扣除<=可抵值数
                        MmbPointRecord record_del = new MmbPointRecord();
                        record_del.setRecordNo(item.getRecordNo());
                        // 剩余积分值
                        record_del.setPointSurplus(surplus - sur_del);
                        // 修改人
                        record_del.setUpdateUserId(userid);
                        // 修改时间
                        record_del.setUpdateTime(date);
                        mmbPointRecordMapper.updateByPrimaryKeySelective(record_del);
                        break;

                    } else {
                        // 扣除 > 可抵值数
                        MmbPointRecord record_del = new MmbPointRecord();
                        record_del.setRecordNo(item.getRecordNo());
                        // 剩余积分值
                        record_del.setPointSurplus(0);
                        // 修改人
                        record_del.setUpdateUserId(userid);
                        // 修改时间
                        record_del.setUpdateTime(date);
                        mmbPointRecordMapper.updateByPrimaryKeySelective(record_del);

                        subCount = subCount + surplus;
                    }
                }
            }
            /**************************会员使用积分抵值现金**************************/
            /**************************会员退货积分扣除**************************/
            else if ("80".equals(form.getWorthId())) {
                List<MmbPointRecord> historyList = mmbPointRecordMapperExt.selectPointRecordByScoreSource(form.getScoreSource());
                if (historyList == null || historyList.size() == 0) {
                    throw new ExceptionErrorData("订单积分履历没找到");
                }


                for (MmbPointRecord item : historyList) {

                    // 如果订单包含赠送积分，则需要把赠送的积分也退还
                    if("21".equals(item.getType())){
                        point_return = point_return + item.getPoint();
                        item.setPoint(0);
                    } else {
                        Double item_sub = item.getPointRatio().doubleValue() * form.getWorthCash().doubleValue();
                        Integer item_sub_int = NumberUtil.formatInteger(item_sub);
                        point_return = point_return + item_sub_int;
                        item.setPoint(item.getPoint() - item_sub_int);

                        Integer worthCash_int = NumberUtil.formatInteger(form.getWorthCash());

                        if (item.getPointCash() > worthCash_int) {
                            item.setPointCash(item.getPointCash() - worthCash_int);
                        } else {
                            item.setPointCash(0);
                        }
                    }

                    item.setUpdateTime(date);
                    item.setUpdateUserId(userid);

                    mmbPointRecordMapper.updateByPrimaryKeySelective(item);
                }
            }
            /**************************会员退货积分扣除**************************/

            /**************************会员消费现金后积分累计**************************/
            if ("10".equals(form.getRuleId())) {

                // 取得积分规则(累计)
                MmbPointRule rule = mmbPointRuleMapperExt.selectByRuleCode("10");
                if (rule == null || !"0".equals(rule.getDeleted())) {
                    throw new ExceptionErrorData("累计积分规则不存在");
                }

                // 根据会员级别取得对应的积分系数
                if (StringUtil.isEmpty(master.getMemberLevelId())) {
                    throw new ExceptionErrorData("会员级别ID不存在");
                }

                MmbLevelRuleForm ruleForm = new MmbLevelRuleForm();
                ruleForm.setMemberLevelCode(master.getMemberLevelId());
                List<MmbLevelRuleExt> levels = mmbLevelRuleMapperExt.select(ruleForm);
                if (levels == null || levels.size() == 0) {
                    throw new ExceptionErrorData("会员级别不存在");
                }

                MmbLevelRuleExt currentLevel = levels.get(0);

                Double radio = currentLevel.getPointRatio().doubleValue();

                // 汇率*等级
                Double point_radio = rule.getPoint() * radio;

                // 根据会员等级的积分系数进行处理 金额*汇率*等级
                Double point_double = form.getCash().doubleValue() * point_radio;
                point_add = NumberUtil.formatInteger(point_double);

                // 积分履历
                MmbPointRecord record_add = new MmbPointRecord();

                // 会员ID
                record_add.setMemberId(form.getMemberId());
                // 积分类型
                record_add.setType("11");
                // 积分规则ID
                record_add.setRuleId("11");
                // 积分值
                record_add.setPoint(point_add);
                // 消费金额
                record_add.setPointCash(NumberUtil.formatInteger(form.getCash()));
                // 剩余积分值
                record_add.setPointSurplus(0);
                // 积分系数
                record_add.setPointRatio(new BigDecimal(point_radio));
                // 积分时间
                record_add.setPointTime(date);
                // 积分来源
                record_add.setScoreSource(form.getScoreSource());
                // 删除标记
                record_add.setDeleted("0");
                // 修改人
                record_add.setUpdateUserId(userid);
                // 修改时间
                record_add.setUpdateTime(date);
                // 创建人
                record_add.setInsertUserId(userid);
                // 创建时间
                record_add.setInsertTime(date);

                // 插入积分履历
                mmbPointRecordMapper.insertSelective(record_add);

                // 如果是会员生日,当天积分双倍,插入两条数据
                if (isBirthdate(master.getBirthdate())) {

                    // 生日积分规则=
                    MmbPointRule rule_birth = mmbPointRuleMapperExt.selectByRuleCode("90");

                    if (rule_birth != null) {
                        // 积分履历
                        MmbPointRecord record_birth = new MmbPointRecord();

                        // 会员ID
                        record_birth.setMemberId(form.getMemberId());
                        // 积分类型
                        record_birth.setType("91");
                        // 积分规则ID
                        record_birth.setRuleId("91");
                        // 积分值
                        record_birth.setPoint(point_add);
                        // 消费金额
                        record_birth.setPointCash(0);
                        // 剩余积分值
                        record_birth.setPointSurplus(0);
                        // 积分系数
                        record_birth.setPointRatio(new BigDecimal(point_radio));
                        // 积分时间
                        record_birth.setPointTime(date);
                        // 积分来源
                        record_birth.setScoreSource(form.getScoreSource());
                        // 删除标记
                        record_birth.setDeleted("0");
                        // 修改人
                        record_birth.setUpdateUserId(userid);
                        // 修改时间
                        record_birth.setUpdateTime(date);
                        // 创建人
                        record_birth.setInsertUserId(userid);
                        // 创建时间
                        record_birth.setInsertTime(date);

                        // 插入积分履历
                        mmbPointRecordMapper.insertSelective(record_birth);

                        point_add = point_add + point_add;

                    } else {
                        throw new ExceptionErrorData("生日双倍积分规则没有创建");
                    }
                }
            }
            /**************************会员消费现金后积分累计**************************/


            /**************************会员购物增送积分累计**************************/
            if ("20".equals(form.getRuleId())) {

//                // 取得积分规则(累计)
//                MmbPointRule rule = mmbPointRuleMapperExt.selectByRuleCode("20");
//                if (rule == null || !"0".equals(rule.getDeleted())) {
//                    throw new ExceptionErrorData("增送累计积分规则不存在");
//                }

                point_present = form.getPresentPoint();

                // 积分履历
                MmbPointRecord record_present = new MmbPointRecord();

                // 会员ID
                record_present.setMemberId(form.getMemberId());
                // 积分类型
                record_present.setType("21");
                // 积分规则ID
                record_present.setRuleId("21");
                // 积分值
                record_present.setPoint(point_present);
                // 消费金额
                record_present.setPointCash(0);
                // 剩余积分值
                record_present.setPointSurplus(0);
                // 积分系数
                record_present.setPointRatio(BigDecimal.ZERO);
                // 积分时间
                record_present.setPointTime(date);
                // 积分来源
                record_present.setScoreSource(form.getScoreSource());
                // 删除标记
                record_present.setDeleted("0");
                // 修改人
                record_present.setUpdateUserId(userid);
                // 修改时间
                record_present.setUpdateTime(date);
                // 创建人
                record_present.setInsertUserId(userid);
                // 创建时间
                record_present.setInsertTime(date);

                // 插入积分履历
                mmbPointRecordMapper.insertSelective(record_present);
            }
            /**************************会员购物增送积分累计**************************/

            // 会员主表积分更新
            // 可用积分重新加减处理  + point_add -- 消费积分待订单完成后处理
            master.setPoint(master.getPoint() - point_sub - point_exc);
            // 累计总积分-- 实时更新，包括处理新增消费积分、抵值积分、积分换购、退货积分
            master.setAllPoint(master.getAllPoint() + point_add - point_sub - point_exc - point_return + point_present);
            master.setUpdateTime(date);
            master.setUpdateUserId(userid);
            //添加会员购物后将积分加入累计消费
//            master.setPointCumulative(master.getAllPoint());
            master.setPointCumulative(master.getPointCumulative()+ point_add - point_sub - point_exc - point_return + point_present);

            // 更新会员主表信息
            mmbMasterMapper.updateByPrimaryKeySelective(master);

            json.put("resultCode", Constants.NORMAL);

        } catch (ExceptionBusiness e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    /**
     * ERP积分履历接口调用
     *
     * @param record
     * @return
     */
//    private void sendErpPoint(MmbPointRecord record) {
//        MmbPointRecord master = new MmbPointRecord();
//        master.setRecordNo(record.getRecordNo());
//        master.setErpSendStatus("10");
//        try {
//            MmbMaster member = mmbMasterMapper.selectByPrimaryKey(record.getMemberId());
//            VipPoint point = new VipPoint();
//            point.setMemberCode(member.getTelephone());
//            point.setPoint(record.getPoint().toString());
//            point.setPointTime(DataUtils.formatTimeStampToYMD(record.getPointTime()));
//            BaseDate date = new BaseDate();
//            date.setVipPoint(point);
//            String result = soap.vipPointUpdate(getKeyVIPPointUpdate(date), date);
//            log.debug("ERP vipPointUpdate result code:"+result+",param:"+ JSON.toJSONString(date));
//            if (result.equals("00")) {
//            } else if (result.equals("11")) {
//                throw new ExceptionBusiness("ERP数据库连接失败");
//            } else if (result.equals("12")) {
//                throw new ExceptionBusiness("ERP数据库更新失败");
//            } else if (result.equals("21")) {
//                throw new ExceptionBusiness("ERP验证失败");
//            } else {
//                throw new ExceptionBusiness("ERP未知错误");
//            }
//        } catch (Exception e) {
//            master.setErpSendStatus("20");
//        } finally {
//            master.setUpdateTime(new Date());
//            mmbPointRecordMapper.updateByPrimaryKeySelective(master);
//        }
//    }

    /**
     * 判断是否为会员生日
     *
     * @param date
     * @return
     */
    private Boolean isBirthdate(Date date) {

        if (null == date) {
            return false;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month_bd = cal.get(Calendar.MONTH);
        int day_bd = cal.get(Calendar.DAY_OF_MONTH);

        Calendar current = Calendar.getInstance();
        current.setTime(new Date());
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);

        if (month_bd == month && day_bd == day) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 清除36个月前的消费积分
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject clearSurplusPoint() {
        JSONObject json = new JSONObject();
        try {
            Date now = new Date();
            List<MmbPointRecordExt> list = mmbPointRecordMapperExt.countSubPoint(now);
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
            mmbPointRecordMapperExt.clearSubPoint(now);
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
     * 通知一个月后清除消费的积分
     *
     * @return
     */
    public JSONObject notifyClearPoint() {
        JSONObject json = new JSONObject();
        try {
            List<MmbPointRecordExt> list = mmbPointRecordMapperExt.selectNotifyPoint();
            json.put("data", list);
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
//    private void clearPointRecord(MmbPointRecordExt record) throws Exception{
//        MmbPointRecord record1 = new MmbPointRecord();
//        record1.setRecordNo(record.getRecordNo());
//        record1.setErpSendStatus("10");
//        try {
//            VipPoint point = new VipPoint();
//            point.setMemberCode(record.getMemberCode());
//            point.setPoint(record.getPoint()==null?"":record.getPoint().toString());
//            point.setPointTime(DataUtils.formatTimeStampToYMD(record.getPointTime()));
//
//            BaseDate baseDate = new BaseDate();
//            baseDate.setVipPoint(point);
//            String result = soap.vipPointUpdate(getKeyVIPPointUpdate(baseDate), baseDate);
//            log.debug("ERP vipPointUpdate result code:"+result+",param:"+ JSON.toJSONString(baseDate));
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
//        }catch (Exception e){
//            // 若通讯失败,记录失败原因及通讯信息
//            record1.setErpSendStatus("20");
//            throw e;
//        } finally {
//            record1.setUpdateTime(new Date());
//            mmbPointRecordMapper.updateByPrimaryKeySelective(record1);
//        }
//    }

    /**
     * 处理会员积分累计
     * 条件:
     * 1-消费积分及生日积分(积分不生效状态)
     * 2.1-购物订单为完成状态
     * 2.2-ERP数据为积分时间超过三十天
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addPoint() {
        JSONObject json = new JSONObject();
        json.put("resultCode", Constants.NORMAL);
        try {
            // 订单完成的消费积分[包括生日积分]
            List<MmbPointRecord> list = mmbPointRecordMapperExt.queryRecordOfSuccess();

            //        10	现金消费 --11积分不生效
            //        20	赠送积分 --21积分不生效
            //        90	生日消费 --91积分不生效
            for (MmbPointRecord item : list) {
                // 剩余积分值
                item.setPointSurplus(item.getPoint());
                item.setUpdateTime(new Date());

                // 剩余积分值
                item.setPointSurplus(item.getPoint());

                // 根据类型设置为有效积分
                if ("11".equals(item.getType())) {
                    // 积分类型
                    item.setType("10");
                    // 积分规则ID
                    item.setRuleId("10");
                } else if ("91".equals(item.getType())) {
                    // 积分类型
                    item.setType("90");
                    // 积分规则ID
                    item.setRuleId("90");
                } else if ("21".equals(item.getType())) {
                    // 积分类型
                    item.setType("20");
                    // 积分规则ID
                    item.setRuleId("20");
                }

                // 将有效积分更新到主表信息
                MmbMaster master = mmbMasterMapper.selectByPrimaryKey(item.getMemberId());
                if (master != null && "0".equals(master.getDeleted())) {
                    master.setPoint(master.getPoint() + item.getPoint());
                    master.setUpdateTime(new Date());
                    mmbMasterMapper.updateByPrimaryKeySelective(master);
                }

                // 更新积分记录
                mmbPointRecordMapper.updateByPrimaryKeySelective(item);

                if (!"ERP".equals(item.getInsertUserId()) && 0 != item.getPoint()) {
                    // ERP接口调用
//                    sendErpPoint(item);
                    ErpSendUtil.VIPPointUpdate(item,mmbPointRecordMapper,mmbMasterMapper);
                }
            }

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
     * 根据条件获取积分记录列表
     *
     * @param form
     * @return
     */
    public JSONObject selectRecordByPage(MmbPointRecordForm form){
        JSONObject json = new JSONObject();
        try{
            List<MmbPointRecordExt> list = (List<MmbPointRecordExt>) mmbPointRecordMapperExt.selectRecordByPage(form);
            int countAll = mmbPointRecordMapperExt.getRecordCountByPage(form);
            json.put("aaData", list);
            json.put("sEcho",form.getsEcho());
            json.put("iTotalRecords",countAll);
            json.put("iTotalDisplayRecords",countAll);
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

}