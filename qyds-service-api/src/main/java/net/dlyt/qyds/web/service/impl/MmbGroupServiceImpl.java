package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbGroup;
import net.dlyt.qyds.common.dto.MmbGroupMember;
import net.dlyt.qyds.common.dto.ext.MmbGroupExt;
import net.dlyt.qyds.common.dto.ext.MmbGroupMemberExt;
import net.dlyt.qyds.common.form.MmbGroupForm;
import net.dlyt.qyds.dao.MmbGroupMapper;
import net.dlyt.qyds.dao.MmbGroupMemberMapper;
import net.dlyt.qyds.dao.ext.MmbGroupMapperExt;
import net.dlyt.qyds.dao.ext.MmbGroupMemberMapperExt;
import net.dlyt.qyds.web.service.MmbGroupService;
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
@Service("mmbGroupService")
public class MmbGroupServiceImpl implements MmbGroupService {

    @Autowired
    private MmbGroupMapperExt mmbGroupMapperExt;

    @Autowired
    private MmbGroupMapper mmbGroupMapper;

    @Autowired
    private MmbGroupMemberMapperExt mmbGroupMemberMapperExt;

    @Autowired
    private MmbGroupMemberMapper mmbGroupMemberMapper;

    public JSONObject getList(MmbGroupForm form) {

        JSONObject json = new JSONObject();
        try {
            //数据库检索 -- 过滤数据
            List<MmbGroupExt> list = mmbGroupMapperExt.selectGroupList(form);
            int allCount = mmbGroupMapperExt.getGroupListCount(form);

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

    public JSONObject getDetail(MmbGroupForm form) {

        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getGroupId())) {
                throw new ExceptionErrorParam("缺少主键参数");
            }

            MmbGroupExt result = mmbGroupMapperExt.select(form.getGroupId());

            if (result == null) {
                throw new ExceptionErrorData("该分组不存在");
            }

            json.put("data", result);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject add(MmbGroupForm form, String userId) {

        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getGroupName())) {
                throw new ExceptionErrorParam("分组名称不能为空");
            }

            if (StringUtil.isEmpty(form.getType())) {
                throw new ExceptionErrorParam("分组类型不能为空");
            }

            int count = mmbGroupMapperExt.countGroupByName(form);
            if (count >= 1) {
                throw new ExceptionErrorParam("已近存在相同名称的分组");
            }

            Date date = new Date();
            String id = UUID.randomUUID().toString();

            MmbGroup record = new MmbGroup();
            // 会员组ID
            record.setGroupId(id);
            // 分组类型
            record.setType(form.getType());
            // 分组名称
            record.setGroupName(form.getGroupName());
            // 是否有效
            record.setIsValid("0");
            // 删除标记
            record.setDeleted("0");
            // 修改人
            record.setUpdateUserId(userId);
            // 修改时间
            record.setUpdateTime(date);
            // 创建人
            record.setInsertUserId(userId);
            // 创建时间
            record.setInsertTime(date);

            mmbGroupMapper.insertSelective(record);

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
    public JSONObject edit(MmbGroupForm form, String userId) {

        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getGroupId())) {
                throw new ExceptionErrorParam("缺少主键参数");
            }

            if (StringUtil.isEmpty(form.getGroupName())) {
                throw new ExceptionErrorParam("分组名称不能为空");
            }

            if (StringUtil.isEmpty(form.getType())) {
                throw new ExceptionErrorParam("分组类型不能为空");
            }

            int count = mmbGroupMapperExt.countGroupByName(form);
            if (count >= 1) {
                throw new ExceptionErrorParam("已近存在相同名称的分组");
            }

            MmbGroup record = mmbGroupMapper.selectByPrimaryKey(form.getGroupId());

            if (record == null) {
                throw new ExceptionErrorData("该分组不存在");
            } else if (!"0".equals(record.getDeleted())) {
                throw new ExceptionErrorData("该分组已经被删除");
            }

            // 分组类型
            record.setType(form.getType());
            // 分组名称
            record.setGroupName(form.getGroupName());
            // 修改人
            record.setUpdateUserId(userId);
            // 修改时间
            record.setUpdateTime(new Date());

            mmbGroupMapper.updateByPrimaryKeySelective(record);

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
    public JSONObject delete(MmbGroupForm form, String userId) {

        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getGroupId())) {
                throw new ExceptionErrorParam("缺少主键参数");
            }

            MmbGroup record = mmbGroupMapper.selectByPrimaryKey(form.getGroupId());

            if (record == null) {
                throw new ExceptionErrorData("该分组不存在");
            } else if (!"0".equals(record.getDeleted())) {
                throw new ExceptionErrorData("该分组已经被删除");
            }

            Date current = new Date();

            // 删除标记
            record.setDeleted("1");
            // 修改人
            record.setUpdateUserId(userId);
            // 修改时间
            record.setUpdateTime(current);

            // 删除对应分组
            mmbGroupMapper.updateByPrimaryKeySelective(record);

            MmbGroupMember groupMember = new MmbGroupMember();
            groupMember.setGroupId(record.getGroupId());
            groupMember.setUpdateUserId(userId);
            groupMember.setUpdateTime(current);
            // 删除分组对应的所有成员关联信息
            mmbGroupMemberMapperExt.deleteGroup(groupMember);

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


    public JSONObject getMemberList(MmbGroupForm form) {
        JSONObject json = new JSONObject();
        try {
            //数据库检索 -- 过滤数据
            List<MmbGroupMemberExt> list = mmbGroupMapperExt.selectMemberList(form);
            int allCount = mmbGroupMapperExt.countMemberList(form);

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
    public JSONObject updateMemberList(MmbGroupForm form, String userId) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getGroupId())) {
                throw new ExceptionErrorParam("缺少主键参数");
            }

            Date date = new Date();

            for (String addItem : form.getAddList()) {
                MmbGroupMember record = new MmbGroupMember();
                String id = UUID.randomUUID().toString();
                record.setGroupMemberId(id);
                record.setGroupId(form.getGroupId());
                record.setMemberId(addItem);
                record.setIsValid("0");
                record.setCreatUserId(userId);
                record.setCreatTime(date);
                record.setDeleted("0");
                record.setUpdateUserId(userId);
                record.setUpdateTime(date);
                record.setInsertUserId(userId);
                record.setInsertTime(date);
                mmbGroupMemberMapper.insertSelective(record);
            }

            for (String delItem : form.getDelList()) {
                MmbGroupMember record = new MmbGroupMember();
                record.setGroupId(form.getGroupId());
                record.setMemberId(delItem);
                record.setDeleted("1");
                record.setUpdateUserId(userId);
                record.setUpdateTime(date);
                mmbGroupMemberMapperExt.removeMemberFormGroup(record);
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
