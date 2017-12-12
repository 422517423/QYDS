package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.*;
import net.dlyt.qyds.common.form.*;
import net.dlyt.qyds.dao.*;
import net.dlyt.qyds.dao.ext.*;
import net.dlyt.qyds.web.service.ActMasterService;
import net.dlyt.qyds.web.service.CatcheRemoveService;
import net.dlyt.qyds.web.service.GdsMasterService;
import net.dlyt.qyds.web.service.PointsExchangeService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.DataUtils;
import net.dlyt.qyds.web.service.common.ErpSendUtil;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by congkeyan on 2016/7/19.
 */
@Service("pointsExchangeService")
@Transactional(readOnly = true)
public class PointsExchangeServiceImpl implements PointsExchangeService {


    protected final Logger log = LoggerFactory.getLogger(PointsExchangeServiceImpl.class);

    @Autowired
    ActMemberMapperExt actMemberMapperExt;

    @Autowired
    MmbGroupMemberMapperExt mmbGroupMemberMapperExt;

    @Autowired
    MmbMasterMapperExt mmbMasterMapperExt;

    @Autowired
    private ActMasterMapperExt actMasterMapperExt;

    //活动种类列表获取
    @Override
    @Transactional(readOnly = false)
    public JSONObject activityTypeService(String memberId){

        JSONObject json = new JSONObject();

        try{
            JSONObject jsonObject = JSON.parseObject(memberId);
            memberId = (String) jsonObject.get("memberId");
            ActMasterForm form = new ActMasterForm();
            form.setShopId(Constants.ORGID);
            List<ActMasterForm> list = actMasterMapperExt.selectPointsActList(form);

            List<ActMasterForm> activities = new ArrayList<ActMasterForm>();
            for (int i = 0; i < list.size(); i++) {
                ActMasterForm activity = list.get(i);
                if (activity != null && isContainsMember(activity, memberId)) {
                    activities.add(activity);
                }
            }
            json.put("data", activities);
            json.put("resultCode", Constants.NORMAL);
        }catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    private boolean isContainsMember(ActMasterForm activity, String memberId) {
        boolean isContainsMember = false;

        if (memberId == null || memberId.trim().length() == 0) {
            if (net.dlyt.qyds.web.service.common.ComCode.ActivityMemberType.ALL.equals(activity.getMemberType())) {
                //全部会员
                return true;
            } else {
                return false;
            }
        }

        if (net.dlyt.qyds.web.service.common.ComCode.ActivityMemberType.ALL.equals(activity.getMemberType())) {
            // 判断活动用户是否包含该用户
            //全部会员
            isContainsMember = true;
        } else if (net.dlyt.qyds.web.service.common.ComCode.ActivityMemberType.MEMBER_GROUP.equals(activity.getMemberType())) {
            ActMember member = new ActMember();
            member.setActivityId(activity.getActivityId());
            // 按分类
            List<ActMemberForm> memberGroupList = actMemberMapperExt.selectMemberGroupByActivityId(member);
            for (ActMemberForm form : memberGroupList) {
                // 判断memberId是否存在于某个组中
                MmbGroupMember record = new MmbGroupMember();
                record.setGroupId(form.getMemberId());
                record.setMemberId(memberId);
                List<MmbGroupMemberExt> memberList = mmbGroupMemberMapperExt.select(record);
                if (memberList != null && memberList.size() > 0) {
                    isContainsMember = true;
                    break;
                }
            }
        } else if (net.dlyt.qyds.web.service.common.ComCode.ActivityMemberType.MEMBER_LEVEL.equals(activity.getMemberType())) {
            ActMember member = new ActMember();
            member.setActivityId(activity.getActivityId());
            // 按分类
            List<ActMemberForm> memberLevelList = actMemberMapperExt.selectMemberLevelByActivityId(member);
            for (ActMemberForm form : memberLevelList) {
                // 判断memberId是否存在于某个级别中
                MmbMasterExt record = new MmbMasterExt();
                record.setMemberId(memberId);
                record.setMemberLevelId(form.getMemberId());
                record.setDeleted(Constants.DELETED_NO);
                MmbMasterExt memberList = mmbMasterMapperExt.selectBySelective(record);
                if (memberList != null) {
                    isContainsMember = true;
                    break;
                }
            }
        }
        return isContainsMember;
    }

}
