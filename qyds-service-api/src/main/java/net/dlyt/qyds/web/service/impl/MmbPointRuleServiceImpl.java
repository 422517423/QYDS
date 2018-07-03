package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbPointRule;
import net.dlyt.qyds.common.dto.ext.MmbPointRuleExt;
import net.dlyt.qyds.common.form.MmbPointRuleForm;
import net.dlyt.qyds.dao.MmbPointRuleMapper;
import net.dlyt.qyds.dao.ext.ComCodeMapperExt;
import net.dlyt.qyds.dao.ext.MmbPointRuleMapperExt;
import net.dlyt.qyds.web.service.MmbPointRuleService;
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
 * Created by YiLian on 16/7/29.
 */
@Service("mmbPointRuleService")
public class MmbPointRuleServiceImpl implements MmbPointRuleService {

    @Autowired
    private MmbPointRuleMapperExt mmbPointRuleMapperExt;

    @Autowired
    private MmbPointRuleMapper mmbPointRuleMapper;

    @Autowired
    private ComCodeMapperExt comCodeMapperExt;

    public JSONObject getList(MmbPointRuleForm form) {
        JSONObject json = new JSONObject();
        try {
            //数据库检索 -- 过滤数据
            List<MmbPointRuleExt> list = mmbPointRuleMapperExt.selectRuleList(form);
            int allCount = mmbPointRuleMapperExt.getRuleListCount(form);

            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
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
    public JSONObject add(MmbPointRuleForm form, String userId) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getRuleCode())) {
                throw new ExceptionErrorParam("缺少参数:规则编码");
            }

            if (form.getPoint() == null || form.getPoint() == 0) {
                throw new ExceptionErrorParam("缺少参数:积分值");
            }

            int count = mmbPointRuleMapperExt.countSameRule(form);
            if (count > 0) {
                throw new ExceptionErrorParam("已经存在相同的积分奖励规则");
            }

            Date date = new Date();
            String id = UUID.randomUUID().toString();

            MmbPointRule record = new MmbPointRule();

            record.setRuleId(id);
            record.setRuleCode(form.getRuleCode());
//            record.setRuleName(form.getRuleName());
            record.setPoint(form.getPoint());
            record.setComment(form.getComment());
            record.setDeleted("0");
            record.setUpdateUserId(userId);
            record.setUpdateTime(date);
            record.setInsertUserId(userId);
            record.setInsertTime(date);

            mmbPointRuleMapper.insertSelective(record);

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
    public JSONObject edit(MmbPointRuleForm form, String userId) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getRuleId())) {
                throw new ExceptionErrorParam("缺少主键参数");
            }

            if (StringUtil.isEmpty(form.getRuleCode())) {
                throw new ExceptionErrorParam("缺少参数:规则编码");
            }

            if (form.getPoint() == null || form.getPoint() == 0) {
                throw new ExceptionErrorParam("缺少参数:积分值");
            }

            int count = mmbPointRuleMapperExt.countSameRule(form);
            if (count > 0) {
                throw new ExceptionErrorParam("已经存在相同的积分奖励规则");
            }

            MmbPointRule record = mmbPointRuleMapper.selectByPrimaryKey(form.getRuleId());

            if (record == null) {
                throw new ExceptionErrorData("该会员积分奖励规则不存在");
            } else if (!"0".equals(record.getDeleted())) {
                throw new ExceptionErrorData("该会员积分奖励规则已经被删除");
            }


            record.setRuleCode(form.getRuleCode());
//            record.setRuleName(form.getRuleName());
            record.setPoint(form.getPoint());
            record.setComment(form.getComment());
            record.setUpdateUserId(userId);
            record.setUpdateTime(new Date());

            mmbPointRuleMapper.updateByPrimaryKeySelective(record);

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
    public JSONObject delete(MmbPointRuleForm form, String userId) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getRuleId())) {
                throw new ExceptionErrorParam("缺少主键参数");
            }

            MmbPointRule record = mmbPointRuleMapper.selectByPrimaryKey(form.getRuleId());

            if (record == null) {
                throw new ExceptionErrorData("该会员积分奖励规则不存在");
            } else if (!"0".equals(record.getDeleted())) {
                throw new ExceptionErrorData("该会员积分奖励规则已经被删除");
            }

            record.setDeleted("1");
            record.setUpdateUserId(userId);
            record.setUpdateTime(new Date());

            mmbPointRuleMapper.updateByPrimaryKeySelective(record);

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

    public JSONObject getDetail(MmbPointRuleForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getRuleId())) {
                throw new ExceptionErrorParam("缺少主键参数");
            }

            MmbPointRule result = mmbPointRuleMapperExt.select(form);

            if (result == null) {
                throw new ExceptionErrorData("该会员积分奖励规则不存在");
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

    public JSONObject getRuleByCode(MmbPointRuleForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getRuleCode())) {
                throw new ExceptionErrorParam("缺少参数规则编码");
            }

            MmbPointRule result = mmbPointRuleMapperExt.selectByRuleCode(form.getRuleCode());

            if (result == null) {
                throw new ExceptionErrorData("该会员积分奖励规则不存在");
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
}
