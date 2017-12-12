package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbContact;
import net.dlyt.qyds.common.form.MmbContactExt;
import net.dlyt.qyds.dao.MmbContactMapper;
import net.dlyt.qyds.dao.ext.MmbContactMapperExt;
import net.dlyt.qyds.web.service.MmbContactService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Congky on 16/8/2.
 */
@Service("mmbContactService")
public class MmbContactServiceImpl implements MmbContactService {

    @Autowired
    private MmbContactMapper mmbContactMapper;

    @Autowired
    private MmbContactMapperExt mmbContactMapperExt;

    /**
     * 添加送货地址
     *
     * @param form contactId:会员ID
     *             theme:省
     *             address:市
     *             telephone:区
     *             userName:街道
     *             comment:地址
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject add(MmbContact form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getTheme())) {
                throw new ExceptionErrorParam("请输入主题");
            }

            if (StringUtil.isEmpty(form.getTelephone())) {
                throw new ExceptionErrorParam("请输入联系电话");
            }

            if(!StringUtil.isTelephone(form.getTelephone())){
                throw new ExceptionErrorParam("联系电话输入格式不正确");
            }

            if (StringUtil.isEmpty(form.getUserName())) {
                throw new ExceptionErrorParam("请选择姓名");
            }

            if (StringUtil.isEmpty(form.getAddress())) {
                throw new ExceptionErrorParam("请输入邮件地址");
            }

            Date date = new Date();
            String id = UUID.randomUUID().toString();

            form.setContactId(id);
            form.setDeleted("0");
            form.setInsertTime(date);
            form.setUpdateTime(date);
            form.setInsertUserId("contat");
            form.setUpdateUserId("contat");
            // 添加新地址
            mmbContactMapper.insertSelective(form);

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


    /**
     * 待升级审批会员一览
     *
     * @param form
     * @return
     */
    public JSONObject getList(MmbContactExt form) {
        JSONObject json = new JSONObject();
        try {

            List<MmbContact> list = mmbContactMapperExt.selectAllList(form);
            int allCount = mmbContactMapperExt.selectCountAllList(form);

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

}
