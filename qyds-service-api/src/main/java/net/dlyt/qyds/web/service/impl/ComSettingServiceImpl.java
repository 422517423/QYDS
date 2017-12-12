package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ActMaster;
import net.dlyt.qyds.common.dto.ActTemplate;
import net.dlyt.qyds.common.dto.ComSetting;
import net.dlyt.qyds.common.dto.CouponMaster;
import net.dlyt.qyds.common.dto.ext.ComSettingExt;
import net.dlyt.qyds.dao.ActMasterMapper;
import net.dlyt.qyds.dao.ComSettingMapper;
import net.dlyt.qyds.dao.CouponMasterMapper;
import net.dlyt.qyds.web.service.ActMasterService;
import net.dlyt.qyds.web.service.ComSettingService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by cjk on 2016/12/14.
 */
@Service("comSettingService")
public class ComSettingServiceImpl implements ComSettingService{

    @Autowired
    private ComSettingMapper comSettingMapper = null;
    @Autowired
    private ActMasterMapper actMasterMapper = null;
    @Autowired
    private CouponMasterMapper couponMasterMapper = null;

    @Override
    public JSONObject getDetail(){
        JSONObject json = new JSONObject();
        try {
            ComSetting comSetting = comSettingMapper.selectByPrimaryKey("QYDS");
            if(comSetting == null){
                //如果没有则创建
                comSetting = new ComSetting();
                comSetting.setComSettingId("QYDS");
                comSetting.setDeleted(Constants.DELETED_NO);
                comSetting.setInsertTime(new Date());
                comSetting.setInsertUserId("SYSTEM");
                comSetting.setUpdateTime(new Date());
                comSetting.setUpdateUserId("SYSTEM");
                comSettingMapper.insertSelective(comSetting);
                json.put("result", comSetting);
            }else{
                ComSettingExt comSettingExt = new ComSettingExt();
                BeanUtils.copyProperties(comSetting,comSettingExt);
                // 判断字段是否有值
                if(!StringUtil.isEmpty(comSetting.getFirstBuyActivity())){
                    comSettingExt.setFirstBuyActivityDetail( actMasterMapper.selectByPrimaryKey(comSetting.getFirstBuyActivity()));
                    comSettingExt.setFirstBuyCouponDetail(couponMasterMapper.selectByPrimaryKey(comSetting.getFirstBuyCoupon()));
                }
                json.put("result", comSettingExt);
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

    @Override
    public JSONObject edit(ComSettingExt form) {
        JSONObject json = new JSONObject();
        try {
            //如果没有则创建
            ComSetting comSetting = new ComSetting();
            comSetting.setComSettingId("QYDS");
            comSetting.setFirstBuyCoupon(form.getFirstBuyCoupon());
            comSetting.setFirstBuyActivity(form.getFirstBuyActivity());
            comSetting.setUpdateTime(new Date());
            comSetting.setUpdateUserId("SYSTEM");
            comSettingMapper.updateByPrimaryKeySelective(comSetting);
            json.put("result", comSetting);
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
