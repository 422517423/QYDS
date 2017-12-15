package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbLevelRule;
import net.dlyt.qyds.common.dto.ext.MmbLevelRuleExt;
import net.dlyt.qyds.common.form.MmbLevelRuleForm;
import net.dlyt.qyds.dao.MmbLevelRuleMapper;
import net.dlyt.qyds.dao.ext.MmbLevelRuleMapperExt;
import net.dlyt.qyds.web.service.MmbLevelRuleService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by YiLian on 2016/7/27.
 */
@Service("mmbLevelRuleService")
public class MmbLevelRuleServiceImpl implements MmbLevelRuleService {

    @Autowired
    private MmbLevelRuleMapperExt mmbLevelRuleMapperExt;

    @Autowired
    private MmbLevelRuleMapper mmbLevelRuleMapper;


    public JSONObject getList(MmbLevelRuleForm form) {
        JSONObject json = new JSONObject();
        try {

            List<MmbLevelRuleExt> list = mmbLevelRuleMapperExt.selectAll(form);
            int allCount = mmbLevelRuleMapperExt.getAllDataCount(form);

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

    public JSONObject getDetail(MmbLevelRuleForm form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberLevelId())) {
                throw new ExceptionErrorParam("级别名称不能为空");
            }

            List<MmbLevelRuleExt> list = mmbLevelRuleMapperExt.select(form);

            if (list == null || list.size() == 0) {
                throw new ExceptionErrorData("该会员等级规则不存在");
            }

            MmbLevelRuleExt result = list.get(0);

            if (!"0".equals(result.getDeleted())) {
                throw new ExceptionErrorData("该会员等级规则已经被删除");
            }

            json.put("data", result);
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
     * 编辑会员等级规则信息
     *
     * @param form
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject edit(MmbLevelRuleForm form, String userId) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberLevelId())) {
                throw new ExceptionErrorParam("缺少主键参数");
            }

            if (StringUtil.isEmpty(form.getMemberLevelCode())) {
                throw new ExceptionErrorParam("请选择会员级别");
            }

            if (form.getPointRatio() == null) {
                throw new ExceptionErrorParam("积分系数不能为空");
            }

            double radio = form.getPointRatio().doubleValue();
            if (radio > 999.99) {
                throw new ExceptionErrorParam("积分系数不能大于999.99");
            } else if (radio < 1.00) {
                throw new ExceptionErrorParam("积分系数不能小于1.00");
            }

            if (form.getDiscount() == null) {
                throw new ExceptionErrorParam("优惠折扣不能为空");
            }

            double discount = form.getDiscount().doubleValue();
            if (discount > 1.00) {
                throw new ExceptionErrorParam("优惠折扣不能大于1.00");
            } else if (discount < 0.01) {
                throw new ExceptionErrorParam("优惠折扣不能小于0.01");
            }

            if (form.getPointLower() == null && form.getPointUpper() == null) {
                throw new ExceptionErrorParam("积分范围不能都为空");
            }

            if (form.getPointLower() == null) {
                form.setPointLower(0);
            }
            if (form.getPointUpper() == null) {
                form.setPointUpper(Integer.MAX_VALUE);
            }

            if (form.getPointUpper() <= form.getPointLower()) {
                throw new ExceptionErrorParam("积分上限必须大于积分下限");
            }

            MmbLevelRule levelRule = mmbLevelRuleMapper.selectByPrimaryKey(form.getMemberLevelId());

            if (levelRule == null) {
                throw new ExceptionErrorData("该会员等级规则不存在");
            }

            if (!"0".equals(levelRule.getDeleted())) {
                throw new ExceptionErrorData("该会员等级规则已经被删除");
            }

            MmbLevelRuleForm noForm = new MmbLevelRuleForm();
            List<MmbLevelRuleExt> extList = mmbLevelRuleMapperExt.select(noForm);
            for (MmbLevelRuleExt item : extList) {
                if (item.getMemberLevelId().equals(form.getMemberLevelId())
                        || form.getPointLower() > item.getPointUpper()
                        || form.getPointUpper() < item.getPointLower()) {
                    continue;
                } else {
                    throw new ExceptionErrorParam("积分范围与其他规则重叠");
                }
            }

            levelRule.setPointRatio(form.getPointRatio());
            levelRule.setPointSingle(form.getPointSingle());
            // TODO: 2017/12/14
            levelRule.setPointCumulative(form.getPointCumulative());
            levelRule.setPointLower(form.getPointLower());
            levelRule.setPointUpper(form.getPointUpper());
            levelRule.setDiscount(form.getDiscount());
            levelRule.setComment(form.getComment());
            levelRule.setUpdateUserId(userId);
            levelRule.setUpdateTime(new Date());

            mmbLevelRuleMapper.updateByPrimaryKeySelective(levelRule);

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

    @Transactional(rollbackFor = Exception.class)
    public JSONObject add(MmbLevelRuleForm form, String userId) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberLevelCode())) {
                throw new ExceptionErrorParam("请选择会员级别");
            }

            if (form.getPointRatio() == null) {
                throw new ExceptionErrorParam("积分系数不能为空");
            }

            double radio = form.getPointRatio().doubleValue();
            if (radio > 999.99) {
                throw new ExceptionErrorParam("积分系数不能大于999.99");
            } else if (radio < 1.00) {
                throw new ExceptionErrorParam("积分系数不能小于1.00");
            }

            if (form.getDiscount() == null) {
                throw new ExceptionErrorParam("优惠折扣不能为空");
            }

            double discount = form.getDiscount().doubleValue();
            if (discount > 1.00) {
                throw new ExceptionErrorParam("优惠折扣不能大于1.00");
            } else if (discount < 0.01) {
                throw new ExceptionErrorParam("优惠折扣不能小于0.01");
            }

            if (form.getPointLower() == null && form.getPointUpper() == null) {
                throw new ExceptionErrorParam("积分范围不能都为空");
            }

            if (form.getPointLower() == null) {
                form.setPointLower(0);
            }
            if (form.getPointUpper() == null) {
                form.setPointUpper(Integer.MAX_VALUE);
            }

            if (form.getPointUpper() <= form.getPointLower()) {
                throw new ExceptionErrorParam("积分上限必须大于积分下限");
            }

            MmbLevelRuleForm codeForm = new MmbLevelRuleForm();
            codeForm.setMemberLevelCode(form.getMemberLevelCode());
            List<MmbLevelRuleExt> list = mmbLevelRuleMapperExt.select(codeForm);
            if (list != null && list.size() > 0) {
                throw new ExceptionErrorData("相同级别的会员等级规则已经存在");
            }

            MmbLevelRuleForm noForm = new MmbLevelRuleForm();
            List<MmbLevelRuleExt> extList = mmbLevelRuleMapperExt.select(noForm);
            for (MmbLevelRuleExt item : extList) {
                if (form.getPointLower() > item.getPointUpper() || form.getPointUpper() < item.getPointLower()) {
                    continue;
                } else {
                    throw new ExceptionErrorParam("积分范围与其他规则重叠");
                }
            }

            Date date = new Date();
            String id = UUID.randomUUID().toString();

            MmbLevelRule levelRule = new MmbLevelRule();
            levelRule.setMemberLevelId(id);
            levelRule.setMemberLevelCode(form.getMemberLevelCode());
            // 等级名称采用码表名称
            levelRule.setPointRatio(form.getPointRatio());
            levelRule.setDiscount(form.getDiscount());
            levelRule.setComment(form.getComment());
            levelRule.setPointUpper(form.getPointUpper());
            levelRule.setPointLower(form.getPointLower());
            levelRule.setPointSingle(form.getPointSingle());
            // TODO: 2017/12/14
            levelRule.setPointCumulative(form.getPointCumulative());
            levelRule.setInsertTime(date);
            levelRule.setInsertUserId(userId);
            levelRule.setUpdateUserId(userId);
            levelRule.setUpdateTime(date);

            mmbLevelRuleMapper.insertSelective(levelRule);

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

    @Transactional(rollbackFor = Exception.class)
    public JSONObject delete(MmbLevelRuleForm form, String userId) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberLevelId())) {
                throw new ExceptionErrorParam("缺少主键参数");
            }


            MmbLevelRule levelRule = mmbLevelRuleMapper.selectByPrimaryKey(form.getMemberLevelId());

            if (levelRule != null && "0".equals(levelRule.getDeleted())) {
                levelRule.setDeleted("1");
                levelRule.setUpdateUserId(userId);
                levelRule.setUpdateTime(new Date());

                mmbLevelRuleMapper.updateByPrimaryKeySelective(levelRule);
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
