package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.form.ActTemplateForm;
import net.dlyt.qyds.common.form.GoodsCodeColorNameDto;
import net.dlyt.qyds.common.form.SkuForm;
import net.dlyt.qyds.dao.ActTempParamMapper;
import net.dlyt.qyds.dao.ActTemplateMapper;
import net.dlyt.qyds.dao.ext.ActTempParamMapperExt;
import net.dlyt.qyds.dao.ext.ActTemplateMapperExt;
import net.dlyt.qyds.dao.ext.ErpGoodsMapperExt;
import net.dlyt.qyds.dao.ext.SkuMapperExt;
import net.dlyt.qyds.web.service.ActTemplateService;
import net.dlyt.qyds.web.service.common.ComCode;
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
 * Created by cjk on 16/7/27.
 */
@Service("actTemplateService")
public class ActTemplateServiceImpl implements ActTemplateService {

    @Autowired
    ActTemplateMapper actTemplateMapper;

    @Autowired
    ActTempParamMapper actTempParamMapper;

    @Autowired
    ActTempParamMapperExt actTempParamMapperExt;

    @Autowired
    ActTemplateMapperExt actTemplateMapperExt;

    @Autowired
    ErpGoodsMapperExt erpGoodsMapperExt;

    @Autowired
    SkuMapperExt skuMapperExt;

    public JSONObject getTemplateList(ActTemplateForm form) {
        JSONObject json = new JSONObject();
        try {
            //数据库检索 -- 过滤数据
            form.setDeleted(Constants.DELETED_NO);
            List<ActTemplate> list = actTemplateMapperExt.select(form);
            int allCount = actTemplateMapperExt.selectCount(form);
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

    public JSONObject getTemplateApproveList(ActTemplateForm form) {
        JSONObject json = new JSONObject();
        try {
            //数据库检索 -- 过滤数据
            form.setDeleted(Constants.DELETED_NO);
            List<ActTemplate> list = actTemplateMapperExt.selectApproveList(form);
            int allCount = actTemplateMapperExt.selectApproveCount(form);
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

    public JSONObject getDetail(ActTemplateForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getTempId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            ActTemplateForm template = actTemplateMapperExt.selectById(form);
            if (template == null) {
                throw new ExceptionErrorData("活动模板不存在");
            }
            // 检索参数
            ActTempParam actTempParam = new ActTempParam();
            actTempParam.setTempId(template.getTempId());
            List<ActTempParam> paramList = actTempParamMapperExt.selectByTempId(actTempParam);
            template.setParamList(paramList);
            // 满送货品的情况需要列出货品
            if (ComCode.ActivityType.FULL_SEND_GOODS.equals(template.getActitionType())) {
                if (paramList.size() > 0) {
                    //{"goodCode": "DA111", "colorCode": "022"}
                    String skuJson = paramList.get(0).getParamValue();
                    if (!StringUtil.isEmpty(skuJson)) {
                        List<SkuForm> skuIdList = (List<SkuForm>) JSONArray.parseArray(skuJson, SkuForm.class);
                        // 拼成符合in的查询语句
                        StringBuffer inIdsSb = new StringBuffer();
                        for (int i = 0; i < skuIdList.size(); i++) {
                            inIdsSb.append("'");
                            inIdsSb.append(skuIdList.get(i).getGoodsCode());
                            inIdsSb.append("_");
                            inIdsSb.append(skuIdList.get(i).getColorCode());
                            inIdsSb.append("'");
                            if (i != skuIdList.size() - 1) {
                                inIdsSb.append(",");
                            }
                        }
                        List<SkuForm> skuList = skuMapperExt.getSkuColorsByGoodsCodeAndColorCode(inIdsSb.toString());
                        template.setSkuList(skuList);
                    }
                }
            }
            json.put("data", template);
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


    public JSONObject delete(ActTemplateForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getTempId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            ActTemplate record = new ActTemplate();
            record.setTempId(form.getTempId());
            record.setDeleted(Constants.DELETED_YES);
            actTemplateMapper.updateByPrimaryKeySelective(record);
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
    public JSONObject add(ActTemplateForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getTempName())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getActitionType())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 判断模板名称是否重复
            ActTemplate form1 = new ActTemplate();
            form1.setTempName(form.getTempName());
            int count = actTemplateMapperExt.checkExistByTempName(form1);
            if (count > 0) {
                throw new ExceptionErrorData("已经存在相同名称的模板");
            }
            // 插入模板主表
            Date date = new Date();
            String id = UUID.randomUUID().toString();
            ActTemplate at = new ActTemplate();
            at.setTempId(id);
            at.setDeleted(Constants.DELETED_NO);
            at.setTempName(form.getTempName());
            at.setActitionType(form.getActitionType());
            at.setApplyContent(form.getApplyContent());
            at.setApproveStatus(ComCode.ApproveStatus.APPROVE_STATUS_NOT_APPLY);
            at.setShopId(form.getShopId());
            at.setComment(form.getComment());
            at.setInsertUserId(form.getInsertUserId());
            at.setInsertTime(date);
            actTemplateMapper.insertSelective(at);
            if (form.getParamList() != null) {
                for (int i = 0; i < form.getParamList().size(); i++) {
                    ActTempParam param = new ActTempParam();
                    param.setInsertUserId(form.getInsertUserId());
                    param.setShopId("000000");
                    param.setInsertTime(date);
                    param.setParamCondition(form.getParamList().get(i).getParamCondition());
                    param.setParamValue(form.getParamList().get(i).getParamValue());
                    param.setTempId(id);
                    param.setParamId(UUID.randomUUID().toString());
                    actTempParamMapper.insertSelective(param);
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
    public JSONObject edit(ActTemplateForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getTempId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getTempName())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getActitionType())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 检索出原来的数据
            ActTemplate oldData = actTemplateMapper.selectByPrimaryKey(form.getTempId());
            if (oldData == null || Constants.DELETED_YES.equals(oldData.getDeleted())) {
                throw new ExceptionErrorData("模板不存在");
            }
            if (!oldData.getTempName().equals(form.getTempName())) {
                // 模板名称发生变化
                // 判断模板名称是否重复
                ActTemplate form1 = new ActTemplate();
                form1.setTempName(form.getTempName());
                int count = actTemplateMapperExt.checkExistByTempName(form1);
                if (count > 0) {
                    throw new ExceptionErrorData("已经存在相同名称的模板");
                }
            }

            // 更新模板主表
            Date date = new Date();
            form.setUpdateUserId(form.getUpdateUserId());
            form.setUpdateTime(date);
            // 编辑之后状态变成未申请
            form.setApproveStatus(ComCode.ApproveStatus.APPROVE_STATUS_NOT_APPLY);
            actTemplateMapper.updateByPrimaryKeySelective(form);
            // 先删除之前的参数,重新插入
            ActTempParam actTempParam = new ActTempParam();
            actTempParam.setTempId(form.getTempId());
            actTempParamMapperExt.deleteByTempId(actTempParam);
            if (form.getParamList() != null) {
                for (int i = 0; i < form.getParamList().size(); i++) {
                    ActTempParam param = new ActTempParam();
                    param.setInsertUserId(form.getInsertUserId());
                    param.setShopId(form.getShopId());
                    param.setInsertTime(date);
                    param.setParamCondition(form.getParamList().get(i).getParamCondition());
                    param.setParamValue(form.getParamList().get(i).getParamValue());
                    param.setTempId(form.getTempId());
                    param.setParamId(UUID.randomUUID().toString());
                    actTempParamMapper.insertSelective(param);
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
    public JSONObject apply(ActTemplateForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getTempId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 获取当前审批状态
            ActTemplate template = actTemplateMapper.selectByPrimaryKey(form.getTempId());
            if (template == null) {
                throw new ExceptionErrorData("模板不存在");
            }
            if (!ComCode.ApproveStatus.APPROVE_STATUS_NOT_APPLY.equals(template.getApproveStatus())) {
                throw new ExceptionErrorData("审批状态不正确");
            }
            template.setApplyContent(form.getApplyContent());
            template.setApproveStatus(ComCode.ApproveStatus.APPROVE_STATUS_APPROVING);
            template.setApplyUserId(form.getApplyUserId());
            template.setApplyTime(new Date());
            actTemplateMapper.updateByPrimaryKeySelective(template);
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
    public JSONObject approve(ActTemplateForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getTempId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 获取当前审批状态
            ActTemplate template = actTemplateMapper.selectByPrimaryKey(form.getTempId());
            if (template == null) {
                throw new ExceptionErrorData("模板不存在");
            }
            if (!ComCode.ApproveStatus.APPROVE_STATUS_APPROVING.equals(template.getApproveStatus())) {
                throw new ExceptionErrorData("审批状态不正确");
            }
            template.setApproveContent(form.getApproveContent());
            template.setApproveStatus(ComCode.ApproveStatus.APPROVE_STATUS_APPROVED);
            template.setApproveUserId(form.getApproveUserId());
            template.setApproveTime(new Date());
            actTemplateMapper.updateByPrimaryKeySelective(template);
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
    public JSONObject reject(ActTemplateForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getTempId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 获取当前审批状态
            ActTemplate template = actTemplateMapper.selectByPrimaryKey(form.getTempId());
            if (template == null) {
                throw new ExceptionErrorData("模板不存在");
            }
            if (!ComCode.ApproveStatus.APPROVE_STATUS_APPROVING.equals(template.getApproveStatus())) {
                throw new ExceptionErrorData("审批状态不正确");
            }
            template.setApproveContent(form.getApproveContent());
            template.setApproveStatus(ComCode.ApproveStatus.APPROVE_STATUS_REJECT);
            template.setApproveUserId(form.getApproveUserId());
            template.setApproveTime(new Date());
            actTemplateMapper.updateByPrimaryKeySelective(template);
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
            List<ErpGoods> list = erpGoodsMapperExt.getActGoodsColorListById(id);
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
     * @param tempId
     * @param skuList
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject importSkuList(String loginId, String tempId, List<GoodsCodeColorNameDto> skuList) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(tempId)) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 活动信息检查
            ActTemplate master = actTemplateMapper.selectByPrimaryKey(tempId);
            if (master == null || Constants.DELETED_YES.equals(master.getDeleted())) {
                throw new ExceptionErrorData("指定活动不存在");
            }
            // 未申请或已申请或驳回状态的才可以导入
            if ((!master.getApproveStatus().equals("10"))
                    && (!master.getApproveStatus().equals("30"))
                    && (!master.getApproveStatus().equals("40"))) {
                throw new ExceptionErrorData("指定活动状态不正确");
            }
            // 活动类型为满送货品才可以导入
            if (!master.getActitionType().equals("42")) {
                throw new ExceptionErrorData("指定活动类型不正确");
            }
            // 活动参数检查
            ActTempParam param = new ActTempParam();
            param.setTempId(tempId);
            List<ActTempParam> paramList = actTempParamMapperExt.selectByTempId(param);
            if (paramList == null || paramList.size()!=1) {
                throw new ExceptionErrorData("活动参数错误");
            }
            String paramId = paramList.get(0).getParamId();

            // 取得颜色代码
            String nameJson = JSONArray.toJSONString(skuList);
            List<GoodsCodeColorNameDto> list = erpGoodsMapperExt.getGoodsColorCodeByName(nameJson);
            // 更新活动参数表
            param = new ActTempParam();
            param.setParamId(paramId);
            param.setParamValue(JSONArray.toJSONString(list));
            param.setUpdateUserId(loginId);
            param.setUpdateTime(new Date());
            actTempParamMapper.updateByPrimaryKeySelective(param);
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
